package br.com.zup.academy.alissonprado.features.registraPix

import br.com.zup.academy.alissonprado.model.TipoChave
import br.com.zup.academy.alissonprado.model.TipoConta
import br.com.zup.academy.alissonprado.validation.PixValidator
import br.com.zup.academy.alissonprado.validation.Uuid
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@PixValidator
@Introspected
data class RegistraChavePixRequest(
    @field:NotBlank @Uuid
    val idClienteBanco: String,

    @field:NotNull
    val tipoConta: TipoConta,

    @field:NotNull
    val tipoChave: TipoChave,

    @field:Size(max = 77)
    val chave: String?
)
