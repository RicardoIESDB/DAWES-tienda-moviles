package com.dwes.proyecto.service;

import com.dwes.proyecto.model.Brand;
import com.dwes.proyecto.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    public BrandRepository brandRepository;

    // CONSULTAS

    @Transactional(readOnly = true)
    public List<Brand> findAll() {
        return brandRepository.findSqlAll();
    }

    @Transactional(readOnly = true)
    public Brand findById(Long id) {
        return brandRepository.findSqlById(id);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return brandRepository.count();
    }

    // ACTUALIZACIONES

    @Transactional
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Transactional
    public Brand update(Long id, Brand brandDetails) {
        Brand brand = brandRepository.findSqlById(id);
        if (brand == null) {
            throw new RuntimeException("Marca no encontrada");
        }

        if (brandDetails.getName() != null) {
            brand.setName(brandDetails.getName());
        }

        return brandRepository.save(brand);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new RuntimeException("Marca no encontrada");
        }
        brandRepository.deleteById(id);
    }
}