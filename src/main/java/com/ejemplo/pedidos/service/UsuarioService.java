package com.ejemplo.pedidos.service;
import com.ejemplo.pedidos.security.dto.UserDto;
import com.ejemplo.pedidos.model.Usuario;
import com.ejemplo.pedidos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(UserDto userDto){
        String passwordHasheada = passwordEncoder.encode(userDto.getPassword());
        Usuario usuario = new Usuario(userDto.getUsername(), passwordHasheada, userDto.getRol());
        return usuarioRepository.save(usuario);
    }
}
