package br.com.zup.academy.alissonprado.grpc

import br.com.zup.academy.alissonprado.PesquisaChavePixServiceGrpc
import br.com.zup.academy.alissonprado.RegistraPixServiceGrpc
import br.com.zup.academy.alissonprado.RemovePixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel(value = "keymanager-grpc") val channel: ManagedChannel) {

    @Singleton
    fun registraPixClientSutb() = RegistraPixServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removePixClientSutb() = RemovePixServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun pesquisaPixClientSutb() = PesquisaChavePixServiceGrpc.newBlockingStub(channel)

//    @Bean
//    fun registraPixClientSutb(): RegistraPixServiceGrpc.RegistraPixServiceBlockingStub {
//        return RegistraPixServiceGrpc.newBlockingStub(channel)
//    }

}