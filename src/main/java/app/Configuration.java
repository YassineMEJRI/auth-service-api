package app;

import app.errors.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import app.domain.role.RoleService;
import app.domain.user.UserService;

class Configuration {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final RoleService ROLE_SERVICE = new RoleService();

    private static final UserService USER_SERVICE = new UserService(ROLE_SERVICE);
    private static final GlobalExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    static UserService getUserService() {
        return USER_SERVICE;
    }
    static RoleService getRoleService() {
        return ROLE_SERVICE;
    }
    public static GlobalExceptionHandler getErrorHandler() {
        return GLOBAL_ERROR_HANDLER;
    }
}
