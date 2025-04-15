package com.agricultura.plataformaAgricola.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agricultura.plataformaAgricola.model.Producto;
import com.agricultura.plataformaAgricola.model.Usuario;
import com.agricultura.plataformaAgricola.service.AzureBlobService;
import com.agricultura.plataformaAgricola.service.ProductoService;
import com.agricultura.plataformaAgricola.service.UsuarioService;

@RestController
@RequestMapping("/v1/api")
public class ProductoController {

	@Autowired
	AzureBlobService azureBlobService;
	
	@Autowired
	ProductoService productoService;
	
	@Autowired
	UsuarioService usuarioService;

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@PostMapping(value = "/productos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Producto> registrarProducto(
	        @RequestParam("nombre") String nombre,
	        @RequestParam("descripcion") String descripcion,
	        @RequestParam("stock") Integer stock,
	        @RequestParam("precio") Double precio,
	        @RequestParam("usuario") Long usuario_id,
	        @RequestPart("imagen") MultipartFile imagen
	) throws IOException {
	    String urlImagen = azureBlobService.uploadImage(imagen);
	    
	    Usuario usuario = usuarioService.obtenerUsuarioPorId(usuario_id)
	    		.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	    	
	    Producto producto = new Producto();
	    
	    producto.setNombre(nombre);
	    producto.setDescripcion(descripcion);
	    producto.setStock(stock);
	    producto.setPrecio(precio);
	    producto.setImagen(urlImagen);
	    producto.setDisponible(true);
	    producto.setUsuario(usuario);

	    return ResponseEntity.status(HttpStatus.CREATED).body(productoService.registrarProducto(producto));
	}


	@DeleteMapping("/productos/{id}")
	public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
		productoService.eliminarProducto(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/productos")
	public ResponseEntity<List<Producto>> listarProducto() {
		return ResponseEntity.ok(productoService.listarProducto());
	}

	@GetMapping("/productos/{id}")
	public ResponseEntity<Producto> buscarProductoxID(@PathVariable Long id) {
		Optional<Producto> productoOptional = productoService.buscarProductoXid(id);
		return productoOptional
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	/*@PutMapping("/productos/{id}")
	public ResponseEntity<Producto> actualizarProducto(@RequestBody Producto producto, @PathVariable Long id){
		return ResponseEntity.ok(productoService.actualizarProducto(producto, id));
	}*/
	@PutMapping(value = "/productos/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Producto> actualizarProducto(
	        @PathVariable Long id,
	        @RequestParam("nombre") String nombre,
	        @RequestParam("descripcion") String descripcion,
	        @RequestParam("stock") Integer stock,
	        @RequestParam("precio") Double precio,
	        @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

	    Optional<Producto> optionalProducto = productoService.buscarProductoXid(id);

	    if (optionalProducto.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }

	    Producto producto = optionalProducto.get();
	    producto.setNombre(nombre);
	    producto.setDescripcion(descripcion);
	    producto.setStock(stock);
	    producto.setPrecio(precio);

	    if (imagen != null && !imagen.isEmpty()) {
	        String urlImagen = azureBlobService.uploadImage(imagen);
	        producto.setImagen(urlImagen);
	    }

	    return ResponseEntity.ok(productoService.actualizarProducto(producto, id));
	}

}
