package com.note.twittercounter.model

import android.widget.EditText
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TwitterApiService {
    @FormUrlEncoded
    @POST("2/tweets")
    fun postTweet(
        @Field("text") text: String
    ): Call<POST>
}