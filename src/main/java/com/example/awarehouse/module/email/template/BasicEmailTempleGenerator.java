package com.example.awarehouse.module.email.template;

import com.example.awarehouse.module.product.dto.UnderstockedProductInGroupDto;
import com.example.awarehouse.module.product.dto.UnderstockedProductInWarehouseDto;
import com.example.awarehouse.module.report.ReportInterval;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@Component
public class BasicEmailTempleGenerator implements EmailTemplateGenerator{

    private final TemplateEngine templateEngine;

    @Override
    public String generateForUnderstockedProductsInWarehouse(List<UnderstockedProductInWarehouseDto> understockedProductInWarehouseDtos,
                                                             String warehouseName, ReportInterval reportInterval) {
        Context context = new Context(new Locale("en"));
        context.setVariable("products", understockedProductInWarehouseDtos);
        context.setVariable("warehouseName", warehouseName);
        context.setVariable("reportInterval", reportInterval.name().toLowerCase());
        return templateEngine.process("understockInWarehouseTemplate", context);
    }

    @Override
    public String generateForUnderstockedProductsInGroup(List<UnderstockedProductInGroupDto> understockedInGroupDtos,
                                                         String groupName, ReportInterval reportInterval) {
        Context context = new Context(new Locale("en"));
        context.setVariable("products", understockedInGroupDtos);
        context.setVariable("groupName", groupName);
        context.setVariable("reportInterval", reportInterval);
        return templateEngine.process("understockInGroupTemplate", context);
    }
}
