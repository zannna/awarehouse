package com.example.awarehouse.module.token.mapper;

import com.example.awarehouse.module.token.SharingToken;
import com.example.awarehouse.module.token.dto.SharingTokenResponse;

public class SharingTokenMapper {
    public static SharingTokenResponse toDto(SharingToken sharingToken){
        return new SharingTokenResponse(sharingToken.getSharingToken());
    }
}
