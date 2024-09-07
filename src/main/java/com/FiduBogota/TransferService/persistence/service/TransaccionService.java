package com.FiduBogota.TransferService.persistence.service;

import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Entity.Transaccion;
import com.FiduBogota.TransferService.Domain.Repository.TransaccionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;


    public TransaccionService(TransaccionRepository transaccionRepository){
        this.transaccionRepository = transaccionRepository;
    }
    public Transaccion create(Transaccion transaccion){
        return this.transaccionRepository.save(transaccion);
    }

}
