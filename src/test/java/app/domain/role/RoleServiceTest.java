package app.domain.role;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class RoleServiceTest {
    private RoleService roleService;

    @Before
    public void setUp() throws Exception {
        roleService = new RoleService();
    }

    @Test
    public void createRole() {
        roleService.createRole("admin");
        HashMap<String, Role> roles = roleService.getRoles();
        Role role = roles.get("admin");
        assertNotNull(role);
        assertEquals("admin", role.getName());
    }

    @Test
    public void getRoleByName() {
        roleService.createRole("admin");
        Role roleByName = roleService.getRoleByName("admin");
        assertNotNull(roleByName);
        assertEquals("admin", roleByName.getName());
    }

    @Test
    public void deleteRole() {
        roleService.createRole("admin");
        roleService.deleteRole("admin");

        Role roleByName = roleService.getRoleByName("admin");
        assertNull(roleByName);
    }

    @Test
    public void getAllRoles() {
        List<Role> allRoles = roleService.getAllRoles();
        assertTrue(allRoles.isEmpty());

        roleService.createRole("admin");
        roleService.createRole("user");

        allRoles = roleService.getAllRoles();
        assertEquals(2, allRoles.size());
    }
}