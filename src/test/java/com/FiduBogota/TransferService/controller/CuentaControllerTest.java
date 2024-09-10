package com.FiduBogota.TransferService.controller;
import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.dto.DepositRequestDto;
import com.FiduBogota.TransferService.Domain.dto.WithDrawRequestDto;
import com.FiduBogota.TransferService.persistence.service.CuentaBancariaService;
import com.FiduBogota.TransferService.persistence.service.TransaccionService;
import com.FiduBogota.TransferService.web.controllers.CuentaController;
import com.FiduBogota.TransferService.web.exceptions.AccountNotFoundException;
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
    public void testDeposit_CuentaExiste() throws Exception {
        Long cuentaId = 1L;
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setMonto(100);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(cuentaMock);
        doNothing().when(cuentaBancariaService).deposit(eq(depositRequestDto),any(CuentaBancaria.class));

        ResponseEntity<String> response = cuentaController.deposit(cuentaId, depositRequestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Consignación realizada con éxito.", response.getBody());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(1)).deposit(eq(depositRequestDto),any(CuentaBancaria.class));
        verify(transaccionService, times(1)).deposit(any(CuentaBancaria.class),eq(100));
    }

    @Test
    public void testDeposit_CuentaNoExiste() throws Exception {
        Long cuentaId = 1L;
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setMonto(500);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(null);
        try{
            ResponseEntity<String> response = cuentaController.deposit(cuentaId, depositRequestDto);
        }catch (Exception e){
            assertEquals(AccountNotFoundException.class, e.getClass());
        }

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(0)).create(any(CuentaBancaria.class));
        verify(transaccionService, times(0)).deposit(any(CuentaBancaria.class),eq(100));
    }

    @Test
    public void testWithDraw_CuentaExiste() throws Exception {
        Long cuentaId = 1L;
        WithDrawRequestDto withDrawRequestDto = new WithDrawRequestDto();
        withDrawRequestDto.setMonto(99);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(cuentaMock);
        doNothing().when(cuentaBancariaService).withDraw(eq(withDrawRequestDto),any(CuentaBancaria.class));

        ResponseEntity<String> response = cuentaController.withdraw(cuentaId, withDrawRequestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Retiro realizado con éxito.", response.getBody());

        verify(cuentaBancariaService, times(1)).find(cuentaId);
        verify(cuentaBancariaService, times(1)).withDraw(eq(withDrawRequestDto),any(CuentaBancaria.class));
        verify(transaccionService, times(1)).withDraw(any(CuentaBancaria.class),eq(99));
    }

    @Test
    public void testWithDraw_CuentaNoExiste() throws Exception {
        Long cuentaId = 1L;
        WithDrawRequestDto withDrawRequestDto = new WithDrawRequestDto();
        withDrawRequestDto.setMonto(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(null);

        try{
            ResponseEntity<String> response = cuentaController.withdraw(cuentaId, withDrawRequestDto);
        }catch (Exception e){
            assertEquals(AccountNotFoundException.class, e.getClass());
        }

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

    @Test
    public void testToggleBlock_CuentaNoExiste(){
        Long cuentaId = 1L;

        when(cuentaBancariaService.find(cuentaId)).thenReturn(null);

        try{
            ResponseEntity<CuentaBancaria> response = cuentaController.toggleBlock(cuentaId);
        }catch (Exception e){
            assertEquals(AccountNotFoundException.class, e.getClass());
        }

        verify(cuentaBancariaService, times(1)).find(cuentaId);
    }
    @Test
    public void testToggleBlock_CuentaExiste(){
        Long cuentaId = 1L;
        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        when(cuentaBancariaService.find(cuentaId)).thenReturn(cuentaMock);
        when(cuentaBancariaService.create(any(CuentaBancaria.class))).thenReturn(cuentaMock);
        ResponseEntity<CuentaBancaria> response = cuentaController.toggleBlock(cuentaId);
        assertEquals(200, response.getStatusCodeValue());


        verify(cuentaBancariaService, times(1)).find(cuentaId);
    }
}

