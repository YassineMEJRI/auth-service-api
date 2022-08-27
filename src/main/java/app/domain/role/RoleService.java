package app.domain.role;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoleService {
    private final HashMap<String, Role> roles;

    public RoleService() {
        this.roles = new HashMap<>();
    }

    public Role createRole(String name) {
        Role role = new Role(name);
        if( roles.putIfAbsent(name, role) != null ) {
            return null;
        }
        else return role;
    }

    public Role getRoleByName(String name) {
        return roles.get(name);
    }

    public boolean deleteRole(String name) {
        return roles.remove(name) != null;
    }

    public List<Role> getAllRoles() {
        return new ArrayList<>(roles.values());
    }

    public HashMap<String, Role> getRoles() {
        return roles;
    }
}