package com.bookpoint.sucursales.controller;

import com.bookpoint.sucursales.model.Sucursal;
import com.bookpoint.sucursales.repository.SucursalRepository;
import com.bookpoint.sucursales.service.SucursalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SucursalControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SucursalRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SucursalService sucursalService;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        reset(sucursalService);
    }

    @Test
    void postSucursal_ok() throws Exception {

        Sucursal input = new Sucursal(null, "Centro", "111", "Dir1", "09-18");
        Sucursal output = new Sucursal(1L, "Centro", "111", "Dir1", "09-18");

        when(sucursalService.crear(any())).thenReturn(output);

        mockMvc.perform(post("/api/v1/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAll_ok() throws Exception {

        when(sucursalService.listar()).thenReturn(
                java.util.List.of(
                        new Sucursal(1L, "A", "1", "D1", "H1")
                )
        );

        mockMvc.perform(get("/api/v1/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("A"));
    }

    @Test
    void getAll_empty_404() throws Exception {

        when(sucursalService.listar()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/v1/sucursales"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_ok() throws Exception {

        when(sucursalService.buscarPorId(1L))
                .thenReturn(new Sucursal(1L, "Centro", "111", "Dir1", "09-18"));

        mockMvc.perform(get("/api/v1/sucursales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Centro"));
    }

    @Test
    void getById_notFound() throws Exception {

        when(sucursalService.buscarPorId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/sucursales/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void putSucursal_ok() throws Exception {

        Sucursal updated = new Sucursal(1L, "Nuevo", "222", "Dir2", "10-20");

        when(sucursalService.modificar(eq(1L), any()))
                .thenReturn(updated);

        mockMvc.perform(put("/api/v1/sucursales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nuevo"));
    }

    @Test
    void deleteSucursal_ok() throws Exception {

        doNothing().when(sucursalService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/sucursales/1"))
                .andExpect(status().isOk());
    }

    @Test
    void usuarios_ok() throws Exception {

        when(sucursalService.obtenerUsuariosPorSucursal(1L))
                .thenReturn(java.util.List.of("user1", "user2"));

        mockMvc.perform(get("/api/v1/sucursales/1/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    void usuarios_error() throws Exception {

        when(sucursalService.obtenerUsuariosPorSucursal(1L))
                .thenThrow(new RuntimeException("fallo"));

        mockMvc.perform(get("/api/v1/sucursales/1/usuarios"))
                .andExpect(status().isBadRequest());
    }
}