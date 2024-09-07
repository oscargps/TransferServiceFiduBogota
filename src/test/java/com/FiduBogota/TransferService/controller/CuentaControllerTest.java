package com.FiduBogota.TransferService.controller;
import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.dto.DepositRequestDto;
import com.FiduBogota.TransferService.Domain.dto.WithDrawRequestDto;
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
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setMonto(100);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(cuentaMock);
        when(cuentaBancariaService.create(any(CuentaBancaria.class))).thenReturn(cuentaMock);

        ResponseEntity<String> response = cuentaController.deposit(cuentaId, depositRequestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Consignación realizada con éxito. Nuevo saldo: 100", response.getBody());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(1)).create(any(CuentaBancaria.class));
        verify(transaccionService, times(1)).deposit(any(CuentaBancaria.class),eq(100));
    }

    @Test
    public void testDeposit_CuentaNoExiste() {
        Long cuentaId = 1L;
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setMonto(500);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(null);

        ResponseEntity<String> response = cuentaController.deposit(cuentaId, depositRequestDto);

        assertEquals(404, response.getStatusCodeValue());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(0)).create(any(CuentaBancaria.class));
        verify(transaccionService, times(0)).deposit(any(CuentaBancaria.class),eq(100));
    }

    @Test
    public void testDeposit_MontoNegativo() {
        Long cuentaId = 1L;
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setMonto(0);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(cuentaMock);

        ResponseEntity<String> response = cuentaController.deposit(cuentaId, depositRequestDto);

        assertEquals(400, response.getStatusCodeValue());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(0)).create(any(CuentaBancaria.class));
        verify(transaccionService, times(0)).deposit(any(CuentaBancaria.class),eq(100));
    }

    @Test
    public void testWithDraw_CuentaExiste() {
        Long cuentaId = 1L;
        WithDrawRequestDto withDrawRequestDto = new WithDrawRequestDto();
        withDrawRequestDto.setMonto(99);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(cuentaMock);
        when(cuentaBancariaService.create(any(CuentaBancaria.class))).thenReturn(cuentaMock);

        ResponseEntity<String> response = cuentaController.withdraw(cuentaId, withDrawRequestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Retiro realizado con éxito. Nuevo saldo: 1", response.getBody());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(1)).create(any(CuentaBancaria.class));
        verify(transaccionService, times(1)).withDraw(any(CuentaBancaria.class),eq(99));
    }

    @Test
    public void testWithDraw_CuentaNoExiste() {
        Long cuentaId = 1L;
        WithDrawRequestDto withDrawRequestDto = new WithDrawRequestDto();
        withDrawRequestDto.setMonto(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(null);

        ResponseEntity<String> response = cuentaController.withdraw(cuentaId, withDrawRequestDto);

        assertEquals(404, response.getStatusCodeValue());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(0)).create(any(CuentaBancaria.class));
        verify(transaccionService, times(0)).withDraw(any(CuentaBancaria.class),eq(100));
    }

    @Test
    public void testWithDraw_MontoNegativo() {
        Long cuentaId = 1L;
        WithDrawRequestDto withDrawRequestDto = new WithDrawRequestDto();
        withDrawRequestDto.setMonto(0);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(cuentaMock);

        ResponseEntity<String> response = cuentaController.withdraw(cuentaId, withDrawRequestDto);

        assertEquals(400, response.getStatusCodeValue());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(0)).create(any(CuentaBancaria.class));
        verify(transaccionService, times(0)).withDraw(any(CuentaBancaria.class),eq(100));
    }

    @Test
    public void testGetBalance_CuentaExiste(){
        Long cuentaId = 1L;

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(cuentaMock);
        ResponseEntity<Integer> response = cuentaController.balance(cuentaId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100, response.getBody());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
    }

    @Test
    public void testGetBalance_CuentaNoExiste(){
        Long cuentaId = 1L;

        when(cuentaBancariaService.find(cuentaId)).thenReturn(null);
        ResponseEntity<Integer> response = cuentaController.balance(cuentaId);

        assertEquals(404, response.getStatusCodeValue());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
    }
}

