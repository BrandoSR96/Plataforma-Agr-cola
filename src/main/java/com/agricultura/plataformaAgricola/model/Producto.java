package com.agricultura.plataformaAgricola.model;

import java.time.LocalDateTime;

import com.agricultura.plataformaAgricola.security.EstadoProducto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "producto")
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    private Integer stock;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private String imagen;

    @Column(nullable = false)
    private Boolean disponible = true;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private EstadoProducto estado = EstadoProducto.PENDIENTE;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}