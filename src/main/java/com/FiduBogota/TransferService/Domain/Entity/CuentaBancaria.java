package com.FiduBogota.TransferService.Domain.Entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class CuentaBancaria {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 100)
    private String titular;

    @Column(nullable = false)
    private Integer saldo;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "cuentaBancaria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaccion> transacciones;

    public CuentaBancaria() {
        this.fechaCreacion = LocalDateTime.now();
        this.saldo = 0;
    }

}
