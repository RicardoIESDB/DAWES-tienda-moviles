package com.dwes.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mobiles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Mobile implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del modelo es obligatorio")
    @Size(max = 100, message = "El modelo no puede tener más de 100 caracteres")
    @Column(name = "model_name", nullable = false)
    private String modelName;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "La RAM es obligatoria")
    @Min(value = 1, message = "La RAM debe ser al menos 1 GB")
    @Column(name = "ram_gb", nullable = false)
    private Integer ramGB;

    @NotNull(message = "El almacenamiento es obligatorio")
    @Min(value = 1, message = "El almacenamiento debe ser al menos 1 GB")
    @Column(name = "storage_gb", nullable = false)
    private Integer storageGB;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    @Column(length = 200)
    private String description;

    @NotNull
    @Column(nullable = false)
    private Integer discount = 0;

    // Relación con Brand
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @JsonIgnoreProperties({"mobiles", "hibernateLazyInitializer", "handler"}) //Evita bucles infinitos al pedir la lista de marcas
    private Brand brand;
}