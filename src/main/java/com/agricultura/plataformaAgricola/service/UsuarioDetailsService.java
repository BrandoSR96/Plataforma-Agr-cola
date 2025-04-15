package com.agricultura.plataformaAgricola.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.agricultura.plataformaAgricola.model.Usuario;
import com.agricultura.plataformaAgricola.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByNombre(nombre)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
            usuario.getNombre(),
            usuario.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority(usuario.getRole().name()))
        );
    }
}