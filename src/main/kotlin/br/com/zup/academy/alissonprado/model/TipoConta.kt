package br.com.zup.academy.alissonprado.model

import br.com.zup.academy.alissonprado.TipoConta.*

enum class TipoConta {
    CONTA_CORRENTE,
    CONTA_POUPANCA,
    CONTA_DESCONHECIDA
}

fun TipoConta.getTipoContaGrpc(): br.com.zup.academy.alissonprado.TipoConta {
    return when (this.name) {
        "CONTA_CORRENTE" -> CONTA_CORRENTE
        "CONTA_POUPANCA" -> CONTA_CORRENTE
        else -> CONTA_DESCONHECIDA
    }
}