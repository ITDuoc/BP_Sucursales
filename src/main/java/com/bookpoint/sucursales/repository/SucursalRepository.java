package com.bookpoint.sucursales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookpoint.sucursales.model.Sucursal;

public interface SucursalRepository
        extends JpaRepository<Sucursal, Long> {

}