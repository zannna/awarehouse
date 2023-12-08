package com.example.awarehouse.module.warehouse.shelve.tier;


import com.example.awarehouse.module.warehouse.shelve.Shelve;
import jakarta.persistence.*;

@Entity
@Table(name = "tier")
public class ShelveTier {
    @Id
    @Column(name ="tier_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    private double height=0.0;

    private String name;

    @ManyToOne
    @JoinColumn(name = "shelve_id")
    private Shelve shelve;
}
