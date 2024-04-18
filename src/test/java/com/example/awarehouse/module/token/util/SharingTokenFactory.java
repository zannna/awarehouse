package com.example.awarehouse.module.token.util;

import com.example.awarehouse.module.token.OwnerType;
import com.example.awarehouse.module.token.SharingToken;

import java.util.UUID;

public class SharingTokenFactory {

    public static SharingToken createSharingTokenForWarehouse() {
        return new SharingToken("token", "ZLhnHxdpHETcxmtEStgpI", UUID.fromString("48d0c5d7-8e25-4ccd-b23f-b748137a0dc6"), OwnerType.WAREHOUSE);
    }
    public static SharingToken createSharingTokenForGroup() {
        return new SharingToken("token", "ZLhnHxdpHETcxmtEStgpI", UUID.fromString("48d0c5d7-8e25-4ccd-b23f-b748137a0dc6"), OwnerType.GROUP);
    }
}
