package com.example.awarehouse.module.group;

import com.example.awarehouse.module.auth.Worker;
import com.example.awarehouse.module.token.SharingToken;
import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.WorkerWarehouse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WarehouseGroup {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "group_id", columnDefinition = "UUID")
    private UUID id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @OneToMany(mappedBy = "group",cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<GroupWorker> groupWorker = new HashSet<>();


    public WarehouseGroup(String name, Worker worker) {
        this.name = name;
        this. worker =  worker;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
       WarehouseGroup that = (WarehouseGroup) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public boolean isAdministrator(UUID workerId) {
        Optional<Role> role = groupWorker.stream()
                .filter(groupWorker -> groupWorker.getWorker().getId().equals(workerId))
                .findFirst()
                .map(GroupWorker::getRole);
        return role.isPresent() && role.get().equals(Role.ADMIN);
    }
}
