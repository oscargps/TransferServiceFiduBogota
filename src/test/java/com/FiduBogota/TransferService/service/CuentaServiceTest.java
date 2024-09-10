package com.FiduBogota.TransferService.service;

import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Repository.CuentaRepository;
import com.FiduBogota.TransferService.Domain.dto.DepositRequestDto;
import com.FiduBogota.TransferService.Domain.dto.WithDrawRequestDto;
import com.FiduBogota.TransferService.persistence.service.CuentaBancariaService;
import com.FiduBogota.TransferService.web.exceptions.AccountBlockedException;
import com.FiduBogota.TransferService.web.exceptions.AccountNotFoundException;
import com.FiduBogota.TransferService.web.exceptions.InvalidDepositAmountException;
import com.FiduBogota.TransferService.web.exceptions.NotEnoughBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
        when(cuentaBancariaRepository.save(any(CuentaBancaria.class))).thenReturn(cuentaMock);
        CuentaBancaria cuentaCreada = cuentaBancariaService.create(cuentaMock);


        assertNotNull(cuentaCreada);
        assertEquals("John Doe", cuentaCreada.getTitular());
        assertEquals(100, cuentaCreada.getSaldo());
    }
    @Test
    public void testDeposit_MontoNegativo() throws Exception {
        Long cuentaId = 1L;
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setMonto(0);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        try {
            cuentaBancariaService.deposit(depositRequestDto, cuentaMock);
        }catch (Exception e){
            assertEquals(InvalidDepositAmountException.class, e.getClass());
        }
    }
    @Test
    public void testDeposit_CuentaBlockeada() throws Exception {
        Long cuentaId = 1L;
        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setMonto(1);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);
        cuentaMock.setIsBlock(true);

        try {
            cuentaBancariaService.deposit(depositRequestDto, cuentaMock);
        }catch (Exception e){
            assertEquals(AccountBlockedException.class, e.getClass());
        }
    }

    @Test
    public void testWithDraw_MontoNegativo() throws Exception {
        Long cuentaId = 1L;
        WithDrawRequestDto withDrawRequestDto = new WithDrawRequestDto();
        withDrawRequestDto.setMonto(0);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);

        try {
            cuentaBancariaService.withDraw(withDrawRequestDto, cuentaMock);
        }catch (Exception e){
            assertEquals(InvalidDepositAmountException.class, e.getClass());
        }
    }

    @Test
    public void testWithDraw_SaldoInsuficiente() throws Exception {
        Long cuentaId = 1L;
        WithDrawRequestDto withDrawRequestDto = new WithDrawRequestDto();
        withDrawRequestDto.setMonto(100);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(99);

        try {
            cuentaBancariaService.withDraw(withDrawRequestDto, cuentaMock);
        }catch (Exception e){
            assertEquals(NotEnoughBalanceException.class, e.getClass());
        }
    }
    @Test
    public void testWithDraw_CuentaBlockeada() throws Exception {
        Long cuentaId = 1L;
        WithDrawRequestDto withDrawRequestDto = new WithDrawRequestDto();
        withDrawRequestDto.setMonto(100);

        CuentaBancaria cuentaMock = new CuentaBancaria();
        cuentaMock.setId(cuentaId);
        cuentaMock.setSaldo(100);
        cuentaMock.setIsBlock(true);

        try {
            cuentaBancariaService.withDraw(withDrawRequestDto, cuentaMock);
        }catch (Exception e){
            assertEquals(AccountBlockedException.class, e.getClass());
        }
    }

}
