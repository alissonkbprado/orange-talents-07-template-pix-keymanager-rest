package br.com.zup.academy.alissonprado.validation

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton
import java.util.*
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [UuidValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER
)
annotation class Uuid(
    val message: String = "Formato de Uuid invalido",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class UuidValidator : ConstraintValidator<Uuid, String> {

    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<Uuid>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value.isNullOrBlank())
            return false

        return UUID.fromString(value).toString() == value
    }

}
