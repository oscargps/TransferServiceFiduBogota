package com.FiduBogota.TransferService.web.controllers;

import com.FiduBogota.TransferService.Domain.Constants.TipoTransaccion;
import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Entity.Transaccion;
import com.FiduBogota.TransferService.Domain.dto.DepositRequest;
import com.FiduBogota.TransferService.Domain.dto.WithDrawRequest;
import com.FiduBogota.TransferService.persistence.service.CuentaBancariaService;
import com.FiduBogota.TransferService.persistence.service.TransaccionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("accounts")
public class CuentaController {

    private final CuentaBancariaService cuentaBancariaService;
    private final TransaccionService transaccionService;
    @PostMapping
    public ResponseEntity<CuentaBancaria> Create(@RequestBody CuentaBancaria cuentaBancaria){
        return ResponseEntity.ok(this.cuentaBancariaService.create(cuentaBancaria));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<String> deposit(@PathVariable Long id, @RequestBody DepositRequest depositRequest) {
        Optional<CuentaBancaria> cuentaBancariaOptional = Optional.ofNullable(this.cuentaBancariaService.find(id));

        if (cuentaBancariaOptional.isEmpty()) {
            log.info("Cuenta no encontrada");
            return ResponseEntity.notFound().build();
        }

        if (depositRequest.getMonto() <= 0){
            log.info("Monto de deposito negativo");
            ResponseEntity.badRequest().build();
        }

        CuentaBancaria cuentaBancaria = cuentaBancariaOptional.get();
        cuentaBancaria.setSaldo(cuentaBancaria.getSaldo());
        this.cuentaBancariaService.create(cuentaBancaria);

        Transaccion transaccion = new Transaccion();
        transaccion.setCuentaBancaria(cuentaBancaria);
        transaccion.setTipo(TipoTransaccion.DEPOSITO);
        transaccion.setMonto(depositRequest.getMonto() + depositRequest.getMonto());
        transaccion.setFecha(LocalDateTime.now());
        this.transaccionService.create(transaccion);

        return ResponseEntity.ok("Consignación realizada con éxito. Nuevo saldo: " + cuentaBancaria.getSaldo());
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable Long id, @RequestBody WithDrawRequest withDrawRequest) {
        Optional<CuentaBancaria> cuentaBancariaOptional = Optional.ofNullable(this.cuentaBancariaService.find(id));

        if (cuentaBancariaOptional.isEmpty()) {
            log.info("Cuenta no encontrada");
            return ResponseEntity.notFound().build();
        }
        if (withDrawRequest.getMonto() <= 0){
            log.info("Monto de retiro negativo");
            ResponseEntity.badRequest().build();
        }

        CuentaBancaria cuentaBancaria = cuentaBancariaOptional.get();

        if(cuentaBancaria.getSaldo() < withDrawRequest.getMonto() ){
            log.info("saldo insuficiente para retiro");
            return ResponseEntity.internalServerError().build();
        }
        cuentaBancaria.setSaldo(cuentaBancaria.getSaldo() - withDrawRequest.getMonto());
        this.cuentaBancariaService.create(cuentaBancaria);

        Transaccion transaccion = new Transaccion();
        transaccion.setCuentaBancaria(cuentaBancaria);
        transaccion.setTipo(TipoTransaccion.RETIRO);
        transaccion.setMonto(withDrawRequest.getMonto());
        transaccion.setFecha(LocalDateTime.now());
        this.transaccionService.create(transaccion);

        return ResponseEntity.ok("Retiro realizado con éxito. Nuevo saldo: " + cuentaBancaria.getSaldo());
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Integer> balance(@PathVariable Long id){
        Optional<CuentaBancaria> cuentaBancariaOptional = Optional.ofNullable(this.cuentaBancariaService.find(id));

        return cuentaBancariaOptional.map(cuentaBancaria -> ResponseEntity.ok(cuentaBancaria.getSaldo())).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
