package com.example.awarehouse.module.email.template;

import com.example.awarehouse.module.product.dto.UnderstockedProductInWarehouseDto;
import com.example.awarehouse.module.report.ReportInterval;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.awarehouse.util.Constants.URI_VERSION_V1;
import static com.example.awarehouse.util.Constants.URI_WAREHOUSE;

@RestController
@RequestMapping(URI_VERSION_V1+URI_WAREHOUSE+"/cos")
@AllArgsConstructor
public class TempleteController {

        EmailTemplateGenerator emailTemplateGenerator;

        @GetMapping
        public String get(){
            List<UnderstockedProductInWarehouseDto> list = List.of(new UnderstockedProductInWarehouseDto(UUID.randomUUID(), "kotek", "36 zł", "zabawki"),
                    new UnderstockedProductInWarehouseDto(UUID.randomUUID(), "kotek", "190 zł", "zabawki"),
                    new UnderstockedProductInWarehouseDto(UUID.randomUUID(), "doniczka", "36 zł", "ogród"),
                    new UnderstockedProductInWarehouseDto(UUID.randomUUID(), "kapelusz fajny", "277 zł", "ubrania")
                    );
             return   emailTemplateGenerator.generateForUnderstockedProductsInWarehouse(list, "Magazyn 1", ReportInterval.WEEKLY);
            //  return emailTemplateGenerator.generateForUnderstockedProductsInWarehouse(null, "Magazyn 1", ReportType.WEEKLY);
        }
}
