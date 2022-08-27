package app.domain;

public class UserRoleDto {
    private String username;
    private String rolename;

    public UserRoleDto() {
    }

    public UserRoleDto(String username, String rolename) {
        this.username = username;
        this.rolename = rolename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
