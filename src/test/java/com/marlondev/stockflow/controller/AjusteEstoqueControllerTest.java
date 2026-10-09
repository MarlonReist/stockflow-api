package com.marlondev.stockflow.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;

class AjusteEstoqueControllerTest {

    @Test
    void ajusteCriadoNaoPossuiEndpointsComunsDeEdicaoOuExclusao() {
        boolean possuiEndpointMutavel = Arrays.stream(AjusteEstoqueController.class.getDeclaredMethods())
                .anyMatch(method -> method.isAnnotationPresent(PutMapping.class)
                        || method.isAnnotationPresent(PatchMapping.class)
                        || method.isAnnotationPresent(DeleteMapping.class));

        assertFalse(possuiEndpointMutavel);
    }
}
