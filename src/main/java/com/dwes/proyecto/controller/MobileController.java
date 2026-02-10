package com.dwes.proyecto.controller;

import com.dwes.proyecto.model.Mobile;
import com.dwes.proyecto.service.MobileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Móviles", description = "API para gestión de móviles")
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MobileController {

    @Autowired
    private MobileService mobileService;

    //CONSULTAS

    @Operation(summary = "Obtener todos los móviles", description = "Devuelve el listado de móviles")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Móviles obtenidos con éxito") })
    @GetMapping("/mobiles")
    public ResponseEntity<List<Mobile>> showAll() {
        return ResponseEntity.status(HttpStatus.OK).body(mobileService.findAll());
    }

    @Operation(summary = "Obtener móvil por ID", description = "Busca un móvil por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Móvil encontrado"),
            @ApiResponse(responseCode = "404", description = "Móvil no encontrado", content = @Content())
    })
    @GetMapping("/mobiles/{id}")
    public ResponseEntity<Mobile> showById(@PathVariable Long id) {
        Mobile mobile = mobileService.findById(id);
        if (mobile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(mobile);
    }

    // Buscar por marca (similar a showByDep)
    @Operation(summary = "Obtener móviles por Marca", description = "Devuelve los móviles asociados a una marca específica")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Operación exitosa") })
    @GetMapping("/mobiles/brand/{brandId}")
    public ResponseEntity<List<Mobile>> showByBrand(@PathVariable Long brandId) {
        return ResponseEntity.status(HttpStatus.OK).body(mobileService.findByBrandId(brandId));
    }

    @Operation(summary = "Contar móviles", description = "Devuelve el número total de móviles")
    @GetMapping("/mobiles/count")
    public ResponseEntity<Map<String, Object>> count() {
        Map<String, Object> map = new HashMap<>();
        map.put("count", mobileService.count());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(map);
    }

    // ACTUALIZACIONES

    @Operation(summary = "Crear móvil", description = "Añade un nuevo móvil al catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Móvil creado"),
            @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content())
    })
    @PostMapping("/mobiles")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Mobile mobile) {
        ResponseEntity<Map<String, Object>> response;

        if (mobile == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo no puede estar vacío");
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            // Validaciones manuales como la profesora
            String error = "";
            if (mobile.getModelName() == null || mobile.getModelName().trim().isEmpty()) {
                error += "El modelo es obligatorio. ";
            }
            if (mobile.getPrice() == null || mobile.getPrice() < 0) {
                error += "El precio debe ser positivo. ";
            }
            if (mobile.getRamGb() == null || mobile.getRamGb() <= 0) {
                error += "La RAM debe ser mayor a 0. ";
            }

            if (!error.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", error.trim());
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
            } else {
                Mobile newMobile = mobileService.save(mobile);
                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Móvil creado con éxito");
                map.put("insertRealizado", newMobile);
                response = ResponseEntity.status(HttpStatus.CREATED).body(map);
            }
        }
        return response;
    }

    @Operation(summary = "Actualizar móvil", description = "Actualiza los datos de un móvil existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Móvil actualizado"),
            @ApiResponse(responseCode = "404", description = "Móvil no encontrado", content = @Content())
    })
    @PutMapping("/mobiles")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Mobile mobile) {
        ResponseEntity<Map<String, Object>> response;

        if (mobile == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo no puede estar vacío");
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            Long id = mobile.getId();
            Mobile existingObj = mobileService.findById(id);

            if (existingObj == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Móvil no encontrado");
                map.put("id", id);
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {
                if (mobile.getModelName() != null) existingObj.setModelName(mobile.getModelName());
                if (mobile.getPrice() != null) existingObj.setPrice(mobile.getPrice());
                if (mobile.getRamGb() != null) existingObj.setRamGb(mobile.getRamGb());
                if (mobile.getStorageGb() != null) existingObj.setStorageGb(mobile.getStorageGb());
                if (mobile.getDescription() != null) existingObj.setDescription(mobile.getDescription());
                if (mobile.getDiscount() != null) existingObj.setDiscount(mobile.getDiscount());

                Mobile updated = mobileService.save(existingObj);
                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Móvil actualizado con éxito");
                map.put("updateRealizado", updated);
                response = ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }
        return response;
    }

    @Operation(summary = "Eliminar móvil", description = "Borra un móvil del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Móvil eliminado"),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content())
    })
    @DeleteMapping("/mobiles/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        ResponseEntity<Map<String, Object>> response;
        Mobile existingObj = mobileService.findById(id);

        if (existingObj == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Móvil no encontrado");
            map.put("id", id);
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {
            mobileService.deleteById(id);
            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Móvil eliminado con éxito");
            map.put("deletedRealizado", existingObj);
            response = ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return response;
    }

    // OPERACIONES ESPECIALES

    // Asignar Marca (Equivalente a asignarDepartamento)
    // URL: http://localhost:8080/mobile-store/api/mobiles/1/asignar/brand/2
    @Operation(summary = "Asignar marca", description = "Asocia una marca a un móvil existente")
    @PutMapping("/mobiles/{mobileId}/asignar/brand/{brandId}")
    public ResponseEntity<Map<String, Object>> asignarMarca(
            @PathVariable Long mobileId,
            @PathVariable Long brandId) {

        Mobile mobile = mobileService.asignarMarca(mobileId, brandId);

        if (mobile != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Marca asignada con éxito");
            map.put("updateRealizado", mobile);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Móvil o Marca no existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
    }

    // Desasignar Marca
    @Operation(summary = "Desasignar marca", description = "Elimina la asociación de marca de un móvil")
    @PutMapping("/mobiles/{mobileId}/desasignar/brand")
    public ResponseEntity<Map<String, Object>> desasignarMarca(@PathVariable Long mobileId) {
        Mobile mobile = mobileService.desasignarMarca(mobileId);

        if (mobile != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Marca desasignada con éxito");
            map.put("updateRealizado", mobile);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Móvil no existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
    }
}