package com.android.compose.musicPlayer

import androidx.annotation.RawRes
import androidx.annotation.DrawableRes
import com.android.compose.R

data class Track(
    val name: String,
    val desc: String,
    @RawRes val id: Int,
    @DrawableRes val image:Int
){
    constructor():this(name = "First song", desc = "First song description", id = R.raw.one, image = R.drawable.music)
}

val songs = listOf(
    Track(
        name = "First song",
        desc = "First song description",
        id = R.raw.one,
        image = R.drawable.one
    ),Track(
        name = "Second song",
        desc = "Second song description",
        id = R.raw.two,
        image = R.drawable.two
    ),Track(
        name = "Third song",
        desc = "Third song description",
        id = R.raw.three,
        image = R.drawable.three
    ),Track(
        name = "Four song",
        desc = "Four song description",
        id = R.raw.four,
        image = R.drawable.four
    ),Track(
        name = "Five song",
        desc = "Five song description",
        id = R.raw.five,
        image = R.drawable.five
    ),
)
