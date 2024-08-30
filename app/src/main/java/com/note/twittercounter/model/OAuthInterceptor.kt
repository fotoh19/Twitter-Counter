package com.note.twittercounter.model

import oauth.signpost.OAuthConsumer
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import oauth.signpost.http.HttpRequest
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OAuthInterceptor(private val consumer: OAuthConsumer) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val signedRequest = consumer.sign(request)
        return proceed(signedRequest)
    }
}

private fun proceed(request: HttpRequest?): Response {
    TODO("Not yet implemented")
}

fun createRetrofitInstance(): TwitterApiService {
    val consumer = CommonsHttpOAuthConsumer(
        "IMLpFXxhVDWhAxMrmHec8IUeU",
        "nbnsNcxJ5zb303K6J8d1gPTLJQaipGCfe0ppRDWKF0eV10ICz0"
    )
    consumer.setTokenWithSecret("1813218983870394368-52VXNZdIz9R882AOBI1MwTefCXYkGs", "YCXgb7qcuk7jXJvi2m40I3MN0myWE8KxWgS6nPYtqRkCZ")

    val client = OkHttpClient.Builder()
        .addInterceptor(OAuthInterceptor(consumer))
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.twitter.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(TwitterApiService::class.java)
}
