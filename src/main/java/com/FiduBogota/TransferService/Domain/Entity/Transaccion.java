package com.FiduBogota.TransferService.Domain.Entity;

import com.FiduBogota.TransferService.Domain.Constants.TipoTransaccion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_bancaria_id", nullable = false)
    private CuentaBancaria cuentaBancaria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransaccion tipo;

    @Column(nullable = false)
    private Integer monto;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public Transaccion(){
        this.fecha =  LocalDateTime.now();
    }


}
