package com.example.awarehouse.module.token;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

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

    private UUID tokenOwnerId;

    @Enumerated(EnumType.STRING)
    private  OwnerType ownerType;

    public SharingToken(String sharingToken, String salt, UUID tokenOwnerId, OwnerType ownerType) {
        this.sharingToken = sharingToken;
        this.salt = salt;
        this.tokenOwnerId = tokenOwnerId;
        this.ownerType = ownerType;
    }
}
