package br.com.zup.academy.alissonprado.model

import br.com.zup.academy.alissonprado.TipoConta.*

enum class TipoConta {
    CONTA_CORRENTE,
    CONTA_POUPANCA
}

fun TipoConta.getTipoContaGrpc(): br.com.zup.academy.alissonprado.TipoConta {
    when (this.name) {
        "CONTA_CORRENTE" -> return CONTA_CORRENTE
        "CONTA_POUPANCA" -> return CONTA_CORRENTE
        else -> return CONTA_DESCONHECIDA
    }
}