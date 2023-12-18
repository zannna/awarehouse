package com.example.awarehouse.module.report.command;

import com.example.awarehouse.module.product.Product;
import com.example.awarehouse.module.product.ProductService;
import com.example.awarehouse.module.product.dto.ProductWithGroupDto;
import com.example.awarehouse.module.product.dto.ProductWithWarehouseDto;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import com.example.awarehouse.module.report.ReportScope;
import com.example.awarehouse.module.report.dto.ReportCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportCommandHandler {
    ProductService productService;
    UnderstockReportEmailCreator reportEmailCreator;

    public void handle(List<ReportCommand> reportCommands) {
        for (ReportCommand command: reportCommands) {
            if (command.reportScope().equals(ReportScope.WAREHOUSE)) {

            }
            else if((command.reportScope().equals(ReportScope.GROUP))){
                products=  productService.findProductUnderstockByGroupId(command.scopeEntityId());
                List<ProductWithGroupDto> groupDtos =  products.stream().map(ProductMapper::toProductWithGroupDto).collect(Collectors.toList());
            }

        }
    }
    private void sendEmailForUnderstockInWarehouse(){
        List<Product> products = productService.findProductUnderstockByWarehouseId(command.scopeEntityId());
        List<ProductWithWarehouseDto> productDtos = products.stream().map(ProductMapper::toProductWithWarehouseDto).collect(Collectors.toList());
        reportEmailCreator.createEmail()
    }

}
