package by.esas.forgetmenot.domain

import by.esas.forgetmenot.domain.usecase.CompletionBlock
import by.esas.forgetmenot.domain.usecase.DeleteIdUseCase
import by.esas.forgetmenot.model.input.DeleteIdInput
import by.esas.forgetmenot.model.result.DeleteIdResult
import kotlin.coroutines.CoroutineContext

/**
 * Class used for managing IDs on the server.
 *
 * @param options Manager options.
 */
class ForgetMeNotManager(private val options: ForgetMeNotManagerOptions) {

    /**
     * Options for the manager.
     *
     * @param foregroundContext Main context of the identifier.
     * @param group Server group for identification.
     */
    data class ForgetMeNotManagerOptions(
        val foregroundContext: CoroutineContext,
        val group: String
    )

    /**
     * Delete user ID on the server.
     *
     * @param userId User ID to delete.
     * @param callbacks Callbacks for completion of deletion.
     */
    fun deleteId(
        userId: String,
        callbacks: CompletionBlock<DeleteIdResult>
    ) {
        val deleteIdInput = DeleteIdInput(userId, options.group)

        DeleteIdUseCase(options.foregroundContext)
            .execute(deleteIdInput, callbacks)
    }
}