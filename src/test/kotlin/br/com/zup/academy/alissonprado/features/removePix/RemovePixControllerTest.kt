package br.com.zup.academy.alissonprado.features.removePix

import br.com.zup.academy.alissonprado.RemovePixResponse
import br.com.zup.academy.alissonprado.RemovePixServiceGrpc
import br.com.zup.academy.alissonprado.grpc.GrpcClientFactory
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.hateoas.JsonError
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*

@MicronautTest
internal class RemovePixControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var removeStub: RemovePixServiceGrpc.RemovePixServiceBlockingStub

    companion object {
        val PIX_ID = UUID.randomUUID().toString()
        val CLIENTE_ID = UUID.randomUUID().toString()
        val CHAVE = "teste@teste.com"
    }

    @Test
    fun `deve remover chave Pix`() {

        Mockito.`when`(removeStub.removePix(Mockito.any())).thenReturn(dadosRemovePixResponse())

        val request = HttpRequest.DELETE<Any>("/api/clientes/${CLIENTE_ID}/pix/${PIX_ID}", null)
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertTrue(response.body.get().toString().contains("Chave $CHAVE removida com sucesso"))
    }

//    @Test
//    fun `nao deve remover idPix invalida`() {
//
//        val idPixInvalido = "123456"
//
////        given(removeStub.removePix(Mockito.any())).  willThrow(
////            StatusRuntimeException(
////                Status.INVALID_ARGUMENT.withDescription(
////                    "Invalid UUID string: $idPixInvalido"
////                )
////            )
////        )
//
//        Mockito.`when`(removeStub.removePix(Mockito.any()))
//            .thenThrow(StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription("Invalid UUID string: $idPixInvalido")))
//
//        val request = HttpRequest.DELETE<Any>("/api/clientes/${CLIENTE_ID}/pix/${idPixInvalido}", null)
//        val error = assertThrows<HttpClientResponseException> { client.toBlocking().exchange(request, Any::class.java) }
//
//        assertEquals(HttpStatus.BAD_REQUEST, error.status)
//        assertEquals("Invalid UUID string: $idPixInvalido", error.localizedMessage)
//    }

    private fun dadosRemovePixResponse(): RemovePixResponse {
        return RemovePixResponse.newBuilder().setMensagem("Chave $CHAVE removida com sucesso").build()
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class MochitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(RemovePixServiceGrpc.RemovePixServiceBlockingStub::class.java)
    }

}