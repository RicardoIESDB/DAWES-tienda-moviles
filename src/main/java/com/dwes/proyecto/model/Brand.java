package com.dwes.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//LOMBOK
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "mobiles")
@EqualsAndHashCode(exclude = "mobiles")
@Entity
@Table(name = "brands")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
// Brands = Marcas
public class Brand implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotBlank(message = "El nombre de la marca es obligatorio")
    @Size(min = 1, max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column(nullable = false, unique = true)
    private String name;
    // 'mappedBy' indica que el dueño de la relación es el atributo "brand" en la clase Movil
    // cascade = CascadeType.ALL permite que si borras la marca, se borren sus móviles (coincidiendo con tu ON DELETE CASCADE del SQL)
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("brand")
    // private List<Mobile> mobiles;
    private Set<Mobile> mobiles = new HashSet<>();
}
