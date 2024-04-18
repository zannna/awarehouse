package com.example.awarehouse.module.warehouse.shelve;

import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "shelve")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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


    @OneToMany(mappedBy= "shelve", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<ShelveTier> shelveTiers;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    private int row;
    public Shelve(int number, String name, Dimensions dimensions, boolean size, Set<ShelveTier> shelveTiers,
                  Warehouse warehouse, int row) {
        this.number = number;
        this.name = name;
        this.dimensions = dimensions;
        this.size = size;
        this.shelveTiers = shelveTiers;
        this.warehouse = warehouse;
        this.row = row;
    }
    public Boolean hasFreeSpace() {
        Boolean response = false;
        for (ShelveTier tier : shelveTiers) {
            if(!tier.isSize()){
                response= null;
            }
            else if (tier.hasFreeSpace()) {
                return true;
            }
        }
        return response;
    }

    public void modifyShelve(Shelve shelve){
        this.number = shelve.getNumber();
        this.name = shelve.getName();
        this.dimensions = shelve.getDimensions();
        this.size = shelve.isSize();
        this.warehouse = shelve.getWarehouse();
        this.row = shelve.getRow();
    }
}
