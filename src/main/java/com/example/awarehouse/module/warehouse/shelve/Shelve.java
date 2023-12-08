package com.example.awarehouse.module.warehouse.shelve;

import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "shelve")
@Getter
@NoArgsConstructor
public class Shelve {

    @Id
    @Column(name = "shelve_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int number;

    private String name;

    @Embedded
    private Dimensions dimensions;

    boolean size;


    @Column(name = "same_size_tier")
    private boolean sameSizeTiers;

    @OneToMany(mappedBy="shelve", cascade = CascadeType.PERSIST)
    private Set<ShelveTier> shelveTiers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    public Shelve(int number, String name, Dimensions dimensions, boolean size, boolean sameSizeTiers, Set<ShelveTier> shelveTiers, Warehouse warehouse) {
        this.number = number;
        this.name = name;
        this.dimensions = dimensions;
        this.size = size;
        this.sameSizeTiers = sameSizeTiers;
        this.shelveTiers = shelveTiers;
        this.warehouse = warehouse;
    }
}
