package com.dwes.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "moviles")

public class Movil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_name")
    private String modelName;

    @Column(nullable = false)
    private Double price;

    @Column(name = "ram_gb", nullable = false)
    private Integer ramGB;

    @Column(name = "storage_gb", nullable = false)
    private Integer storageGB;

    @Column(length = 200)
    private String description;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @JsonIgnore
    private Brand brand;
}
