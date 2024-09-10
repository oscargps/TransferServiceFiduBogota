package com.FiduBogota.TransferService.persistence.service;

import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Repository.CuentaRepository;
import com.FiduBogota.TransferService.Domain.dto.DepositRequestDto;
import com.FiduBogota.TransferService.Domain.dto.WithDrawRequestDto;
import com.FiduBogota.TransferService.web.exceptions.AccountBlockedException;
import com.FiduBogota.TransferService.web.exceptions.InvalidDepositAmountException;
import com.FiduBogota.TransferService.web.exceptions.NotEnoughBalanceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j

@Service
@RequiredArgsConstructor
public class CuentaBancariaService {

    private final CuentaRepository cuentaRepository;

    public void deposit(DepositRequestDto depositRequestDto, CuentaBancaria cuentaBancaria) throws Exception{
            if (depositRequestDto.getMonto() <= 0){
                log.info("Monto de deposito invalido");
                throw new InvalidDepositAmountException("El monto del depósito debe ser mayor a 0.");
            }
            if(cuentaBancaria.getIsBlock()){
                throw new AccountBlockedException("Cuenta bloqueada");
            }
            cuentaBancaria.setSaldo(cuentaBancaria.getSaldo()+depositRequestDto.getMonto());
            this.cuentaRepository.save(cuentaBancaria);

    }

    public void withDraw(WithDrawRequestDto withDrawRequestDto, CuentaBancaria cuentaBancaria) throws Exception{
        if (withDrawRequestDto.getMonto() <= 0){
            log.info("Monto de deposito invalido");
            throw new InvalidDepositAmountException("El monto del depósito debe ser mayor a 0.");
        }
        if(cuentaBancaria.getIsBlock()){
            throw new AccountBlockedException("Cuenta bloqueada");
        }
        if(cuentaBancaria.getSaldo() < withDrawRequestDto.getMonto() ){
            log.info("saldo insuficiente para retiro");
            throw new NotEnoughBalanceException("Su saldo es insuficiente");
        }
        cuentaBancaria.setSaldo(cuentaBancaria.getSaldo() - withDrawRequestDto.getMonto());
        this.cuentaRepository.save(cuentaBancaria);

    }

    public CuentaBancaria create(CuentaBancaria cuentaBancaria){
        return this.cuentaRepository.save(cuentaBancaria);
    }
    public CuentaBancaria find(long id){
        return this.cuentaRepository.findById(id).orElse(null);
    }



}
