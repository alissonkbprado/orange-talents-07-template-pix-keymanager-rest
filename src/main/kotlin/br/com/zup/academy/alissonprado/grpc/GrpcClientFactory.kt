package br.com.zup.academy.alissonprado.grpc

import br.com.zup.academy.alissonprado.RegistraPixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel(value = "keymanager-grpc") val channel: ManagedChannel) {

    @Singleton
    fun registraPixClientSutb() = RegistraPixServiceGrpc.newBlockingStub(channel)

//    @Bean
//    fun registraPixClientSutb(): RegistraPixServiceGrpc.RegistraPixServiceBlockingStub {
//        return RegistraPixServiceGrpc.newBlockingStub(channel)
//    }

}