package com.jonrib.tasks.service;

public interface SecurityService {
    String findLoggedInUsername();

    String autoLogin(String username, String password);
}
