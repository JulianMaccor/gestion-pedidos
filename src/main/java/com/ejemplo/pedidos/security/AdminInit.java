package com.ejemplo.pedidos.security;

import com.ejemplo.pedidos.repository.UsuarioRepository;
import com.ejemplo.pedidos.security.dto.UserDto;
import com.ejemplo.pedidos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInit implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @Value("${admin.inicial.username}")
    private String adminUsername;
    @Value("${admin.inicial.password}")
    private String adminPassword;

    public AdminInit(UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }


    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByUsername(adminUsername).isEmpty()) {
            UserDto dto = new UserDto(adminUsername, adminPassword, "ADMIN");
            usuarioService.registrarAdmin(dto);
            System.out.println(">>> Admin inicial creado: " + adminUsername);
        }
    }
}
