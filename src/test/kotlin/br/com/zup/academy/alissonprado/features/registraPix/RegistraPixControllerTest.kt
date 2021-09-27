package br.com.zup.academy.alissonprado.features.registraPix

import br.com.zup.academy.alissonprado.RegistraPixResponse
import br.com.zup.academy.alissonprado.RegistraPixServiceGrpc
import br.com.zup.academy.alissonprado.grpc.GrpcClientFactory
import br.com.zup.academy.alissonprado.model.TipoChave
import br.com.zup.academy.alissonprado.model.TipoConta
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*

@MicronautTest
internal class RegistraPixControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var registraStub: RegistraPixServiceGrpc.RegistraPixServiceBlockingStub

    lateinit var registraChavePixRequest: RegistraChavePixRequest

    lateinit var responseGrpc: RegistraPixResponse

    companion object {
        val PIX_ID = UUID.randomUUID().toString()
        val CLIENTE_ID = UUID.randomUUID().toString()
        val CHAVE = "teste@teste.com"
    }

    @BeforeEach
    fun setup() {
        registraChavePixRequest = RegistraChavePixRequest(
            tipoConta = TipoConta.CONTA_CORRENTE,
            tipoChave = TipoChave.EMAIL,
            chave = CHAVE
        )

        responseGrpc = RegistraPixResponse.newBuilder()
            .setIdPix(PIX_ID)
            .build()
    }


    @Test
    fun `deve cadastrar nova chave Pix`() {

        Mockito.`when`(registraStub.registraPix(Mockito.any())).thenReturn(responseGrpc)

        val request = HttpRequest.POST("/api/clientes/$CLIENTE_ID/pix/", registraChavePixRequest)
        val response = client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertEquals(
            response.header("Location"),
            ("/api/clientes/$CLIENTE_ID/pix/$PIX_ID")
        )
    }

    @Test
    fun `deve cadastrar nova chave Pix com TipoConta = CONTA_POUPANCA`() {

        val registraChavePixRequest = RegistraChavePixRequest(
            tipoConta = TipoConta.CONTA_POUPANCA,
            tipoChave = TipoChave.EMAIL,
            chave = CHAVE
        )

        Mockito.`when`(registraStub.registraPix(Mockito.any())).thenReturn(responseGrpc)

        val request = HttpRequest.POST("/api/clientes/$CLIENTE_ID/pix/", registraChavePixRequest)
        val response = client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertEquals(
            response.header("Location"),
            ("/api/clientes/$CLIENTE_ID/pix/$PIX_ID")
        )
    }

    @Test
    fun `deve cadastrar nova chave Pix para TipoChave = ALEATORIA`() {

        given(registraStub.registraPix(Mockito.any())).willReturn(responseGrpc)

        val registraChavePixRequest = RegistraChavePixRequest(
            tipoConta = TipoConta.CONTA_CORRENTE,
            tipoChave = TipoChave.ALEATORIA,
            chave = null
        )

        val request = HttpRequest.POST("/api/clientes/$CLIENTE_ID/pix/", registraChavePixRequest)
        val response = client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertEquals(
            response.header("Location"),
            ("/api/clientes/$CLIENTE_ID/pix/$PIX_ID")
        )
    }

    @Test
    fun `deve cadastrar nova chave Pix para TipoChave = CPF`() {

        given(registraStub.registraPix(Mockito.any())).willReturn(responseGrpc)

        val registraChavePixRequest = RegistraChavePixRequest(
            tipoConta = TipoConta.CONTA_CORRENTE,
            tipoChave = TipoChave.CPF,
            chave = "00011122233"
        )

        val request = HttpRequest.POST("/api/clientes/$CLIENTE_ID/pix/", registraChavePixRequest)
        val response = client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertEquals(
            response.header("Location"),
            ("/api/clientes/$CLIENTE_ID/pix/$PIX_ID")
        )
    }

    @Test
    fun `deve cadastrar nova chave Pix para TipoChave = CELULAR`() {

        given(registraStub.registraPix(Mockito.any())).willReturn(responseGrpc)

        val registraChavePixRequest = RegistraChavePixRequest(
            tipoConta = TipoConta.CONTA_CORRENTE,
            tipoChave = TipoChave.CELULAR,
            chave = "+5541999999999"
        )

        val request = HttpRequest.POST("/api/clientes/$CLIENTE_ID/pix/", registraChavePixRequest)
        val response = client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertEquals(
            response.header("Location"),
            ("/api/clientes/$CLIENTE_ID/pix/$PIX_ID")
        )
    }


    @Test
    fun `nao deve cadastrar nova chave Pix se a chave for maior que 77 caracteres`() {

        val registraChavePixRequest = RegistraChavePixRequest(
            tipoConta = TipoConta.CONTA_CORRENTE,
            tipoChave = TipoChave.CPF,
            chave = "012345678901234567890123456789012345678901234567890123456789012345678912345678"
        )

        val request = HttpRequest.POST("/api/clientes/$CLIENTE_ID/pix/", registraChavePixRequest)

        val error = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, error.status)
        assertEquals("Bad Request", error.message)
    }



    @Test
    fun `nao deve cadastrar se TipoChave = CPF e chave for incorreta`() {

        val registraChavePixRequest = RegistraChavePixRequest(
            tipoConta = TipoConta.CONTA_CORRENTE,
            tipoChave = TipoChave.CPF,
            chave = "teste@teste.com"
        )

        val request = HttpRequest.POST("/api/clientes/$CLIENTE_ID/pix/", registraChavePixRequest)

        val error = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, error.status)
        assertEquals("Bad Request", error.message)
    }

    @Test
    fun `nao deve cadastrar se TipoChave = CELULAR e chave for incorreta`() {

        val registraChavePixRequest = RegistraChavePixRequest(
            tipoConta = TipoConta.CONTA_CORRENTE,
            tipoChave = TipoChave.CELULAR,
            chave = "teste@teste.com"
        )

        val request = HttpRequest.POST("/api/clientes/$CLIENTE_ID/pix/", registraChavePixRequest)

        val error = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, error.status)
        assertEquals("Bad Request", error.message)
    }

    @Test
    fun `nao deve cadastrar se TipoChave = EMAIL e chave for incorreta`() {

        val registraChavePixRequest = RegistraChavePixRequest(
            tipoConta = TipoConta.CONTA_CORRENTE,
            tipoChave = TipoChave.EMAIL,
            chave = "00000000000"
        )

        val request = HttpRequest.POST("/api/clientes/$CLIENTE_ID/pix/", registraChavePixRequest)

        val error = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, error.status)
        assertEquals("Bad Request", error.message)
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    @Introspected
    class MochitoStubFactory {
        @Singleton
        fun stubMock(): RegistraPixServiceGrpc.RegistraPixServiceBlockingStub {
            return Mockito.mock(RegistraPixServiceGrpc.RegistraPixServiceBlockingStub::class.java)
        }
//        fun stubMock() = Mockito.mock(RegistraPixServiceGrpc.RegistraPixServiceBlockingStub::class.java)
    }

}


//    @Test
//    fun `nao deve cadastrar nova chave Pix se o clienteId for invalido`() {
//
//        val clientIdInvalido = "123456"
//
//        given(registraStub.registraPix(Mockito.any())).willThrow(
//            StatusRuntimeException(
//                Status.INVALID_ARGUMENT.withDescription(
//                    "Invalid UUID string: $clientIdInvalido"
//                )
//            )
//        )
//
//        val request = HttpRequest.POST(
//            "/api/clientes/$clientIdInvalido/pix/", registraChavePixRequest
//        )
//        val response = client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)
//
//        val error = assertThrows<HttpClientResponseException> {
//            client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)
//        }
//
//        assertEquals(HttpStatus.BAD_REQUEST, error.status)
//        assertEquals("Invalid UUID string: $CLIENTE_ID", error.message)
//    }
//
//    @Test
//    fun `nao deve cadastrar nova chave Pix se o clienteId nao for encontrado`() {
//
//        val clientIdInvalido = UUID.randomUUID().toString()
//
//        given(registraStub.registraPix(Mockito.any())).willThrow(
//            StatusRuntimeException(
//                Status.FAILED_PRECONDITION.withDescription(
//                    "Não encontrados dados do cartão com a instituição financeira"
//                )
//            )
//        )
//
//        val request = HttpRequest.POST("/api/clientes/$clientIdInvalido/pix/", registraChavePixRequest)
//
//        val error = assertThrows<HttpClientResponseException> {
//            client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)
//        }
//
//        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, error.status)
//        assertEquals("Não encontrados dados do cartão com a instituição financeira", error.message)
//    }
//
//    @Test
//    fun `nao deve cadastrar nova chave Pix se a chave ja estiver cadastrada`() {
//
//        given(registraStub.registraPix(Mockito.any())).willThrow(
//            StatusRuntimeException(
//                Status.ALREADY_EXISTS.withDescription(
//                    "Valor de chave informado já está registrado"
//                )
//            )
//        )
//
//        val request = HttpRequest.POST("/api/clientes/$CLIENTE_ID/pix/", registraChavePixRequest)
//
//        val error = assertThrows<HttpClientResponseException> {
//            client.toBlocking().exchange(request, RegistraChavePixRequest::class.java)
//        }
//
//        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, error.status)
//        assertEquals("Valor de chave informado já está registrado", error.message)
//    }



