package by.esas.forgetmenot.network

import android.util.Log
import by.esas.forgetmenot.model.error.ForgetMeNotError
import by.esas.forgetmenot.model.error.ForgetMeNotException
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.request.RequestHeaders
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal object ForgetMeNotClient {

    private val authService: ForgetMeNotService by lazy {
        ForgetMeNotServiceProvider.provideFaceIdService()
    }

    suspend fun <D : Operation.Data, T, V : Operation.Variables> startQuery(query: Query<D, T, V>): Response<T> {
        val token = ForgetMeNotServiceProvider.getAccessToken(authService.okHttpClient)
            ?: throw ForgetMeNotException(
                "Failed to get FaceId access token",
                ForgetMeNotError.NO_ACCESS_TOKEN
            )

        Log.d("FaceId", "FaceId access token: $token")
        val header = RequestHeaders.builder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return suspendCoroutine {
            val callback = object : ApolloCall.Callback<T>() {
                override fun onResponse(response: Response<T>) {
                    it.resume(response)
                }

                override fun onFailure(e: ApolloException) {
                    it.resumeWithException(
                        ForgetMeNotException(
                            "Failed to execute query", ForgetMeNotError.FAILED_QUERY, e
                        )
                    )
                }
            }

            authService.apolloClient.query(query)?.requestHeaders(header)?.enqueue(callback)
        }
    }

}