package br.com.zup.academy.alissonprado.model

enum class TipoChave {
    CPF,
    CELULAR,
    EMAIL,
    ALEATORIA
}

fun TipoChave.getTipoChaveGrpc(): br.com.zup.academy.alissonprado.TipoChave {
    when (this.name) {
        "CPF" -> return br.com.zup.academy.alissonprado.TipoChave.CPF
        "CELULAR" -> return br.com.zup.academy.alissonprado.TipoChave.CELULAR
        "EMAIL" -> return br.com.zup.academy.alissonprado.TipoChave.EMAIL
        "ALEATORIA" -> return br.com.zup.academy.alissonprado.TipoChave.ALEATORIA
        else -> return br.com.zup.academy.alissonprado.TipoChave.CHAVE_DESCONHECIDA
    }
}
