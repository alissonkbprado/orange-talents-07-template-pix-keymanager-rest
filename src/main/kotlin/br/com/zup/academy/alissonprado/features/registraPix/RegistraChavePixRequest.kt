package br.com.zup.academy.alissonprado.features.registraPix

import br.com.zup.academy.alissonprado.RegistraPixRequest
import br.com.zup.academy.alissonprado.model.TipoChave
import br.com.zup.academy.alissonprado.model.TipoConta
import br.com.zup.academy.alissonprado.model.getTipoChaveGrpc
import br.com.zup.academy.alissonprado.model.getTipoContaGrpc
import br.com.zup.academy.alissonprado.validation.PixValidator
import br.com.zup.academy.alissonprado.validation.Uuid
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@PixValidator
@Introspected
data class RegistraChavePixRequest(
    @field:NotNull
    val tipoConta: TipoConta?,

    @field:NotNull
    val tipoChave: TipoChave?,

    @field:Size(max = 77)
    val chave: String?
) {

    fun toGrpc(@NotBlank @Uuid clienteId: String) : RegistraPixRequest {

        return RegistraPixRequest.newBuilder()
            .setIdClienteBanco(clienteId)
            .setTipoConta(this.tipoConta?.getTipoContaGrpc() ?: br.com.zup.academy.alissonprado.TipoConta.CONTA_DESCONHECIDA)
            .setTipoChave(this.tipoChave?.getTipoChaveGrpc() ?: br.com.zup.academy.alissonprado.TipoChave.CHAVE_DESCONHECIDA)
            .setChave(this.chave ?: "")
            .build()
    }
}
