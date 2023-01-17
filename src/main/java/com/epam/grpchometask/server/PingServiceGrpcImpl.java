package com.epam.grpchometask.server;


import com.epam.grpchometask.stubs.ping.PingRequest;
import com.epam.grpchometask.stubs.ping.PingResponse;
import com.epam.grpchometask.stubs.ping.PingServiceGrpc;
import io.grpc.stub.StreamObserver;

public class PingServiceGrpcImpl extends PingServiceGrpc.PingServiceImplBase {

    @Override
    public void getResponse(PingRequest request, StreamObserver<PingResponse> responseObserver) {

        System.out.println("Ping server has received a message - " + request.getMessage());

        PingResponse pingResponse = PingResponse.newBuilder()
                .setMessage("Pong")
                .build();

        responseObserver.onNext(pingResponse);
        responseObserver.onCompleted();
    }
}
