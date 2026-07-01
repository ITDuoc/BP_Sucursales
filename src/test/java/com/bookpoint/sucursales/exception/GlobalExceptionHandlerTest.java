package com.bookpoint.sucursales.exception;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void testManejoErroresValidacion() throws Exception {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        Object target = new Object();

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(target, "target");

        bindingResult.addError(
                new FieldError("target", "nombre", "El nombre es obligatorio")
        );

        bindingResult.addError(
                new FieldError("target", "telefono", "El telefono es obligatorio")
        );

        Method method = Object.class.getMethod("toString");

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        Map<String, String> response =
                handler.manejoErroresValidacion(ex);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("El nombre es obligatorio", response.get("nombre"));
        assertEquals("El telefono es obligatorio", response.get("telefono"));
    }
}