package com.agricultura.plataformaAgricola.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agricultura.plataformaAgricola.model.Producto;
import com.agricultura.plataformaAgricola.repository.ProductoRespository;

@Service
public class ProductoService {
	
	@Autowired
	ProductoRespository ProductoRepository;
	
	public Producto registrarProducto(Producto producto) {
		return ProductoRepository.save(producto);
	}
	
	public List<Producto> listarProducto() {
		return ProductoRepository.findAll();
	}
	
	public Optional<Producto> buscarProductoXid(Long id) {
		return ProductoRepository.findById(id);
	}
	
	public Producto actualizarProducto(Producto producto, Long id) {
		Producto productoactualizar = ProductoRepository.findById(id)
				.orElseThrow(() -> new  RuntimeException("Producto no encontrado"));
		
		productoactualizar.setNombre(producto.getNombre());
		productoactualizar.setDescripcion(producto.getDescripcion());
		productoactualizar.setStock(producto.getStock());
		productoactualizar.setPrecio(producto.getPrecio());
		productoactualizar.setImagen(producto.getImagen());
		productoactualizar.setDisponible(producto.getDisponible());
		
		return ProductoRepository.save(productoactualizar);
	}
	
	public void eliminarProducto(Long id) {
		
		if (!ProductoRepository.existsById(id)) {
			throw new RuntimeException("Producto no encontrado");
		}
		
		ProductoRepository.deleteById(id);
	}
}
