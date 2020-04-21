package com.jonrib.app.service;

public interface SecurityService {

    String autoLogin(String username, String password);

	String findLoggedInUsername(String token);
}
