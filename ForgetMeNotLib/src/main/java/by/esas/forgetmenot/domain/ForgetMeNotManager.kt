package by.esas.forgetmenot.domain

import by.esas.forgetmenot.domain.usecase.CompletionBlock
import by.esas.forgetmenot.domain.usecase.DeleteIdUseCase
import by.esas.forgetmenot.model.input.DeleteIdInput
import by.esas.forgetmenot.model.result.DeleteIdResult
import kotlin.coroutines.CoroutineContext

class ForgetMeNotManager(private val options: ForgetMeNotManagerOptions) {

    data class ForgetMeNotManagerOptions(
        val foregroundContext: CoroutineContext,
        val group: String
    )

    fun deleteId(
        userId: String,
        callbacks: CompletionBlock<DeleteIdResult>
    ) {
        val deleteIdInput = DeleteIdInput(userId, options.group)

        DeleteIdUseCase(options.foregroundContext)
            .execute(deleteIdInput, callbacks)
    }
}