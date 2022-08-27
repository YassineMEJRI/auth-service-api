package app.api;

import app.domain.TokenRole;
import app.errors.ApplicationExceptions;
import app.errors.GlobalExceptionHandler;
import app.security.Authorization;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CheckRoleHandler extends Handler {
    public CheckRoleHandler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
    }
    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            ResponseEntity<String> e = checkRole(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        }
        else {
            throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private ResponseEntity<String> checkRole(InputStream is) {
        String response;
        TokenRole tokenRole = super.readRequest(is, TokenRole.class);
        boolean hasRole = false;
        try {
            hasRole = Authorization.checkUserRole(tokenRole.getToken(), tokenRole.getRole());
            if (hasRole)
                response = "Authorized";
            else response = "Unauthorized";
        } catch (JWTVerificationException e ) {
            response = e.getMessage();
        }

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
}
