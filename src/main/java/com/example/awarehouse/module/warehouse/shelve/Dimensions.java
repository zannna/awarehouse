package com.example.awarehouse.module.warehouse.shelve;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Dimensions {
    private double height=0.0;
    private double width=0.0;
    private double length=0.0;

    public double getVolume(){
        return height*width*length;
    }
}
