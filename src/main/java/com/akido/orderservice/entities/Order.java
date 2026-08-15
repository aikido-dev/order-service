package com.akido.orderservice.entities;

import com.akido.orderservice.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

}
