package com.epam.grpchometask.client;

import com.epam.grpchometask.stubs.ping.PingRequest;
import com.epam.grpchometask.stubs.ping.PingResponse;
import com.epam.grpchometask.stubs.ping.PingServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PingClient {

    private static final Logger logger = Logger.getLogger(PingClient.class.getName());

    private final PingServiceGrpc.PingServiceBlockingStub pingServiceBlockingStub;

    public PingClient(Channel channel) {
        this.pingServiceBlockingStub = PingServiceGrpc.newBlockingStub(channel);
    }

    public String getMessage(String requestMessage) {
        logger.info("Sending a message to the server - " + requestMessage);

        PingRequest pingRequest = PingRequest.newBuilder().setMessage(requestMessage).build();

        PingResponse pingResponse = pingServiceBlockingStub.getResponse(pingRequest);

        return pingResponse.getMessage();
    }

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8081").usePlaintext().build();

        PingClient pingClient = new PingClient(channel);
        String message = pingClient.getMessage("Ping");

        logger.info("Server has responded with a message - " + message);


        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.ALL, "Exception during server termination");
            throw new RuntimeException(e);
        }
    }
}
