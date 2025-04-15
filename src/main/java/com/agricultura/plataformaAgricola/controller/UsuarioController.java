package com.agricultura.plataformaAgricola.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agricultura.plataformaAgricola.model.Usuario;
import com.agricultura.plataformaAgricola.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<List<Usuario>> listarUsuario() {
		return ResponseEntity.ok(usuarioService.listarUsuario());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
		Optional<Usuario> usuOptional = usuarioService.obtenerUsuarioPorId(id);
		return usuOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
		if (usuario.getLastLogin() == null) {
			usuario.setLastLogin(LocalDateTime.now());
		}
		
		if (usuario.getProductos() == null) {
			usuario.setProductos(new ArrayList<>());
		}
		
		return ResponseEntity.ok(usuarioService.guardarUsuario(usuario));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioactualizado) {
		Optional<Usuario> existente = usuarioService.obtenerUsuarioPorId(id);
		if (existente.isPresent()) {
			Usuario usuario = existente.get();
			usuario.setNombre(usuarioactualizado.getNombre());
			usuario.setEmail(usuarioactualizado.getEmail());
			usuario.setPassword(usuarioactualizado.getPassword());
			usuario.setRole(usuarioactualizado.getRole());
			usuario.setEnabled(usuarioactualizado.isEnabled());
			return ResponseEntity.ok(usuarioService.guardarUsuario(usuario));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
		usuarioService.eliminarUsuario(id);
		return ResponseEntity.noContent().build();
	}

}
