package com.example.exceptions;

import com.google.gson.Gson;
import static spark.Spark.*;

public class GlobalExceptionHandler {

    public static void handleExceptions() {
        exception(IllegalArgumentException.class, (e, request, response) -> {
            response.status(400);
            response.type("application/json");

            Gson gson = new Gson();
            String errorMessage = "Error: " + e.getMessage();
            response.body(gson.toJson(errorMessage));
        });
    }
}
