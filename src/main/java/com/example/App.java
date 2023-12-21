package com.example;
import static spark.Spark.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.controllers.PaymentController;
import com.example.exceptions.GlobalExceptionHandler;
import com.example.services.PaymentProcessor;

import spark.Spark;

public class App 
{
    public static void main(String[] args) {
        port(8080);

        GlobalExceptionHandler.handleExceptions();

        
        post("/payment/initiate", PaymentController.initiatePayment); 
        
        PaymentProcessor paymentProcessor = new PaymentProcessor();
        paymentProcessor.startPaymentQueueListener();
        
        initializeServer();
    }
    
    private static void initializeServer() {
        enableCORS("*", "*", "*");
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Spark.stop();
            System.out.println("Server stopped gracefully");
        }));
    }
    
    private static void enableCORS(final String origin, final String methods, final String headers) {
        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", origin));
    }
}
