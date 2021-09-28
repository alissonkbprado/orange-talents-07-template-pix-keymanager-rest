package br.com.zup.academy.alissonprado.features.pesquisaPix

import br.com.zup.academy.alissonprado.PesquisaChavePixResponse
import br.com.zup.academy.alissonprado.PesquisaChavePixServiceGrpc
import br.com.zup.academy.alissonprado.TipoChave
import br.com.zup.academy.alissonprado.TipoConta
import br.com.zup.academy.alissonprado.grpc.GrpcClientFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*

@MicronautTest
internal class PesquisaPixControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var detalhaStub: PesquisaChavePixServiceGrpc.PesquisaChavePixServiceBlockingStub

    companion object {
        val PIX_ID = UUID.randomUUID().toString()
        val CLIENTE_ID = UUID.randomUUID().toString()
        val CHAVE = "teste@teste.com"
    }

    @Test
    fun `deve pesquisar detalhes da chave Pix por IdPix e clienteId`() {

        Mockito.`when`(detalhaStub.pesquisaPix(Mockito.any())).thenReturn(dadosPesquisaPixResponse())

        val request = HttpRequest.GET<Any>("/api/clientes/${CLIENTE_ID}/pix/${PIX_ID}")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertTrue(response.body.get().toString().contains(PIX_ID))
        assertTrue(response.body.get().toString().contains(CLIENTE_ID))
        assertTrue(response.body.get().toString().contains(CHAVE))
        assertTrue(response.body.get().toString().contains("instituicao=Itau"))
        assertTrue(response.body.get().toString().contains("nomeTitular=Teste de Silva"))

    }

    @Test
    fun `deve pesquisar detalhes chave Pix por chave`() {

        Mockito.`when`(detalhaStub.pesquisaPix(Mockito.any())).thenReturn(dadosPesquisaPixResponse())

        val request = HttpRequest.GET<Any>("/api/clientes/${CLIENTE_ID}/chave/${CHAVE}")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertTrue(response.body.get().toString().contains(PIX_ID))
        assertTrue(response.body.get().toString().contains(CLIENTE_ID))
        assertTrue(response.body.get().toString().contains(CHAVE))
        assertTrue(response.body.get().toString().contains("instituicao=Itau"))
        assertTrue(response.body.get().toString().contains("nomeTitular=Teste de Silva"))

    }

    private fun dadosPesquisaPixResponse(): PesquisaChavePixResponse {
        return PesquisaChavePixResponse.newBuilder()
            .setIdPix(PIX_ID)
            .setIdClienteBanco(CLIENTE_ID)
            .setChave(
                PesquisaChavePixResponse.ChavePix.newBuilder()
                    .setTipo(TipoChave.EMAIL)
                    .setChave(CHAVE)
                    .setConta(
                        PesquisaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                            .setTipo(TipoConta.CONTA_CORRENTE)
                            .setInstituicao("Itau")
                            .setNomeDoTitular("Teste de Silva")
                            .setCpfDoTitular("00011122233")
                            .setAgencia("1234")
                            .setNumeroDaConta("123456")
                    )
                    .setCriadaEm(
                        Timestamp.newBuilder()
                            .setSeconds(LocalDateTime.now().second.toLong())
                            .setNanos(LocalDateTime.now().nano)
                            .build()
                    ).build()
            ).build()
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    @Introspected
    class MochitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(PesquisaChavePixServiceGrpc.PesquisaChavePixServiceBlockingStub::class.java)
    }

}
