package com.example.services;

import com.example.models.Payment;
import com.google.gson.Gson;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class PaymentService {
    public boolean isValid(Payment payment){
        if (payment == null || payment.getAmount() == null || payment.getBeneficiary() == null || payment.getSender() == null) {
            throw new IllegalArgumentException("Invalid payment details. Please provide valid payment information.");
        }

        return isAmountValid(payment.getAmount()) && isBeneficiaryValid(payment.getBeneficiary()) && isSenderValid(payment.getSender());
    }
    private boolean isAmountValid(String amount) {
        try {
            double parsedAmount = Double.parseDouble(amount);
            return parsedAmount > 0;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    private boolean isBeneficiaryValid(String beneficiary) {
        return beneficiary != null && !beneficiary.trim().isEmpty();
    }
    private boolean isSenderValid(String sender) {
        return sender != null && !sender.trim().isEmpty();
    }

    public String initiatePayment(Payment payment){
        Gson gson = new Gson();
        PaymentResponse paymentResponse = new PaymentResponse();

        if (isValid(payment)) {
            paymentResponse.setMessage("Payment initiated successfully and sent to the Queue");

            System.out.println("sending to queue");
            try {
                sendToQueue(gson.toJson(payment));
            } catch (Exception e) {
                System.out.println("deu merda");
            }
        } else {
            paymentResponse.setMessage("Invalid payment. Payment cannot be initiated.");
        }

        return gson.toJson(paymentResponse);
    }

    static class PaymentResponse {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    private void sendToQueue(String message) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); 
            
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.queueDeclare("payment_queue", false, false, false, null);

                channel.basicPublish("", "payment_queue", null, message.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
