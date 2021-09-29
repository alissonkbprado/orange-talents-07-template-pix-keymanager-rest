package br.com.zup.academy.alissonprado.features.listaPix

import br.com.zup.academy.alissonprado.ListaChavesPixRequest
import br.com.zup.academy.alissonprado.ListaChavesPixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Inject
import org.slf4j.LoggerFactory

@Controller(value = "/api/clientes/{clienteId}")
class ListaPixController(
    @Inject private val grpcClient: ListaChavesPixServiceGrpc.ListaChavesPixServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get(value = "/pix/")
    fun listaChavesPix(clienteId: String): HttpResponse<Any> {

        logger.info(
            "Recebida requisição para listar todas as chaves. idCliente: ${
                clienteId?.replaceAfter("-", "***")
            }"
        )

        val response = grpcClient.listaChavesPix(
            ListaChavesPixRequest.newBuilder()
                .setIdClienteBanco(clienteId)
                .build()
        )

        val chaves = response.chavesList.map { chavePix -> ChavePixInfo(chavePix) }

        return HttpResponse.ok(object {
            val idClienteBanco = clienteId
            val chaves = chaves.let {
                if (it.isEmpty()) {
                    "Nenhuma chave resgistrada"
                } else {
                    it
                }
            }
        })
    }
}