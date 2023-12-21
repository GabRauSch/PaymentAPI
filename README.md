# Payment API with Message Processor

This is an API for an emulation of a Financial service (finpexxia).

This is supported by a basic process flow:

1. Requisition of payment
2. Basic Validation that data is correcly setted
3. Message enqueued
4. Database simple Insert as Processor

## Requirements:

JDK 
RabbitMQ
SQL Database

## how to use:

post call in "http://localhost:8080/payment/initiate" with the following body:

```
{
    "amount": 100,
    "beneficiary": "String",
    "sender": "String"
}
```
