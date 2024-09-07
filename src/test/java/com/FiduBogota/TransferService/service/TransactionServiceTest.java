package com.FiduBogota.TransferService.service;

import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Entity.Transaccion;
import com.FiduBogota.TransferService.Domain.Repository.TransaccionRepository;
import com.FiduBogota.TransferService.persistence.service.TransaccionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private TransaccionService transaccionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransaction_deposit() {
        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(1L);
        cuentaMock.setTitular("John Doe");
        cuentaMock.setSaldo(100);
        cuentaMock.setFechaCreacion(LocalDateTime.now());
        Transaccion transaccionMock = new Transaccion();
        transaccionMock.setCuentaBancaria(cuentaMock);
        transaccionMock.setMonto(100);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccionMock);
        Transaccion transaccion = transaccionService.deposit(cuentaMock, 100);

        assertEquals(cuentaMock, transaccion.getCuentaBancaria());
        assertEquals(100, transaccion.getMonto());
    }

    @Test
    public void testCreateTransaction_withdraw() {
        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(1L);
        cuentaMock.setTitular("John Doe");
        cuentaMock.setSaldo(100);
        cuentaMock.setFechaCreacion(LocalDateTime.now());
        Transaccion transaccionMock = new Transaccion();
        transaccionMock.setCuentaBancaria(cuentaMock);
        transaccionMock.setMonto(100);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccionMock);
        Transaccion transaccion = transaccionService.withDraw(cuentaMock, 100);


        assertEquals(cuentaMock, transaccion.getCuentaBancaria());
        assertEquals(100, transaccion.getMonto());
    }

}
