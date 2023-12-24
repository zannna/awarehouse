package com.example.awarehouse.module.email.template;

import com.example.awarehouse.module.product.dto.UnderstockedProductInGroupDto;
import com.example.awarehouse.module.product.dto.UnderstockedProductInWarehouseDto;
import com.example.awarehouse.module.report.ReportInterval;

import java.util.List;

public interface EmailTemplateGenerator {
    String  generateForUnderstockedProductsInWarehouse(List<UnderstockedProductInWarehouseDto> understockedProductInWarehouseDto,
                                                       String warehouseName, ReportInterval reportInterval);

    String generateForUnderstockedProductsInGroup(List<UnderstockedProductInGroupDto> understockedInGroupDtos,
                                                  String warehouseName, ReportInterval reportInterval);
}

