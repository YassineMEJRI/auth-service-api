package app.domain.user;

import app.domain.role.Role;
import app.domain.role.RoleService;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest {
    private UserService userService;
    private RoleService roleService;
    private Role role;

    @Before
    public void setUp() throws Exception {
        roleService = new RoleService();
        userService = new UserService(roleService);
        UserDto userDto = new UserDto("username", "securepassword");
        userService.createUser(userDto);
        role = roleService.createRole("admin");
    }

    @Test
    public void createUser() {
        UserDto userDto = new UserDto("user", "password");
        User user1 = userService.createUser(userDto);
        assertNotNull(user1);
    }

    @Test
    public void testDuplicateUsername() {
        UserDto userDto = new UserDto("username", "password");
        User user = userService.createUser(userDto);
        assertNull(user);
    }

    @Test
    public void getUserByUsername() {
        User user = userService.getUserByUsername("username");
        assertNotNull(user);
    }

    @Test
    public void deleteUser() {
        boolean deleteUser = userService.deleteUser("username");
        User user = userService.getUserByUsername("username");
        assertTrue(deleteUser);
        assertNull(user);
    }

    @Test
    public void getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        assertNotNull(allUsers);
        assertEquals(1, allUsers.size());
    }

    @Test
    public void addRole() {
        boolean addRole = false;
        try {
            addRole = userService.addRole("username", "admin");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertTrue(addRole);

        assertTrue(userService.getUserByUsername("username").roles.contains(role));
    }

    @Test
    public void deleteRole() {
        try {
            userService.addRole("username", "admin");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        boolean deleteRole = false;
        try {
            deleteRole = userService.deleteRole("username", "admin");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertTrue(deleteRole);

        assertFalse(userService.getUserByUsername("username").roles.contains(role));
    }

    @Test
    public void authenticate() {
        User user = userService.getUserByUsername("username");
        User authenticated = userService.authenticate("username", "securepassword");
        User unauthenticated = userService.authenticate("username", "falsepassword");
        assertEquals(user, authenticated);
        assertNull(unauthenticated);
    }
}