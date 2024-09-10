package com.FiduBogota.TransferService.persistence.service;

import com.FiduBogota.TransferService.Domain.Constants.TipoTransaccion;
import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Entity.Transaccion;
import com.FiduBogota.TransferService.Domain.Repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public Transaccion deposit(CuentaBancaria cuentaBancaria, Integer monto){
        final Transaccion transaccion = Transaccion.builder()
                .cuentaBancaria(cuentaBancaria)
                .tipo(TipoTransaccion.DEPOSITO)
                .monto(monto)
                .build();

        return this.transaccionRepository.save(transaccion);
    }
    public Transaccion withDraw(CuentaBancaria cuentaBancaria, Integer monto){
        final Transaccion transaccion = Transaccion.builder()
                .cuentaBancaria(cuentaBancaria)
                .tipo(TipoTransaccion.RETIRO)
                .monto(monto)
                .build();

        return this.transaccionRepository.save(transaccion);
    }

    public List<Transaccion> getTransactionsByAccount(CuentaBancaria cuentaBancaria){
        return this.transaccionRepository.findByAccount(cuentaBancaria);
    }

}
