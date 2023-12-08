package com.example.awarehouse.module.warehouse.shelve;

import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shelve")
public class Shelve {

    @Id
    @Column(name = "shelve_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    private String name;

    @Embedded
    private Dimensions dimensions;

    @Column(name = "number_of_tiers")
    private int numberOfTiers;

    @Column(name = "same_size_tier")
    private boolean sameSizeTiers;

    @OneToMany(mappedBy="shelve")
    private Set<ShelveTier> shelveTiers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

}
