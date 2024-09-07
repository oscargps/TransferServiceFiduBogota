package com.FiduBogota.TransferService.Domain.Repository;

import com.FiduBogota.TransferService.Domain.Entity.Transaccion;
import org.springframework.data.repository.ListCrudRepository;

public interface TransaccionRepository extends ListCrudRepository<Transaccion,Long> {
}
