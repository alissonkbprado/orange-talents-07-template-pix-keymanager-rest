package br.com.zup.academy.alissonprado.features.pesquisaPix

import br.com.zup.academy.alissonprado.*
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Inject
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


@Controller(value = "/api/clientes/{clienteId}")
class PesquisaPixController(
    @Inject val grpcClient: PesquisaChavePixServiceGrpc.PesquisaChavePixServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Busca de destalhes da chave Pix buscando por idPix e id do cliente
     */
    @Get(value = "/pix/{idPix}")
    fun pesquisaPorIdPix(clienteId: String, idPix: String): HttpResponse<Any> {

        logger.info(
            "Recebida requisição para mostrar detalhes da chave Pix pesquisando por idPix. idCliente: ${
                clienteId?.replaceAfter("-", "***")
            } - idPix: ${idPix?.replaceAfter("-", "***")}"
        )

        val response = grpcClient.pesquisaPix(
            PesquisaChavePixRequest.newBuilder()
                .setPixId(
                    PesquisaChavePixRequest.FiltroPorPixId.newBuilder()
                        .setIdPix(idPix)
                        .setIdClienteBanco(clienteId)
                        .build()
                ).build()
        )

        val responseDto = response.toDto(response)

        return HttpResponse.ok(responseDto)
    }

    /**
     * Busca de destalhes da chave Pix buscando por chave
     */
    @Get(value = "/chave/{chavePix}")
    fun pesquisaPorChave(clienteId: String, chavePix: String): HttpResponse<Any> {

        logger.info(
            "Recebida requisição para mostrar detalhes da chave Pix pesquisando por chave. idCliente: ${
                chavePix.replaceAfter("-", "***")
            } - idPix: $chavePix"
        )

        val response = grpcClient.pesquisaPix(
            PesquisaChavePixRequest.newBuilder()
                .setChave(chavePix)
                .build()
        )

        val responseDto = response.toDto(response)

        return HttpResponse.ok(responseDto)
    }
}

private fun PesquisaChavePixResponse.toDto(response: PesquisaChavePixResponse): Any {
    return object {
        val pixId = response.idPix
        val clienteId = response.idClienteBanco
        val tipoChave = response.chave.tipo.name
        val chave = response.chave.chave
        val criadaEm = response.chave.criadaEm.let {
            LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
        }
        val Titular = object {
            val nomeTitular = response.chave.conta.nomeDoTitular
            val documentoTitular = response.chave.conta.cpfDoTitular
        }
        val conta = object {
            val instituicao = response.chave.conta.instituicao
            val numero = response.chave.conta.numeroDaConta
            val tipoConta = response.chave.conta.tipo.name
        }

    }
}
