package com.example.awarehouse.module.group;

import com.example.awarehouse.module.auth.Worker;
import com.example.awarehouse.module.warehouse.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GroupWorker {
    @Id
    @Column(name = "gw_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="worker_id")
    private Worker worker;

    @ManyToOne
    @JoinColumn(name= "group_id")
    private  WarehouseGroup group;

    @Enumerated(EnumType.STRING)
    private Role role;

    public GroupWorker(Worker worker, WarehouseGroup group, Role role) {
        this.worker = worker;
        this.group = group;
        this.role = role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
