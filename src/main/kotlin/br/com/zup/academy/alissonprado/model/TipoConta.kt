package br.com.zup.academy.alissonprado.model

enum class TipoConta {
    CONTA_CORRENTE,
    CONTA_POUPANCA
}

fun TipoConta.getTipoContaGrpc(): br.com.zup.academy.alissonprado.TipoConta {
    when (this.name) {
        "CONTA_CORRENTE" -> return br.com.zup.academy.alissonprado.TipoConta.CONTA_CORRENTE
        "CONTA_POUPANCA" -> return br.com.zup.academy.alissonprado.TipoConta.CONTA_CORRENTE
        else -> return br.com.zup.academy.alissonprado.TipoConta.CONTA_DESCONHECIDA
    }
}