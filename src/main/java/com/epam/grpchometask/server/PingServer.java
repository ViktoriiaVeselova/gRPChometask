package com.epam.grpchometask.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PingServer {

    private static final Logger logger = Logger.getLogger(PingServer.class.getName());

    Server server;

    public void startServer() {

        int port = 8081;

        try {
            server = ServerBuilder.forPort(port)
                    .addService(new PingServiceGrpcImpl())
                    .build()
                    .start();

            logger.info("Server started on port " + port);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    PingServer.this.stopServer();
                }
            });
        } catch (IOException e) {
            logger.log(Level.ALL, "Server did not start", e);
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        if (server != null) {
            try {
                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.log(Level.ALL, "Server was shut down", e);
                throw new RuntimeException(e);
            }
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var userServer = new PingServer();
        userServer.startServer();
        userServer.blockUntilShutdown();
    }
}
