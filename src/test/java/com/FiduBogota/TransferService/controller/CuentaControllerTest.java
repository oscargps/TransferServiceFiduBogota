package com.FiduBogota.TransferService.controller;
import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Entity.Transaccion;
import com.FiduBogota.TransferService.Domain.dto.DepositRequest;
import com.FiduBogota.TransferService.persistence.service.CuentaBancariaService;
import com.FiduBogota.TransferService.persistence.service.TransaccionService;
import com.FiduBogota.TransferService.web.controllers.CuentaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class CuentaControllerTest {

    @Mock
    private CuentaBancariaService cuentaBancariaService;

    @Mock
    private TransaccionService transaccionService;

    @InjectMocks
    private CuentaController cuentaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeposit_CuentaExiste() {
        Long cuentaId = 1L;
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setMonto(100);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(cuentaMock);
        when(cuentaBancariaService.create(any(CuentaBancaria.class))).thenReturn(cuentaMock);

        ResponseEntity<String> response = cuentaController.deposit(cuentaId, depositRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Consignación realizada con éxito. Nuevo saldo: 100", response.getBody());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(1)).create(any(CuentaBancaria.class));
        verify(transaccionService, times(1)).create(any(Transaccion.class));
    }

    @Test
    public void testDeposit_CuentaNoExiste() {
        Long cuentaId = 1L;
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setMonto(500);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(null);

        ResponseEntity<String> response = cuentaController.deposit(cuentaId, depositRequest);

        assertEquals(404, response.getStatusCodeValue());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(0)).create(any(CuentaBancaria.class));
        verify(transaccionService, times(0)).create(any(Transaccion.class));
    }

    @Test
    public void testDeposit_MontoNegativo() {
        Long cuentaId = 1L;
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setMonto(0);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(cuentaMock);

        ResponseEntity<String> response = cuentaController.deposit(cuentaId, depositRequest);

        assertEquals(400, response.getStatusCodeValue());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(0)).create(any(CuentaBancaria.class));
        verify(transaccionService, times(0)).create(any(Transaccion.class));
    }
}
