package com.dwes.proyecto.controller;

import com.dwes.proyecto.model.Brand;
import com.dwes.proyecto.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BrandController {
    @Autowired
    private BrandService brandService;

    //CONSULTAS

    // GET: http://localhost:8080/mobile-store/api/brands
    @GetMapping("/brands")
    public ResponseEntity<List<Brand>> showAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(brandService.findAll());
    }

    // GET: http://localhost:8080/mobile-store/api/brands/1
    @GetMapping("/brands/{id}")
    public ResponseEntity<Brand> showById(@PathVariable Long id) {
        Brand brand = brandService.findById(id);

        if (brand == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(brand);
        }
    }

    // GET: http://localhost:8080/mobile-store/api/brands/count
    @GetMapping("/brands/count")
    public ResponseEntity<Map<String, Object>> count() {
        Map<String, Object> map = new HashMap<>();
        map.put("count", brandService.count());

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(map);
    }

    // ***************************************************************************
    // ACTUALIZACIONES
    // ***************************************************************************

    // POST: http://localhost:8080/mobile-store/api/brands
    @PostMapping("/brands")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Brand brand) {
        ResponseEntity<Map<String, Object>> response;

        if (brand == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            // Validación manual al estilo de la profesora
            if (brand.getName() == null || brand.getName().trim().isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "El campo 'name' es obligatorio");
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
            } else {
                Brand newBrand = brandService.save(brand);
                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Marca creada con éxito");
                map.put("insertRealizado", newBrand);

                response = ResponseEntity.status(HttpStatus.CREATED).body(map);
            }
        }
        return response;
    }

    // PUT: http://localhost:8080/mobile-store/api/brands
    @PutMapping("/brands")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Brand brand) {
        ResponseEntity<Map<String, Object>> response;

        if (brand == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            Long id = brand.getId();
            Brand existingBrand = brandService.findById(id);

            if (existingBrand == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Marca no encontrada");
                map.put("id", id);
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {
                if (brand.getName() != null) {
                    existingBrand.setName(brand.getName());
                }

                Brand updatedBrand = brandService.save(existingBrand);
                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Marca actualizada con éxito");
                map.put("updateRealizado", updatedBrand);

                response = ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }
        return response;
    }

    // DELETE: http://localhost:8080/mobile-store/api/brands/1
    @DeleteMapping("/brands/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        ResponseEntity<Map<String, Object>> response;
        Brand existingBrand = brandService.findById(id);

        if (existingBrand == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Marca no encontrada");
            map.put("id", id);
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {
            try {
                brandService.deleteById(id);
                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Marca eliminada con éxito");
                map.put("deletedRealizado", existingBrand);
                response = ResponseEntity.status(HttpStatus.OK).body(map);
            } catch (Exception e) {
                // Captura por si hay integridad referencial
                Map<String, Object> map = new HashMap<>();
                map.put("error", "No se puede eliminar la marca porque tiene móviles asociados");
                response = ResponseEntity.status(HttpStatus.CONFLICT).body(map);
            }
        }
        return response;
    }
}