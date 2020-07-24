package by.esas.forgetmenot.network

import android.util.Log
import by.esas.forgetmenot.BuildConfig
import by.esas.forgetmenot.domain.ForgetMeNot
import by.esas.forgetmenot.utils.getCurrentDateTime
import com.apollographql.apollo.ApolloClient
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

internal object ForgetMeNotServiceProvider {

    private var accessToken: String? = null
    private var expiresAt: Long = 0

    fun provideFaceIdService(): ForgetMeNotService {
        ForgetMeNot.checkInitialization()

        val okHttpClient = provideOkHttpClient()
        val apolloClient = ApolloClient.builder()
            .serverUrl(ForgetMeNot.faceApiUrl!!).okHttpClient(okHttpClient).build()

        return ForgetMeNotService(apolloClient, okHttpClient)
    }

    @Suppress("ConstantConditionIf")
    private fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.BUILD_TYPE == "debug")
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)

        val tlsSupportingClient = enableTls12On(client)
        return tlsSupportingClient.build()
    }

    suspend fun getAccessToken(okHttpClient: OkHttpClient): String? {
        if (accessToken.isNullOrBlank() || expiresAt < getCurrentDateTime().time)
            if (!getNewAccessToken(okHttpClient))
                return null

        return accessToken!!
    }

    private suspend fun getNewAccessToken(okHttpClient: OkHttpClient): Boolean =
        suspendCoroutine { continuation ->
            ForgetMeNot.checkInitialization()

            val requestBody = FormBody.Builder()
                .add("grant_type", BuildConfig.FACE_API_GRANT_TYPE)
                .add("client_id", BuildConfig.FACE_API_CLIENT_ID)
                .add("scope", BuildConfig.FACE_API_SCOPE)
                .add("client_secret", ForgetMeNot.faceApiSecret!!)
                .build()

            val request = Request.Builder()
                .url(ForgetMeNot.faceApiTokenUrl!!)
                .header(BuildConfig.FACE_API_HEADER_NAME, BuildConfig.FACE_API_HEADER_VALUE)
                .post(requestBody)
                .build()

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) =
                    getAccessTokenResponse(response, continuation)

                override fun onFailure(call: Call, e: IOException) =
                    failedToGetAccessToken(e, continuation)
            })
        }

    private fun getAccessTokenResponse(response: Response, continuation: Continuation<Boolean>) {
        val responseBody = response.body?.string()

        if (responseBody.isNullOrBlank())
            continuation.resumeWith(Result.success(false))
        else {
            val jsonObject = JSONObject(responseBody)
            accessToken = jsonObject.getString("access_token")
            expiresAt = jsonObject.getLong("expires_in") + getCurrentDateTime().time
            continuation.resumeWith(Result.success(true))
        }
    }

    private fun failedToGetAccessToken(e: IOException, continuation: Continuation<Boolean>) {
        Log.e("ApolloClientConnection", "Failed to get an access token : ${e.message}")
        continuation.resumeWith(Result.success(false))
    }
}