package com.android.compose.data

import androidx.appsearch.annotation.Document
import androidx.appsearch.app.AppSearchSchema

/** This Class is Used To Store The User Info */
@Document
data class UserInfo(
    /** This is grouping logic mechanism */
    @Document.Namespace
    val nameSpace: String,
    /** Unique Identifier */
    @Document.Id
    val id: String,
    /** Generated Code makes this filed searchable */
    @Document.StringProperty(
        indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES
    )
    val title: String,
    /** Generated Code makes this filed searchable */
    @Document.StringProperty(
        indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES
    )
    val description: String,
    /** It used to filter the selected or unselected filed's */
    @Document.BooleanProperty
    val isDone: Boolean
)