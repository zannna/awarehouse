package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.module.auth.Worker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "worker_warehouse")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WorkerWarehouse {
    @Id
    @Column(name = "ww_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="worker_id")
    private Worker worker;

    @ManyToOne
    @JoinColumn(name= "warehouse_id")
    private  Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void setRole(Role role) {
        this.role = role;
    }
}
