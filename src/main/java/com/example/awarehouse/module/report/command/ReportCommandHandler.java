package com.example.awarehouse.module.report.command;

import com.example.awarehouse.module.email.EmailSender;
import com.example.awarehouse.module.email.dto.EmailDto;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.product.Product;
import com.example.awarehouse.module.product.ProductService;
import com.example.awarehouse.module.product.ProductWarehouse;
import com.example.awarehouse.module.product.dto.UnderstockedProductInGroupDto;
import com.example.awarehouse.module.product.dto.UnderstockedProductInWarehouseDto;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import com.example.awarehouse.module.report.ReportScope;
import com.example.awarehouse.module.report.dto.ReportCommand;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.util.WarehouseConstants;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WarehouseNotExistException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportCommandHandler {
    private final ProductService productService;
    private final UnderstockReportEmailCreator reportEmailCreator;
    private final EmailSender emailSender;
    private final WarehouseService warehouseService;
    private final WarehouseGroupService groupService;
    private final TransactionTemplate transactionTemplate;


    public void handle(List<ReportCommand> reportCommands) {
        for (ReportCommand command : reportCommands) {
            if (command.reportScope().equals(ReportScope.WAREHOUSE)) {
                sendEmailForUnderstockedProductsInWarehouse(command);
            } else if ((command.reportScope().equals(ReportScope.GROUP))) {
                transactionTemplate.execute((status) -> {
                    sendEmailForUnderstockedProductsInGroup(command);
                    return null;
                });
            }

        }
    }

    private void sendEmailForUnderstockedProductsInWarehouse(ReportCommand command) {
        List<Product> products = productService.findProductUnderstockByWarehouseId(command.scopeEntityId());
        String warehouseName = warehouseService.getWarehouse(command.scopeEntityId()).orElseThrow(() -> new WarehouseNotExistException(WarehouseConstants.WAREHOUSE_NOT_EXIST)).getName();
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
