package br.com.zup.academy.alissonprado.features.listaPix

import br.com.zup.academy.alissonprado.ListaChavesPixResponse
import br.com.zup.academy.alissonprado.ListaChavesPixServiceGrpc
import br.com.zup.academy.alissonprado.grpc.GrpcClientFactory
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
import java.util.*

@MicronautTest
internal class ListaPixControllerTest{

    @field:Inject
    @field:Client(value = "/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var listaStub: ListaChavesPixServiceGrpc.ListaChavesPixServiceBlockingStub

    companion object{
        val PIX_ID = UUID.randomUUID().toString()
        val CLIENTE_ID = UUID.randomUUID().toString()
        val CHAVE = "teste@teste.com"
    }

    @Test
    fun `deve retornar lista de chaves cadastradas`(){

        Mockito.`when`(listaStub.listaChavesPix(Mockito.any())).thenReturn(dadosListaChavesPixResponse())

        val request = HttpRequest.GET<Any>("/api/clientes/${CLIENTE_ID}/pix/")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertTrue(response.body.get().toString().contains(CLIENTE_ID))
        assertTrue(response.body.get().toString().contains(PIX_ID))
        assertTrue(response.body.get().toString().contains(CHAVE))
    }

    @Test
    fun `deve retornar lista vazia se nao tiver chaves cadastradas`(){

        Mockito.`when`(listaStub.listaChavesPix(Mockito.any())).thenReturn(ListaChavesPixResponse.newBuilder()
            .setIdClienteBanco(CLIENTE_ID)
            .build())

        val request = HttpRequest.GET<Any>("/api/clientes/${CLIENTE_ID}/pix/")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertTrue(response.body.get().toString().contains("Nenhuma chave resgistrada"))
    }

    private fun dadosListaChavesPixResponse(): ListaChavesPixResponse? {

        val chave1 = ListaChavesPixResponse.ChavePix.newBuilder()
            .setIdPix(PIX_ID)
            .setChave(CHAVE)
            .build()

        val chave2 = ListaChavesPixResponse.ChavePix.newBuilder()
            .setIdPix(UUID.randomUUID().toString())
            .setChave("teste2@teste.com")
            .build()

        return ListaChavesPixResponse.newBuilder()
            .setIdClienteBanco(CLIENTE_ID)
            .addAllChaves(listOf(chave1, chave2))
            .build()
    }


    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    @Introspected
    class MochitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(ListaChavesPixServiceGrpc.ListaChavesPixServiceBlockingStub::class.java)
    }
}
