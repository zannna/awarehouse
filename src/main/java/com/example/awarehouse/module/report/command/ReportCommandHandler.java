package com.example.awarehouse.module.report.command;

import com.example.awarehouse.module.product.Product;
import com.example.awarehouse.module.product.ProductService;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import com.example.awarehouse.module.report.ReportRepository;
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

    public void handle(List<ReportCommand> reportCommands) {
        for (ReportCommand command: reportCommands) {
            List<Product> products = null;
          if(command.reportScope().equals(ReportScope.WAREHOUSE))
              products= productService.findProductByWarehouseId(command.scopeEntityId());
          else if((command.reportScope().equals(ReportScope.GROUP))){
              products=  productService.findProductByGroupId(command.scopeEntityId());
          }
          products.stream().map(ProductMapper::toDto).collect(Collectors.toSet());
        }
    }

}
