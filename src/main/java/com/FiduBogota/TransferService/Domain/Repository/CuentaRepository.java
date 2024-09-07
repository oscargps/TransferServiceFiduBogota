package com.FiduBogota.TransferService.Domain.Repository;

import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import org.springframework.data.repository.ListCrudRepository;


public interface CuentaRepository extends ListCrudRepository<CuentaBancaria,Long> {

}
