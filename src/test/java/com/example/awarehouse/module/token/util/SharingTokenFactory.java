package com.example.awarehouse.module.token.util;

import com.example.awarehouse.module.token.SharingToken;

import static com.example.awarehouse.module.warehouse.util.factory.WarehouseFactory.createWarehouse;

public class SharingTokenFactory {

    public static SharingToken createSharingToken() {
        return new SharingToken("48d0c5d7-8e25-4ccd-b23f-b748137a0dc6", "ZLhnHxdpHETcxmtEStgpI", createWarehouse());
    }
}
