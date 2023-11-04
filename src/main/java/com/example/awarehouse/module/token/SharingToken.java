package com.example.awarehouse.module.token;

import com.example.awarehouse.module.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class SharingToken {
    @Id
    @Column(name = "sharing_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sharingToken;

    private String salt;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id", referencedColumnName = "warehouse_id")
    Warehouse warehouse;

    public SharingToken(String sharingToken, String salt, Warehouse warehouse) {
        this.sharingToken = sharingToken;
        this.salt = salt;
        this.warehouse = warehouse;
    }


}
