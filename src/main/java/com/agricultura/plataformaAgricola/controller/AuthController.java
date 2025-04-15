package com.agricultura.plataformaAgricola.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agricultura.plataformaAgricola.model.Usuario;
import com.agricultura.plataformaAgricola.repository.UsuarioRepository;
import com.agricultura.plataformaAgricola.util.JwtUtil;

import lombok.Data;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepo.save(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        System.out.println("Login intento con el usuario: " + request.getUsername());
        
        Usuario usuario = usuarioRepo.findByNombre(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        System.out.println("Usuario encontrado: " + usuario.getNombre()); // Log para confirmar

        if (passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
        	System.out.println("Comprobando usuario: " + request.getUsername());
        	System.out.println("Comprobando usuario: " + request.getPassword());

            String token = jwtUtil.generarToken(usuario.getNombre());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}

@Data
class AuthRequest {
    private String username;
    private String password;
}
