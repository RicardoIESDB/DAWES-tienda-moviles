package com.dwes.proyecto.service;

import com.dwes.proyecto.model.Brand;
import com.dwes.proyecto.model.Mobile;
import com.dwes.proyecto.repository.BrandRepository;
import com.dwes.proyecto.repository.MobileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MobileService {
    @Autowired
    public MobileRepository mobileRepository;

    @Autowired
    public BrandRepository brandRepository;

    // CONSULTAS

    @Transactional(readOnly = true)
    public List<Mobile> findAll(){
        return mobileRepository.findSqlAll();
    }

    @Transactional(readOnly = true)
    public Mobile findById(Long id) {
        return mobileRepository.findSqlById(id);
    }

    @Transactional(readOnly = true)
    public List<Mobile> findByBrandId(Long brandId) {
        return mobileRepository.findSqlByBrandId(brandId);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return mobileRepository.count();
    }

    // ************************
    // ACTUALIZACIONES
    // ************************

    @Transactional
    public Mobile save(Mobile mobile) {
        return mobileRepository.save(mobile);
    }

    @Transactional
    public Mobile update(Long id, Mobile mobileDetails) {
        Mobile mobile = mobileRepository.findSqlById(id);
        if (mobile == null) {
            throw new RuntimeException("Móvil no encontrado");
        }

        // Actualizamos campo a campo si no es null
        if (mobileDetails.getModelName() != null) {
            mobile.setModelName(mobileDetails.getModelName());
        }
        if (mobileDetails.getPrice() != null) {
            mobile.setPrice(mobileDetails.getPrice());
        }
        if (mobileDetails.getRamGb() != null) {
            mobile.setRamGb(mobileDetails.getRamGb());
        }
        if (mobileDetails.getStorageGb() != null) {
            mobile.setStorageGb(mobileDetails.getStorageGb());
        }
        if (mobileDetails.getDescription() != null) {
            mobile.setDescription(mobileDetails.getDescription());
        }
        if (mobileDetails.getDiscount() != null) {
            mobile.setDiscount(mobileDetails.getDiscount());
        }

        return mobileRepository.save(mobile);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!mobileRepository.existsById(id)) {
            throw new RuntimeException("Móvil no encontrado");
        }
        mobileRepository.deleteById(id);
    }

    // Métodos específicos de relación (Igual que asignarDepartamento)

    @Transactional
    public Mobile asignarMarca(Long mobileId, Long brandId) {
        Mobile mobile = mobileRepository.findSqlById(mobileId);
        Brand brand = brandRepository.findSqlById(brandId);

        if (mobile != null && brand != null) {
            mobile.setBrand(brand);
            return mobileRepository.save(mobile);
        } else {
            return null;
        }
    }

    @Transactional
    public Mobile desasignarMarca(Long mobileId) {
        Mobile mobile = mobileRepository.findSqlById(mobileId);

        if (mobile != null) {
            mobile.setBrand(null);
            return mobileRepository.save(mobile);
        } else {
            return null;
        }
    }
}
