package com.facts.factsbyshahzaib.model


import com.google.gson.annotations.SerializedName

data class Fact(
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("deleted")
    var deleted: Boolean? = null,
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("source")
    var source: String? = null,
    @SerializedName("status")
    var status: Status? = null,
    @SerializedName("text")
    var text: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("updatedAt")
    var updatedAt: String? = null,
    @SerializedName("used")
    var used: Boolean? = null,
    @SerializedName("user")
    var user: String? = null,
    @SerializedName("__v")
    var v: Int? = null
)