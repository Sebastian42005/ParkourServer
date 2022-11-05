package com.example.rolemanagement.management;

import com.example.rolemanagement.role.Role;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RoleHandler {

    protected static final HashMap<String, Role> storage = new HashMap<>();

    public static void push(String token, Role role) {
        storage.put(token, role);
    }

    public static void remove(String token) {
        storage.remove(token);
    }

    protected static Role get(String token) {
        return storage.get(token);
    }
}
