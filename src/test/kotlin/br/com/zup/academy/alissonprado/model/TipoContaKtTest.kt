package br.com.zup.academy.alissonprado.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class TipoContaKtTest{

    @Test
    fun `deve retornar como conta desconhecida`(){

        val tipoConta: TipoConta = TipoConta.CONTA_DESCONHECIDA

        assertEquals(tipoConta.getTipoContaGrpc().name, "CONTA_DESCONHECIDA")
    }
}