package app.domain.user;

import app.domain.role.Role;
import app.domain.role.RoleService;
import app.errors.ApplicationException;
import app.errors.ApplicationExceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class UserService {
    private final HashMap<String, User> users;
    private final RoleService roleService;

    public UserService(RoleService roleService) {
        this.users = new HashMap<>();
        this.roleService = roleService;
    }

    public User createUser(UserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getPassword(), new HashSet<>());
        if( users.putIfAbsent(user.getUsername(), user) != null ) {
            return null;
        }
        else return user;
    }

    public User getUserByUsername(String username) {
        return users.get(username);
    }

    public boolean deleteUser(String username) {
        return users.remove(username) != null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean addRole(String userName, String rolename) throws Exception {
        Role role = roleService.getRoleByName(rolename);
        if (role == null)
            throw new Exception("Role " + rolename + " not found");

        User user = getUserByUsername(userName);
        if (user == null)
            throw new Exception("User " + userName + " not found");

        if (user.hasRole(role))
            throw new Exception("User " + userName + " already has role " + rolename);

        user.addRole(role);
        return true;
    }

    public boolean deleteRole(String username, String rolename) throws Exception {
        Role role = roleService.getRoleByName(rolename);
        if (role == null)
            throw new Exception("Role " + rolename + " not found");

        User user = getUserByUsername(username);
        if (user == null)
            throw new Exception("User " + username + " not found");

        if(!user.hasRole(roleService.getRoleByName(rolename)))
            throw new Exception("User " + username + " has no role " + rolename);

        user.removeRole(role);
        return true;
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user == null)
            return null;
        if(password.equals(user.getPassword()))
            return user;
        return null;
    }
}
