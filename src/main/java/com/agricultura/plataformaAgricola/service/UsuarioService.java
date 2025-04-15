package com.agricultura.plataformaAgricola.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agricultura.plataformaAgricola.model.Usuario;
import com.agricultura.plataformaAgricola.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public List<Usuario> listarUsuario(){
		return usuarioRepository.findAll();
	}
	
	public Optional<Usuario> obtenerUsuarioPorId(Long usuario_id){
		return usuarioRepository.findById(usuario_id);
	}
	
	public Optional<Usuario> obtenerPorEmail(String email){
		return usuarioRepository.findByEmail(email);
	}

	public Usuario guardarUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	public void eliminarUsuario(Long usuario_id) {
		usuarioRepository.deleteById(usuario_id);
	}
}
