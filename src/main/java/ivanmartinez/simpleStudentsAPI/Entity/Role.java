package ivanmartinez.simpleStudentsAPI.Entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT,
    PROFESSOR,
    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
