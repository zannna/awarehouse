package com.example.awarehouse.module.report.command;

import com.example.awarehouse.module.email.EmailSender;
import com.example.awarehouse.module.email.dto.EmailDto;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.product.Product;
import com.example.awarehouse.module.product.ProductService;
import com.example.awarehouse.module.product.dto.UnderstockedProductInGroupDto;
import com.example.awarehouse.module.product.dto.UnderstockedProductInWarehouseDto;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import com.example.awarehouse.module.report.ReportScope;
import com.example.awarehouse.module.report.dto.ReportCommand;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.util.WarehouseConstants;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WarehouseNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportCommandHandler {
    private ProductService productService;
    private UnderstockReportEmailCreator reportEmailCreator;
    private EmailSender emailSender;
    private WarehouseService warehouseService;
    private WarehouseGroupService groupService;


    public void handle(List<ReportCommand> reportCommands) {
        for (ReportCommand command : reportCommands) {
            if (command.reportScope().equals(ReportScope.WAREHOUSE)) {
                sendEmailForUnderstockedProductsInWarehouse(command);
            } else if ((command.reportScope().equals(ReportScope.GROUP))) {
                sendEmailForUnderstockedProductsInGroup(command);
            }

        }
    }

    private void sendEmailForUnderstockedProductsInWarehouse(ReportCommand command) {
        List<Product> products = productService.findProductUnderstockByWarehouseId(command.scopeEntityId());
        String warehouseName= warehouseService.getWarehouse(command.scopeEntityId()).orElseThrow(() -> new WarehouseNotExistException(WarehouseConstants.WAREHOUSE_NOT_EXIST)).getName();
        List<UnderstockedProductInWarehouseDto> productDtos = products.stream().map(ProductMapper::toUnderstockedProductInWarehouseDto).collect(Collectors.toList());
        EmailDto email = reportEmailCreator.createEmailUnderstockInWarehouse(command.email(), productDtos, warehouseName, command.reportInterval());
        emailSender.sendEmail(email);
    }

    private void sendEmailForUnderstockedProductsInGroup(ReportCommand command) {
        List<Product> products = productService.findProductUnderstockByGroupId(command.scopeEntityId());
        List<UnderstockedProductInGroupDto> groupDtos = products.stream().map(ProductMapper::toUnderstockedProductInGroupDto).collect(Collectors.toList());
        String groupName = groupService.getGroup(command.scopeEntityId()).orElseThrow(() -> new WarehouseNotExistException(WarehouseConstants.GROUP_NOT_EXIST)).getName();
        EmailDto email = reportEmailCreator.createEmailUnderstockInGroup(command.email(), groupDtos, groupName, command.reportInterval());
        emailSender.sendEmail(email);
    }

}
