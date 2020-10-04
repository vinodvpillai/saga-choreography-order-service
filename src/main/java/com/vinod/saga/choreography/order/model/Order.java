package com.vinod.saga.choreography.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
@Entity
@Table(name="order",schema = "saga-order")
public class Order implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime orderDate;
    @Column
    private BigDecimal totalAmount;
    @Column
    private Long userId;
    @Column
    private boolean status;
}
