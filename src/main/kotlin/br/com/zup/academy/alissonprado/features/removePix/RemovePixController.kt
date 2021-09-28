package br.com.zup.academy.alissonprado.features.removePix

import br.com.zup.academy.alissonprado.RemovePixRequest
import br.com.zup.academy.alissonprado.RemovePixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import jakarta.inject.Inject
import org.slf4j.LoggerFactory


@Controller(value = "/api/clientes/{clienteId}")
class RemovePixController(
    @Inject val grpcClient: RemovePixServiceGrpc.RemovePixServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Delete(value = "/pix/{idPix}")
    fun remove(clienteId: String, idPix: String): HttpResponse<Any> {

        logger.info(
            "Recebida requisição para remover chave Pix. idCliente: ${
                clienteId?.replaceAfter("-", "***")
            } - idPix: ${idPix?.replaceAfter("-", "***")}"
        )

        val response = grpcClient.removePix(
            RemovePixRequest.newBuilder()
                .setIdPix(idPix)
                .setIdClienteBanco(clienteId)
                .build()
        )

        logger.info("Chave Pix removida: idPix ${idPix?.replaceAfter("-", "***")}")

        return HttpResponse.ok(mutableMapOf(Pair("mensagem:", response.mensagem)))
    }
}