package com.FiduBogota.TransferService.Domain.Repository;

import com.FiduBogota.TransferService.Domain.Entity.CuentaBancaria;
import com.FiduBogota.TransferService.Domain.Entity.Transaccion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransaccionRepository extends ListCrudRepository<Transaccion,Long> {

    @Query(value = "SELECT t FROM Transaccion t WHERE t.cuentaBancaria = :cuentaBancaria")
    List<Transaccion> findByAccount(@Param("cuentaBancaria") CuentaBancaria cuentaBancaria);
}
