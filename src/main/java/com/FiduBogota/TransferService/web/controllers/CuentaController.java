package com.FiduBogota.TransferService.web.controllers;

import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Entity.Transaccion;
import com.FiduBogota.TransferService.Domain.dto.DepositRequestDto;
import com.FiduBogota.TransferService.Domain.dto.WithDrawRequestDto;
import com.FiduBogota.TransferService.persistence.service.CuentaBancariaService;
import com.FiduBogota.TransferService.persistence.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("accounts")
public class CuentaController {

    private final CuentaBancariaService cuentaBancariaService;
    private final TransaccionService transaccionService;
    private final String NOT_FOUND_ACCOUNT_MSG = "Cuenta no encontrada";

    @PostMapping
    public ResponseEntity<CuentaBancaria> Create(@RequestBody @Valid CuentaBancaria cuentaBancaria){
        return ResponseEntity.ok(this.cuentaBancariaService.create(cuentaBancaria));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<String> deposit(@PathVariable Long id, @RequestBody DepositRequestDto depositRequestDto) {
        Optional<CuentaBancaria> cuentaBancariaOptional = Optional.ofNullable(this.cuentaBancariaService.find(id));
        if (cuentaBancariaOptional.isEmpty()) {
            log.info(NOT_FOUND_ACCOUNT_MSG);
            return ResponseEntity.notFound().build();
        }
        if (depositRequestDto.getMonto() <= 0){
            log.info("Monto de deposito negativo");
            return ResponseEntity.badRequest().build();
        }
        CuentaBancaria cuentaBancaria = cuentaBancariaOptional.get();
        if (cuentaBancaria.getIsBlock()){
            return  ResponseEntity.internalServerError().body("Cuenta bloqueada");
        }
        cuentaBancaria.setSaldo(cuentaBancaria.getSaldo() + depositRequestDto.getMonto());
        this.cuentaBancariaService.create(cuentaBancaria);
        this.transaccionService.deposit(cuentaBancaria, depositRequestDto.getMonto());
        return ResponseEntity.ok("Consignación realizada con éxito. Nuevo saldo: " + cuentaBancaria.getSaldo());
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable Long id, @RequestBody WithDrawRequestDto withDrawRequestDto) {
        Optional<CuentaBancaria> cuentaBancariaOptional = Optional.ofNullable(this.cuentaBancariaService.find(id));
        if (cuentaBancariaOptional.isEmpty()) {
            log.info(NOT_FOUND_ACCOUNT_MSG);
            return ResponseEntity.notFound().build();
        }
        if (withDrawRequestDto.getMonto() <= 0){
            log.info("Monto de retiro negativo");
            return ResponseEntity.badRequest().build();
        }
        CuentaBancaria cuentaBancaria = cuentaBancariaOptional.get();
        if(cuentaBancaria.getSaldo() < withDrawRequestDto.getMonto() ){
            log.info("saldo insuficiente para retiro");
            return ResponseEntity.internalServerError().build();
        }
        if (cuentaBancaria.getIsBlock()){
            return  ResponseEntity.internalServerError().body("Cuenta bloqueada");
        }
        cuentaBancaria.setSaldo(cuentaBancaria.getSaldo() - withDrawRequestDto.getMonto());
        this.cuentaBancariaService.create(cuentaBancaria);
        this.transaccionService.withDraw(cuentaBancaria, withDrawRequestDto.getMonto());
        return ResponseEntity.ok("Retiro realizado con éxito. Nuevo saldo: " + cuentaBancaria.getSaldo());
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Integer> balance(@PathVariable Long id){
        Optional<CuentaBancaria> cuentaBancariaOptional = Optional.ofNullable(this.cuentaBancariaService.find(id));

        return cuentaBancariaOptional.map(cuentaBancaria -> ResponseEntity.ok(cuentaBancaria.getSaldo())).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/{id}/transactions")
    public  ResponseEntity<List<Transaccion>> transactions(@PathVariable Long id){
        Optional<CuentaBancaria> cuentaBancariaOptional = Optional.ofNullable(this.cuentaBancariaService.find(id));
        return cuentaBancariaOptional.map(cuentaBancaria -> ResponseEntity.ok(this.transaccionService.getTransactionsByAccount(cuentaBancaria))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/toggle-block")
    public ResponseEntity<CuentaBancaria> toggleBlock(@PathVariable Long id){
        Optional<CuentaBancaria> cuentaBancariaOptional = Optional.ofNullable(this.cuentaBancariaService.find(id));
        if(cuentaBancariaOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        CuentaBancaria cuentaBancaria = cuentaBancariaOptional.get();
        cuentaBancaria.setIsBlock(!cuentaBancaria.getIsBlock());
        return ResponseEntity.ok(this.cuentaBancariaService.create(cuentaBancaria));
    }
}
