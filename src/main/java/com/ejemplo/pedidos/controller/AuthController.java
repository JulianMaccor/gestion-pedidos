package com.ejemplo.pedidos.controller;

import com.ejemplo.pedidos.security.dto.LoginRequestDto;
import com.ejemplo.pedidos.security.dto.TokenResponseDto;
import com.ejemplo.pedidos.security.JwtService;
import com.ejemplo.pedidos.security.dto.UserDto;
import com.ejemplo.pedidos.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto){
        usuarioService.registrar(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuario creado: " + userDto.getUsername());
    }


    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto requestDto){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
        );

        String token = jwtService.generarToken(requestDto.getUsername());
        return new TokenResponseDto(token);

    }

}
