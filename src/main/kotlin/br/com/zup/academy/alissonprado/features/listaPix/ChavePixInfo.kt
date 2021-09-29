package br.com.zup.academy.alissonprado.features.listaPix

import br.com.zup.academy.alissonprado.ListaChavesPixResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class ChavePixInfo(response: ListaChavesPixResponse.ChavePix) {
    val chave = object {
        val idPix = response.idPix
        val tipoChave = response.tipoChave.name
        val chave = response.chave
        val tipoConta = response.tipoConta
        val criadaEm = response.criadaEm.let {
            LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
        }
    }
}

