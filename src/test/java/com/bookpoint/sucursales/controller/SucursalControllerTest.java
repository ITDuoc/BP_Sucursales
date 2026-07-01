package com.bookpoint.sucursales.controller;

import com.bookpoint.sucursales.model.Sucursal;
import com.bookpoint.sucursales.service.SucursalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SucursalController.class)
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SucursalService sucursalService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCrear() throws Exception {

        Sucursal sucursal = new Sucursal(null, "Centro", "111", "Dir1", "09-18");
        Sucursal guardada = new Sucursal(1L, "Centro", "111", "Dir1", "09-18");

        Mockito.when(sucursalService.crear(any())).thenReturn(guardada);

        mockMvc.perform(post("/api/v1/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sucursal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testListarOk() throws Exception {

        Mockito.when(sucursalService.listar())
                .thenReturn(List.of(new Sucursal(1L, "Centro", "111", "Dir1", "09-18")));

        mockMvc.perform(get("/api/v1/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Centro"));
    }

    @Test
    void testListarVacio() throws Exception {

        Mockito.when(sucursalService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/sucursales"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBuscarOk() throws Exception {

        Mockito.when(sucursalService.buscarPorId(1L))
                .thenReturn(new Sucursal(1L, "Centro", "111", "Dir1", "09-18"));

        mockMvc.perform(get("/api/v1/sucursales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Centro"));
    }

    @Test
    void testBuscarNoExiste() throws Exception {

        Mockito.when(sucursalService.buscarPorId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/sucursales/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizar() throws Exception {

        Sucursal actualizado = new Sucursal(1L, "Norte", "222", "Dir2", "10-20");

        Mockito.when(sucursalService.modificar(any(), any()))
                .thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/sucursales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Norte"));
    }

    @Test
    void testEliminar() throws Exception {

        mockMvc.perform(delete("/api/v1/sucursales/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUsuariosOk() throws Exception {

        Mockito.when(sucursalService.obtenerUsuariosPorSucursal(1L))
                .thenReturn(List.of("u1", "u2"));

        mockMvc.perform(get("/api/v1/sucursales/1/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    void testUsuariosError() throws Exception {

        Mockito.when(sucursalService.obtenerUsuariosPorSucursal(1L))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(get("/api/v1/sucursales/1/usuarios"))
                .andExpect(status().isBadRequest());
    }
}