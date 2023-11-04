package com.example.awarehouse.module.warehouse.shelve;

import com.example.awarehouse.module.warehouse.shelve.tier.Tier;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "shelve")
public class Shelve {

    @Id
    @Column(name = "shelve_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    private String name;

    private double height=0.0;

    private double width=0.0;

    private double depth=0.0;

    @Column(name = "number_of_tiers")
    private int numberOfTiers;

    @Column(name = "same_size_tier")
    private boolean sameSizeTiers;

    @OneToMany(mappedBy="shelve")
    private Set<Tier> tiers= new HashSet<>();

}
