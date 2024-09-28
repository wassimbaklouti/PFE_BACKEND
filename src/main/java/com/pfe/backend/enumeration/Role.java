package com.pfe.backend.enumeration;

import static com.pfe.backend.constant.Authority.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_HANDYMAN(HANDYMAN_AUTHORITIES),
    ROLE_PROPERTYOWNER(PROPRETYOWNER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}


/*
    ROLE_USER(USER_AUTHORITIES),
    ROLE_AUDITOR(AUDITOR_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_DATA_SCIENTIST(DATA_SCIENTIST_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),

    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);
    */