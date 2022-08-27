package app;

import app.api.LoginHandler;
import app.api.RoleHandler;
import app.api.UserHandler;
import app.api.UserRoleHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import static app.Configuration.*;


class Application {

    public static void main(String[] args) throws IOException {
        int serverPort = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        UserHandler userHandler = new UserHandler(getUserService(), getObjectMapper(), getErrorHandler());
        RoleHandler roleHandler = new RoleHandler(getRoleService(), getObjectMapper(), getErrorHandler());
        UserRoleHandler userRoleHandler = new UserRoleHandler(getUserService(), getObjectMapper(), getErrorHandler());
        LoginHandler loginHandler = new LoginHandler(getUserService(), getObjectMapper(), getErrorHandler());

        server.createContext("/api/user", userHandler::handle);
        server.createContext("/api/role", roleHandler::handle);
        server.createContext("/api/user/add_role", userRoleHandler::handle);
        server.createContext("/api/login", loginHandler::handle);



        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
