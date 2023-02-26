package com.syafei.pokemontcg.coredata.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageData(
    val small: String,
    val large: String
) : Parcelable