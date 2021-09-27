package br.com.zup.academy.alissonprado.features.registraPix

import br.com.zup.academy.alissonprado.RegistraPixRequest
import br.com.zup.academy.alissonprado.RegistraPixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Validated
@Controller(value = "/api/clientes/{clienteId}")
class RegistraPixController(
    private val gRpcClient: RegistraPixServiceGrpc.RegistraPixServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Post(value = "/pix")
    fun registra(clienteId: String, @Body @Valid request: RegistraChavePixRequest): HttpResponse<Any> {

        logger.info(
            "Recebida requisição para registro de nova chave Pix. idCliente: ${
                clienteId?.replaceAfter(
                    "-",
                    "***"
                )
            } - chave: ${request.chave}"
        )

        val requestGrpc: RegistraPixRequest? = request.toGrpc(clienteId)

        val idPix = gRpcClient.registraPix(requestGrpc).idPix

        logger.info("Chave Pix registrada: idPix ${idPix?.replaceAfter("-", "***")}")

        val uri = UriBuilder.of("/api/clientes/{clienteId}/pix/{idPix}")
            .expand(mutableMapOf(Pair("clienteId", clienteId), Pair("idPix", idPix)))

        return HttpResponse.created<Any?>(uri).body(hashMapOf("idPix" to idPix))
    }
}