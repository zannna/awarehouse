package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.module.auth.Worker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "worker_warehouse")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WorkerWarehouse {
    @Id
    @Column(name = "ww_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="worker_id")
    private Worker worker;

    @ManyToOne
    @JoinColumn(name= "warehouse_id")
    private  Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }
}
