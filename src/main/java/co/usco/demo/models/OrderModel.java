package co.usco.demo.models;

import java.time.LocalDateTime;
import co.usco.demo.models.constants.OrderStatus;
import co.usco.demo.models.constants.OrderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private UserModel patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private UserModel doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorized_by")
    private UserModel authorizedBy;

    @Column(name = "order_creation_date", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "order_authorized_date")
    private LocalDateTime authorizedAt;
    
}

