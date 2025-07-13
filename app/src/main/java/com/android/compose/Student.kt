package com.android.compose

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(val id: Int, val name: String) : Parcelable