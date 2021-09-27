package br.com.zup.academy.alissonprado.features.registraPix

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Validated
@Controller(value = "/api/chavesPix")
class RegistraPixController(
    private val service: RegistraPixService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Post
    fun registra(@Body @Valid request: RegistraChavePixRequest): HttpResponse<Any> {

        logger.info(
            "Recebida requisição para registro de nova chave Pix. idCliente: ${
                request.idClienteBanco.replaceAfter(
                    "-",
                    "***"
                )
            } - chave: ${request.chave}"
        )

        val idPix = service.registra(request)

        val uri = UriBuilder.of("/api/chavesPix/{idPix}")
            .expand(mutableMapOf(Pair("idPix", idPix)))

        return HttpResponse.created<Any?>(uri).body(hashMapOf("idPix" to idPix))
    }
}