package com.bookpoint.sucursales.service;

import com.bookpoint.sucursales.model.Sucursal;
import com.bookpoint.sucursales.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private SucursalRepository repository;

    @InjectMocks
    private SucursalService service;

    @BeforeEach
    void setup() {
        reset(repository);
    }

    @Test
    void testCrear() {
        Sucursal sucursal = new Sucursal(1L, "Centro", "111", "Dir1", "09-18");

        when(repository.save(any())).thenReturn(sucursal);

        Sucursal result = service.crear(sucursal);

        assertNotNull(result);
        assertEquals("Centro", result.getNombre());

        verify(repository).save(sucursal);
    }

    @Test
    void testListar() {
        when(repository.findAll()).thenReturn(List.of(
                new Sucursal(1L, "A", "1", "D1", "H1")
        ));

        List<Sucursal> result = service.listar();

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getNombre());

        verify(repository).findAll();
    }

    @Test
    void testBuscarPorIdExiste() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(new Sucursal(1L, "Centro", "111", "Dir1", "09-18")));

        Sucursal result = service.buscarPorId(1L);

        assertNotNull(result);
        assertEquals("Centro", result.getNombre());
    }

    @Test
    void testBuscarPorIdNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Sucursal result = service.buscarPorId(1L);

        assertNull(result);
    }

    @Test
    void testModificarExiste() {
        Sucursal existente = new Sucursal(1L, "Old", "1", "D1", "H1");
        Sucursal update = new Sucursal(null, "New", "2", "D2", "H2");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        Sucursal result = service.modificar(1L, update);

        assertEquals("New", result.getNombre());
        verify(repository).save(existente);
    }

    @Test
    void testModificarNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Sucursal result = service.modificar(1L, new Sucursal());

        assertNull(result);
    }

    @Test
    void testEliminar() {
        doNothing().when(repository).deleteById(1L);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void testObtenerUsuariosOk() {
        SucursalService spy = spy(service);

        doReturn("OK").when(spy).obtenerUsuariosPorSucursal(1L);

        Object result = spy.obtenerUsuariosPorSucursal(1L);

        assertEquals("OK", result);
    }

    @Test
    void testObtenerUsuariosError() {
        SucursalService spy = spy(service);

        doThrow(new RuntimeException("fail"))
                .when(spy).obtenerUsuariosPorSucursal(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> spy.obtenerUsuariosPorSucursal(1L));

        assertTrue(ex.getMessage().contains("fail"));
    }
}