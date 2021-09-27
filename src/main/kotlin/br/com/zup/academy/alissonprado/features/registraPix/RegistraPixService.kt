package br.com.zup.academy.alissonprado.features.registraPix

import br.com.zup.academy.alissonprado.RegistraPixRequest
import br.com.zup.academy.alissonprado.RegistraPixServiceGrpc
import br.com.zup.academy.alissonprado.model.TipoChave
import br.com.zup.academy.alissonprado.model.getTipoChaveGrpc
import br.com.zup.academy.alissonprado.model.getTipoContaGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class RegistraPixService(
    private val gRpcClient: RegistraPixServiceGrpc.RegistraPixServiceBlockingStub,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun registra(request: RegistraChavePixRequest): String {

        var requestGrpc: RegistraPixRequest? = null

        // Se a chave for do tipo ALEATORIA, o Request é criado sem o campo chave
        if (request.tipoChave == TipoChave.ALEATORIA) {
            requestGrpc = RegistraPixRequest.newBuilder()
                .setIdClienteBanco(request.idClienteBanco)
                .setTipoConta(request.tipoConta.getTipoContaGrpc())
                .setTipoChave(request.tipoChave.getTipoChaveGrpc())
                .build()
        } else {
            requestGrpc = RegistraPixRequest.newBuilder()
                .setIdClienteBanco(request.idClienteBanco)
                .setTipoConta(request.tipoConta.getTipoContaGrpc())
                .setTipoChave(request.tipoChave.getTipoChaveGrpc())
                .setChave(request.chave)
                .build()
        }

        try {
            return gRpcClient.registraPix(requestGrpc).idPix

        } catch (e: StatusRuntimeException) {
            val status = e.status
            val statusCode = status.code
            val description = status.description

            when (statusCode) {
                Status.Code.INVALID_ARGUMENT -> {
                    throw HttpStatusException(HttpStatus.BAD_REQUEST, description)
                }
                Status.Code.ALREADY_EXISTS -> {
                    throw HttpStatusException(HttpStatus.UNPROCESSABLE_ENTITY, description)
                }
                Status.Code.FAILED_PRECONDITION -> {
                    throw HttpStatusException(HttpStatus.UNPROCESSABLE_ENTITY, description)
                }
                else -> {
                    logger.warn("Falha ao tentar acessar o serviço gRPC Keymanager")
                    throw HttpStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Não foi possivel completar a requisição"
                    )
                }
            }
        } catch (e: Exception) {
            logger.error("Erro inesperado: ${e.printStackTrace()}")
            throw HttpStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro inesperado: ${e.message}"
            )
        }
    }
}