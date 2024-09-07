package com.FiduBogota.TransferService.service;

import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Repository.CuentaRepository;
import com.FiduBogota.TransferService.persistence.service.CuentaBancariaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaBancariaRepository;

    @InjectMocks
    private CuentaBancariaService cuentaBancariaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearCuenta() {
        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(1L);
        cuentaMock.setTitular("John Doe");
        cuentaMock.setSaldo(100);
        cuentaMock.setFechaCreacion(LocalDateTime.now());

        CuentaBancaria cuentaCreada = cuentaBancariaService.create(cuentaMock);
        when(cuentaBancariaRepository.save(any(CuentaBancaria.class))).thenReturn(cuentaMock);

        assertNotNull(cuentaCreada);
        assertEquals("John Doe", cuentaCreada.getTitular());
        assertEquals(100, cuentaCreada.getSaldo());
    }


}
