package by.esas.forgetmenot.network

import android.os.Build
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.security.KeyStore
import javax.net.ssl.*


/**
 * If on [Build.VERSION_CODES.LOLLIPOP] or lower, sets [OkHttpClient.Builder.sslSocketFactory] to an instance of
 * [Tls12SocketFactory] that wraps the default [SSLContext.getSocketFactory] for [TlsVersion.TLS_1_2].
 *
 * Does nothing when called on [Build.VERSION_CODES.LOLLIPOP_MR1] or higher.
 *
 * For some reason, Android supports TLS v1.2 from [Build.VERSION_CODES.JELLY_BEAN], but the spec only has it
 * enabled by default from API [Build.VERSION_CODES.KITKAT]. Furthermore, some devices on
 * [Build.VERSION_CODES.LOLLIPOP] don't have it enabled, despite the spec saying they should.
 *
 * Method needs [X509TrustManager] to make trustworthy connections or it will use [Tls12SocketFactory.trustManager]
 * which allows ALL certificates and is not safe to use
 *
 * @return the (potentially modified) [OkHttpClient.Builder]
 */
internal fun enableTls12On(
    client: OkHttpClient.Builder,
    trustManagers: Array<TrustManager> = arrayOf(Tls12SocketFactory.trustManager)
): OkHttpClient.Builder {
    val trustManager = trustManagers.first { it is X509TrustManager } as X509TrustManager

    if (Build.VERSION.SDK_INT in 19..22) {
        try {
            val sslContext = SSLContext.getInstance(TlsVersion.TLS_1_2.javaName)
            sslContext.init(null, trustManagers, null)

            client.sslSocketFactory(
                Tls12SocketFactory(
                    sslContext.socketFactory
                ), trustManager)
        } catch (e: Exception) {
            Log.e("Tls12SocketFactory", "", e)
        }
    } else {
        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagers, null)

            client.sslSocketFactory(sslContext.socketFactory, trustManager)
        } catch (e: Exception) {
            Log.e("Tls12SocketFactory", "", e)
        }
    }
    return client
}

internal class Tls12SocketFactory(private val base: SSLSocketFactory) : SSLSocketFactory() {

    override fun getDefaultCipherSuites(): Array<String> = base.defaultCipherSuites

    override fun getSupportedCipherSuites(): Array<String> = base.supportedCipherSuites

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket? =
        patch(base.createSocket(s, host, port, autoClose))

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(host: String, port: Int): Socket? =
        patch(base.createSocket(host, port))

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(
        host: String,
        port: Int,
        localHost: InetAddress,
        localPort: Int
    ): Socket? = patch(base.createSocket(host, port, localHost, localPort))

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket? =
        patch(base.createSocket(host, port))

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress,
        port: Int,
        localAddress: InetAddress,
        localPort: Int
    ): Socket? = patch(base.createSocket(address, port, localAddress, localPort))

    private fun patch(s: Socket): Socket {
        if (s is SSLSocket)
            s.enabledProtocols =
                TLS_V12_ONLY

        return s
    }

    companion object {
        private val TLS_V12_ONLY = arrayOf("TLSv1.2")

        val trustManager by lazy {
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            trustManagerFactory.trustManagers
                .first { it is X509TrustManager } as X509TrustManager
        }
    }
}

