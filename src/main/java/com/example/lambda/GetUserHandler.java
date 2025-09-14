package com.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.S3Service;
import com.example.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class GetUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final S3Service s3Service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GetUserHandler() {
        String bucketName = System.getenv("BUCKET_NAME");
        this.s3Service = new S3Service(bucketName);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            String userId = request.getPathParameters().get("userId");
            User user = s3Service.getUser(userId);
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            
            if (user != null) {
                return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withHeaders(headers)
                    .withBody(objectMapper.writeValueAsString(user));
            } else {
                return new APIGatewayProxyResponseEvent()
                    .withStatusCode(404)
                    .withBody("{\"error\":\"User not found\"}");
            }
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                .withStatusCode(500)
                .withBody("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}