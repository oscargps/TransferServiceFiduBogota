package com.FiduBogota.TransferService.persistence.service;

import com.FiduBogota.TransferService.Domain.Constants.TipoTransaccion;
import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Entity.Transaccion;
import com.FiduBogota.TransferService.Domain.Repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public Transaccion deposit(CuentaBancaria cuentaBancaria, Integer monto){
        final Transaccion transaccion = new Transaccion();
        transaccion.setCuentaBancaria(cuentaBancaria);
        transaccion.setMonto(monto);
        transaccion.setTipo(TipoTransaccion.DEPOSITO);
        return this.transaccionRepository.save(transaccion);
    }
    public Transaccion withDraw(CuentaBancaria cuentaBancaria, Integer monto){
        final Transaccion transaccion = new Transaccion();
        transaccion.setCuentaBancaria(cuentaBancaria);
        transaccion.setMonto(monto);
        transaccion.setTipo(TipoTransaccion.RETIRO);
        return this.transaccionRepository.save(transaccion);
    }

}
