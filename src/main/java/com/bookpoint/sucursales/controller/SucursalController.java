package com.bookpoint.sucursales.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookpoint.sucursales.model.Sucursal;
import com.bookpoint.sucursales.service.SucursalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/sucursales")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @PostMapping
    public ResponseEntity<?> postSucursal(@Valid @RequestBody Sucursal sucursal) {
        return ResponseEntity.ok(sucursalService.crear(sucursal));
    }

    @GetMapping
    public ResponseEntity<?> getSucursales() {

        List<Sucursal> lista = sucursalService.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.status(404)
                    .body("No hay sucursales registradas");
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getSucursalById(@PathVariable Long id) {

        Sucursal sucursal = sucursalService.buscarPorId(id);

        if (sucursal == null) {
            return ResponseEntity.status(404)
                    .body("Sucursal no encontrada");
        }

        return ResponseEntity.ok(sucursal);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> putSucursal(
            @PathVariable Long id,
            @Valid @RequestBody Sucursal sucursal) {

        return ResponseEntity.ok(
                sucursalService.modificar(id, sucursal));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteSucursal(@PathVariable Long id) {
        sucursalService.eliminar(id);
        return ResponseEntity.ok("Sucursal eliminada correctamente");
    }

    @GetMapping("{id}/usuarios")
    public ResponseEntity<?> getUsuariosPorSucursal(@PathVariable Long id) {

        try {
            Object respuesta = sucursalService.obtenerUsuariosPorSucursal(id);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }
}