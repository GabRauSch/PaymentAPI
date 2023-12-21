package com.example.services;

import com.example.db.DatabaseConnector;
import com.example.models.Payment;
import com.google.gson.Gson;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;

public class PaymentProcessor {
    private boolean running;
    
    public void startPaymentQueueListener() {
        running = true;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {

            channel.queueDeclare("payment_queue", false, false, false, null);

            while (running) {
                channel.basicQos(1);
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    processPaymentMessage(message);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                };

                channel.basicConsume("payment_queue", false, deliverCallback, consumerTag -> {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processPaymentMessage(String message) {
        System.out.println("Received payment message: " + message);
    
        Gson gson = new Gson();
        Payment payment = gson.fromJson(message, Payment.class); 

        try (java.sql.Connection connection = DatabaseConnector.getConnection()) {
            String insertQuery = "INSERT INTO payments (amount, beneficiary, sender) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
    
            double amount = Double.parseDouble(payment.getAmount());

            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, payment.getBeneficiary());
            preparedStatement.setString(3, payment.getSender());
    
            preparedStatement.executeUpdate();
            System.out.println("Payment inserted into the database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPaymentQueueListener() {
        running = false;
    }
}
