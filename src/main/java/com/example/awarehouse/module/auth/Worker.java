package com.example.awarehouse.module.auth;

import com.example.awarehouse.module.group.GroupWorker;
import com.example.awarehouse.module.warehouse.WorkerWarehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Worker {

    @Id
    @Column(name = "worker_id")
    private UUID id;

    String firstName;

    String lastName;

    @OneToMany(mappedBy = "worker")
    private Set<WorkerWarehouse> workerWarehouses = new HashSet<>();

    @OneToMany(mappedBy = "worker", cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<GroupWorker> groupWorkers = new HashSet<>();

    public Worker(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker that = (Worker) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
