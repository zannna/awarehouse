package com.example.awarehouse.module.token.mapper;

import com.example.awarehouse.module.token.SharingToken;
import com.example.awarehouse.module.token.dto.SharingTokenDto;

public class SharingTokenMapper {
    public static SharingTokenDto toDto(SharingToken sharingToken){
        return new SharingTokenDto(sharingToken.getSharingToken());
    }
}
