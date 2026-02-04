package com.dwes.proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brands")

public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    // 'mappedBy' indica que el dueño de la relación es el atributo "brand" en la clase Movil
    // cascade = CascadeType.ALL permite que si borras la marca, se borren sus móviles (coincidiendo con tu ON DELETE CASCADE del SQL)
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<Movil> moviles;
}
