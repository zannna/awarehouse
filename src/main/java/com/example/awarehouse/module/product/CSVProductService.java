package com.example.awarehouse.module.product;

import com.example.awarehouse.module.group.GroupWorkerService;
import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.product.mapper.ProductMapper;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.shelve.ShelveService;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class CSVProductService {
    private final ProductRepository productRepository;
    private final GroupWorkerService groupWorkerService;
    private  final WorkerWarehouseService workerWarehouseService;
    private  final ShelveService shelveService;
    public void processCSVRecords(UUID warehouseId, MultipartFile file, AtomicInteger savedRecords, AtomicInteger allRecords,
                                  AtomicInteger duplicatedIds) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            for (CSVRecord nextRecord : csvParser) {
                parseCSVRecords(warehouseId, nextRecord, savedRecords, allRecords, duplicatedIds);
            }
        }
    }

    private void parseCSVRecords(UUID warehouseId, CSVRecord nextRecord, AtomicInteger savedRecords, AtomicInteger allRecords, AtomicInteger duplicatedIds) {
        String id = nextRecord.get("id");
        String name = nextRecord.get("name");
        String amount = nextRecord.get("amount");
        String price = nextRecord.get("price");
        String width = nextRecord.get("width");
        String height = nextRecord.get("height");
        String length = nextRecord.get("length");
        String group = nextRecord.get("group");
        UUID productId=null;
        if(!id.isEmpty()) {
            productId = UUID.fromString(id);
        }
        Product product = ProductMapper.toDto(productId, name, amount, price, width, height, length);
        Optional<WarehouseGroup> groupEntity = groupWorkerService.findByGroupName(group);
        product.setGroup(groupEntity.orElse(null));
        Warehouse warehouseEntity = workerWarehouseService.getWarehouse(warehouseId);
        product.setProductWarehouses(Set.of(new ProductWarehouse(product, warehouseEntity, Double.parseDouble(amount))));
        saveDataFromCSVFile(product, savedRecords, allRecords, duplicatedIds);
    }

    private void saveDataFromCSVFile(
            Product product
            , AtomicInteger savedRecords, AtomicInteger allRecords, AtomicInteger duplicatedIds
    ) {

        if (product.getId()==null || !productRepository.existsById(product.getId())) {
            productRepository.save(product);
            savedRecords.incrementAndGet();
        } else {
            duplicatedIds.incrementAndGet();
        }
        allRecords.incrementAndGet();
    }
}
