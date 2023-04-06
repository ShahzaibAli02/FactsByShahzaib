package com.facts.factsbyshahzaib.model


import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("feedback")
    var feedback: String? = null,
    @SerializedName("sentCount")
    var sentCount: Int? = null,
    @SerializedName("verified")
    var verified: Boolean? = null
)