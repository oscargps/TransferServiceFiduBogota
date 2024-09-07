package com.FiduBogota.TransferService.persistence.service;

import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CuentaBancariaService {

    private final CuentaRepository cuentaRepository;



    public CuentaBancaria create(CuentaBancaria cuentaBancaria){
        return this.cuentaRepository.save(cuentaBancaria);
    }
    public CuentaBancaria find(long id){
        return this.cuentaRepository.findById(id).orElse(null);
    }



}
