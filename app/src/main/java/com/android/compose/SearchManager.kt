package com.android.compose

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.appsearch.app.SearchSpec
import androidx.appsearch.app.AppSearchSession
import androidx.appsearch.app.SetSchemaRequest
import androidx.appsearch.app.PutDocumentsRequest
import androidx.appsearch.localstorage.LocalStorage
import com.android.compose.data.UserInfo

/** Search Manager Of Local App Search Database. */
class SearchManager(
    private val appContext: Context
) {
    /** is used to store the app search session. */
    private var session: AppSearchSession? = null

    /** Created The App Search Local Database. */
    suspend fun init() {
        withContext(context = Dispatchers.IO) {

            /** is Used to create/open the app search local database and returns session. */
            val sessionFuture = LocalStorage.createSearchSessionAsync(
                LocalStorage.SearchContext.Builder(
                    appContext,
                    "User_Info"
                ).build()
            )

            /** is waiting the async operation to complete. */
            session = sessionFuture.get()

            /** request obj to inform to the app search to store's this kind of instance. */
            val setSchemaRequest = SetSchemaRequest.Builder()
                .addDocumentClasses(UserInfo::class.java)
                .build()

            /** set the request. */
            session?.setSchemaAsync(setSchemaRequest)
        }
    }

    /** Is Used to Check the either the database is empty or not before insertion. */
    suspend fun isDataIsEmptyInTheDatabase(): Boolean {
        return withContext(context = Dispatchers.IO) {
            /** Is Used to Get All Item's From Page 1. */
            val existingUsers = session?.search(
                "",
                SearchSpec.Builder().build()
            ) ?: return@withContext false

            /** Used To Store The User Info In This List. */
            val result = mutableListOf<UserInfo>()

            /** Fetching The User Info Data and Adding To The List. */
            existingUsers.nextPageAsync.get().forEach { userInfo ->
                if (userInfo.genericDocument.schemaType == UserInfo::class.java.simpleName) {
                    result.add(userInfo.genericDocument.toDocumentClass(UserInfo::class.java))
                }
            }

            /** Is used to check the data is empty or not */
            result.isEmpty()
        }
    }

    /** Insert */
    suspend fun putItems(userInfos: List<UserInfo>): Boolean {
        return withContext(context = Dispatchers.IO) {
            /** Created the Request Obj to store the list of item's in the Local AppSearch Database. */
            val insertRequest = PutDocumentsRequest.Builder().addDocuments(userInfos).build()

            /** Is Performing the insert operation in async way and get() will block the underlying the thread until the async operation is done. */
            session?.putAsync(
                insertRequest
            )?.get()?.isSuccess == true
        }
    }


    /** Search */
    suspend fun search(query: String): List<UserInfo> {
        return withContext(context = Dispatchers.IO) {
            /** Creating The Request Obj to do the Search Operation. */
            val searchSpec = SearchSpec.Builder()
                /** To Perform the search operation on particular namespaces item's. */
                .addFilterNamespaces("Work_Related")
                /** Is used to get the repeated item's on top. */
                .setRankingStrategy(SearchSpec.RANKING_STRATEGY_USAGE_COUNT)
                /** build the obj with specified properties and return. */
                .build()

            /** If database is not created or closed then session will be null so in this case's return's the empty list. **/
            val result = session?.search(
                query,
                searchSpec
            ) ?: return@withContext emptyList()

            /** Start the search operation on 1st page and wait for the result and return.
             *  Every page contains the minimum no.of item's so when you called this statement again
             *  It goes to the next page and perform the same operation on it and returns the result.
             *  */
            val page = result.nextPageAsync.get()

            /** database returns the result in generic document type so we've to check the schemaType
             * and cast to the required class. */
            page.mapNotNull {
                if (it.genericDocument.schemaType == UserInfo::class.java.simpleName) {
                    it.genericDocument.toDocumentClass(UserInfo::class.java)
                } else {
                    null
                }
            }
        }
    }

    /** This Method is used to close the db and making to null. */
    fun closeSession() {
        session?.close()
        session = null
    }

}
