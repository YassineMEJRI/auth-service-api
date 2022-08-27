package app.security;

import app.domain.role.Role;
import app.domain.role.RoleService;
import app.domain.user.User;
import app.domain.user.UserDto;
import app.domain.user.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AuthorizationTest {
    private UserService userService;
    private RoleService roleService;
    private User user;
    private Role role1;
    private Role role2;
    private String token;

    @Before
    public void setUp() throws Exception {
        roleService = new RoleService();
        userService = new UserService(roleService);

        user = userService.createUser(new UserDto("username", "password"));
        role1 = roleService.createRole("role1");
        role2 = roleService.createRole("role2");

        userService.addRole("username", "role1");
        userService.addRole("username", "role2");

        token = Authorization.authorize(user);
    }

    @Test
    public void getAuthorizedRoles() {
        List<String> authorizedRoles = Authorization.getAuthorizedRoles(token);
        assertEquals(2, authorizedRoles.size());
        assertTrue(authorizedRoles.contains(role1.getName()));
        assertTrue(authorizedRoles.contains(role2.getName()));
    }

    @Test
    public void checkUserRole() {
        assertTrue(Authorization.checkUserRole(token, role1.getName()));
        assertTrue(Authorization.checkUserRole(token, role2.getName()));
        assertFalse(Authorization.checkUserRole(token, "unexisting-role"));
    }

    @Test
    public void invalidate() {
        Authorization.invalidate(token);
        assertFalse(Authorization.isValid(token));
    }

    @Test
    public void isValid() {
        assertTrue(Authorization.isValid(token));
    }

    @Test
    public void testTokenExpiration() {
        try {
            Thread.sleep(3000); // Must change token expiration to 2 seconds before running the test
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertFalse(Authorization.verify(token));
    }

}