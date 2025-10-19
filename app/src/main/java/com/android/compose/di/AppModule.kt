package com.android.compose.di

import com.android.compose.R
import android.content.Context
import androidx.room.Room
import com.android.compose.data.local.ShoppingItemDatabase
import com.android.compose.data.remote.PixarAPI
import com.android.compose.data.remote.local.local.ShoppingDao
import com.android.compose.other.Constant
import com.android.compose.repositories.DefaultShoppingRepository
import com.android.compose.repositories.ShoppingRepository
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, Constant.DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(dao: ShoppingDao, api: PixarAPI) = DefaultShoppingRepository(dao = dao, pixarAPI = api) as ShoppingRepository

    @Singleton
    @Provides
    fun provideShoppingDao(database: ShoppingItemDatabase) = database.shoppingDao()

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
    )

    @Singleton
    @Provides
    fun providePixarApi(): PixarAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://pixabay.com")
            .build()
            .create(PixarAPI::class.java)
    }

}