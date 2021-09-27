package br.com.zup.academy.alissonprado.model

enum class TipoChave {
    CPF,
    CELULAR,
    EMAIL,
    ALEATORIA
}

fun TipoChave.getTipoChaveGrpc(): br.com.zup.academy.alissonprado.TipoChave {
    return when (this.name) {
        "CPF" -> br.com.zup.academy.alissonprado.TipoChave.CPF
        "CELULAR" -> br.com.zup.academy.alissonprado.TipoChave.CELULAR
        "EMAIL" -> br.com.zup.academy.alissonprado.TipoChave.EMAIL
        "ALEATORIA" -> br.com.zup.academy.alissonprado.TipoChave.ALEATORIA
        else -> br.com.zup.academy.alissonprado.TipoChave.CHAVE_DESCONHECIDA
    }
}
