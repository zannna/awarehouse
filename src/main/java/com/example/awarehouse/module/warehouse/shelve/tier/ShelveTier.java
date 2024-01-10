package com.example.awarehouse.module.warehouse.shelve.tier;


import com.example.awarehouse.module.warehouse.shelve.Dimensions;
import com.example.awarehouse.module.warehouse.shelve.Shelve;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tier")
@NoArgsConstructor
@Getter
public class ShelveTier {
    @Id
    @Column(name ="tier_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int number;
    private String name;
    boolean size;
    @Embedded
    private Dimensions dimensions;
    @ManyToOne
    @JoinColumn(name = "shelve_id")
    private Shelve shelve;
    private double occupiedVolume =0;
    public ShelveTier(int number, String name, boolean size, Dimensions dimensions) {
        this.number = number;
        this.name = name;
        this.size = size;
        this.dimensions = dimensions;
    }

    public void setShelve(Shelve shelve) {
        this.shelve = shelve;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public void addOccupiedVolume(double volume) {
        this.occupiedVolume += volume;
    }
}
