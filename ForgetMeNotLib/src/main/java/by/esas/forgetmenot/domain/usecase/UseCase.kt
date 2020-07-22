package by.esas.forgetmenot.domain.usecase

import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext

typealias CompletionBlock<T> = UseCase.Request<T>.() -> Unit

abstract class UseCase<Y, T> private constructor(private val TAG: String) {

    private var parentJob: Job = Job()
    private lateinit var foregroundContext: CoroutineContext
    private var backgroundContext: CoroutineContext = Dispatchers.IO

    constructor(
        TAG: String,
        foregroundContext: CoroutineContext,
        backgroundContext: CoroutineContext = Dispatchers.IO
    ) : this(TAG) {
        this.foregroundContext = foregroundContext
        this.backgroundContext = backgroundContext
    }

    fun execute(inputParam: Y, block: CompletionBlock<T>) {
        Log.d(TAG, "Execution started")

        val response = Request<T>().apply { block() }
        unsubscribe()

        parentJob = Job()
        CoroutineScope(foregroundContext + parentJob).launch {
            try {
                val result = withContext(backgroundContext) {
                    executeOnBackground(inputParam)
                }

                Log.d(TAG, "Execution success")
                response(result)
            } catch (cancellationException: CancellationException) {
                Log.e(TAG, "Cancellation exception", cancellationException)
                response(cancellationException)
            } catch (e: Exception) {
                Log.e(TAG, "Error exception", e)
                response(e)
            }
        }
    }

    abstract suspend fun executeOnBackground(inputParam: Y): T

    fun unsubscribe() {
        parentJob.apply {
            cancelChildren()
            cancel()
        }
    }

    class Request<T> {
        private var onComplete: ((T) -> Unit)? = null
        private var onError: ((Exception) -> Unit)? = null
        private var onCancel: ((CancellationException) -> Unit)? = null

        fun onComplete(block: (T) -> Unit) {
            onComplete = block
        }

        fun onError(block: (Exception) -> Unit) {
            onError = block
        }

        fun onCancel(block: (CancellationException) -> Unit) {
            onCancel = block
        }

        operator fun invoke(result: T) {
            onComplete?.invoke(result)
        }

        operator fun invoke(error: Exception) {
            onError?.invoke(error)
        }

        operator fun invoke(error: CancellationException) {
            onCancel?.invoke(error)
        }
    }
}