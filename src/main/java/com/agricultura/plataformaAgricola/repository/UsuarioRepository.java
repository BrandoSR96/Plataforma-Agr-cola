package com.agricultura.plataformaAgricola.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agricultura.plataformaAgricola.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombre(String nombre); // Si necesitas buscar por username
}
