package com.bookpoint.sucursales.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookpoint.sucursales.model.Sucursal;
import com.bookpoint.sucursales.repository.SucursalRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String USUARIOS_URL =
            "http://localhost:8081/api/v1/usuarios";

    
    public Sucursal crear(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }

    public List<Sucursal> listar() {
        return sucursalRepository.findAll();
    }

    public Sucursal buscarPorId(Long id) {
        return sucursalRepository.findById(id).orElse(null);
    }

    public Sucursal modificar(Long id, Sucursal sucursal) {

        Sucursal existente =
                sucursalRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombre(sucursal.getNombre());
            existente.setTelefono(sucursal.getTelefono());
            existente.setDireccion(sucursal.getDireccion());
            existente.setHorario(sucursal.getHorario());

            return sucursalRepository.save(existente);
        }

        return null;
    }

    public void eliminar(Long id) {
        sucursalRepository.deleteById(id);
    }

    public Object obtenerUsuariosPorSucursal(Long sucursalId) {

        try {
            return restTemplate.getForObject(
                    USUARIOS_URL + "/sucursal/" + sucursalId,
                    Object.class
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al obtener usuarios: " + e.getMessage()
            );
        }
    }
}