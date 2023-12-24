package com.example.awarehouse.module.report.command;

import com.example.awarehouse.module.email.dto.EmailDto;
import com.example.awarehouse.module.email.template.EmailTemplateGenerator;
import com.example.awarehouse.module.product.dto.UnderstockedProductInGroupDto;
import com.example.awarehouse.module.product.dto.UnderstockedProductInWarehouseDto;
import com.example.awarehouse.module.report.ReportInterval;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UnderstockReportEmailCreator {

    EmailTemplateGenerator emailTemplateGenerator;
    private static final String UNDERSTOCK_IN_WAREHOUSE_SUBJECT = "Understock in warehouse";
    private static final String UNDERSTOCK_IN_GROUP_SUBJECT = "Understock in group";

    public EmailDto createEmailUnderstockInWarehouse(String receiver, List<UnderstockedProductInWarehouseDto> understockedInWarehouseDtos,
                                                     String warehouseName, ReportInterval reportInterval){
        String body = emailTemplateGenerator. generateForUnderstockedProductsInWarehouse(understockedInWarehouseDtos, warehouseName, reportInterval);
        return new EmailDto(receiver, UNDERSTOCK_IN_WAREHOUSE_SUBJECT, body);

    }

    public EmailDto createEmailUnderstockInGroup(String receiver, List<UnderstockedProductInGroupDto> understockedInGroupDtos,
                                                 String groupName, ReportInterval reportInterval){
        String body = emailTemplateGenerator. generateForUnderstockedProductsInGroup(understockedInGroupDtos, groupName, reportInterval);
        return new EmailDto(receiver, UNDERSTOCK_IN_GROUP_SUBJECT, body);

    }
}
