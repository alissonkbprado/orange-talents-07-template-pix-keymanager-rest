package br.com.zup.academy.alissonprado.validation

import br.com.zup.academy.alissonprado.features.registraPix.RegistraChavePixRequest
import br.com.zup.academy.alissonprado.model.TipoChave
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton
import javax.validation.Constraint
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

@MustBeDocumented
@Target(CLASS, FIELD, CONSTRUCTOR, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidaPixValidator::class])
annotation class PixValidator(
    val message: String = "Chave com formato inválido."
)

@Singleton
class ValidaPixValidator : ConstraintValidator<PixValidator, RegistraChavePixRequest> {

    override fun isValid(
        value: RegistraChavePixRequest?,
        annotationMetadata: AnnotationValue<PixValidator>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value?.tipoChave == null) {
            return false
        } else {

            when (value.tipoChave) {
                TipoChave.CPF -> return value.chave!!.matches("^[0-9]{11}\$".toRegex())

                TipoChave.CELULAR -> return value.chave!!.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())

                TipoChave.EMAIL -> return value.chave!!.matches("[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?".toRegex())

                TipoChave.ALEATORIA -> return true
            }
        }
    }

}
