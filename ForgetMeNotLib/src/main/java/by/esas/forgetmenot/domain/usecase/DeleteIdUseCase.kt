package by.esas.forgetmenot.domain.usecase

import by.esas.forgetmenot.DeleteIdQuery
import by.esas.forgetmenot.model.error.ForgetMeNotError
import by.esas.forgetmenot.model.error.ForgetMeNotException
import by.esas.forgetmenot.model.input.DeleteIdInput
import by.esas.forgetmenot.model.result.DeleteIdResult
import by.esas.forgetmenot.network.ForgetMeNotClient
import by.esas.forgetmenot.type.ManagementStatusEnum
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import kotlin.coroutines.CoroutineContext

internal class DeleteIdUseCase(foregroundContext: CoroutineContext) :
    UseCase<DeleteIdInput, DeleteIdResult>("IdRemoval", foregroundContext) {

    override suspend fun executeOnBackground(inputParam: DeleteIdInput): DeleteIdResult {
        val groupInput = Input.optional(inputParam.group)
        val uidInput = Input.optional(inputParam.userId)

        val query = DeleteIdQuery(groupInput, uidInput)
        val response = ForgetMeNotClient.startQuery(query)

        return getResultStatus(response)
    }

    private fun getResultStatus(response: Response<DeleteIdQuery.Data>): DeleteIdResult {
        val data = response.data()?.management()
        if (response.hasErrors() || data == null)
            throw ForgetMeNotException(
                "Response has no data" + response.errors(), ForgetMeNotError.EMPTY_RESPONSE
            )

        return when (data.status()) {
            ManagementStatusEnum.OK -> DeleteIdResult.OK
            ManagementStatusEnum.NOTFOUND, ManagementStatusEnum.NOCHANGES -> DeleteIdResult.ID_NOT_FOUND
            else -> DeleteIdResult.INNER_ERROR
        }
    }
}