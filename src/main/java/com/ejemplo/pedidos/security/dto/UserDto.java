package com.ejemplo.pedidos.security.dto;

public class UserDto {
    String username;
    String password;
    String rol;

    public UserDto(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
