package com.example.controllers;

import spark.Request;
import spark.Response;
import spark.Route;

import com.example.models.Payment;
import com.example.services.PaymentService;
import com.google.gson.Gson;

public class PaymentController {

    
    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

     public static Route initiatePayment = (Request request, Response response) -> {
        response.type("application/json"); // Set response content type to JSON
        
        Gson gson = new Gson();
        Payment payment = gson.fromJson(request.body(), Payment.class);
        
        PaymentService paymentService = new PaymentService();
        String initiationResult = paymentService.initiatePayment(payment);
        
        response.body(initiationResult);
        return response.body();
    };

    static class PaymentResponse {
        private String status;
        private String message;

        public PaymentResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

    }

}
