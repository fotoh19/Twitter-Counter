package com.note.twittercounter.model

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TwitterApiService {
    @FormUrlEncoded
    @POST("1.1/statuses/update.json")
    fun postTweet(
        @Field("status") status: String
    ): Call<POST>
}