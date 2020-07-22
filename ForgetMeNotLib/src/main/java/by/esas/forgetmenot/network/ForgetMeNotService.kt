package by.esas.forgetmenot.network

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

internal data class ForgetMeNotService(val apolloClient: ApolloClient, val okHttpClient: OkHttpClient)