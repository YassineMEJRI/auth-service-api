package app.api;

import app.errors.ApplicationExceptions;
import app.errors.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import app.domain.role.Role;
import app.domain.role.RoleService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RoleHandler extends Handler {
    private final RoleService roleService;

    public RoleHandler(RoleService roleService, ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.roleService = roleService;
    }
    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            ResponseEntity e = createRole(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        }
        else if ("DELETE".equals(exchange.getRequestMethod())) {
            ResponseEntity e = deleteRole(exchange.getRequestBody());
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

    private ResponseEntity deleteRole(InputStream requestBody) {
        Role roleRequest = super.readRequest(requestBody, Role.class);
        String roleName = roleRequest.getName();

        String response;
        if (roleService.deleteRole(roleName))
            response = "Role deleted successfully";
        else response = "Role not found";

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

    private ResponseEntity createRole(InputStream is) {
        Role roleRequest = super.readRequest(is, Role.class);

        Role role = roleService.createRole(roleRequest.getName());
        String response;

        if (role != null)
            response = "Role created successfully";
        else response = "Role already exists";

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

}
