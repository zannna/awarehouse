package com.example.awarehouse.module.report;

import com.example.awarehouse.module.group.GroupWorkerService;
import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.report.dto.ReportCreationDto;
import com.example.awarehouse.module.report.dto.ReportDto;
import com.example.awarehouse.module.token.BasicTokenGenerator;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WorkerNotHaveAccess;
import com.example.awarehouse.util.TimeSupplier;
import com.example.awarehouse.util.UserIdSupplier;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {
    private final BasicTokenGenerator basicTokenGenerator;
    private final TimeSupplier timeSupplier;
    private final ReportRepository reportRepository;
    private final WorkerWarehouseService workerWarehouseService;
    private final GroupWorkerService groupWorkerService;
    private final UserIdSupplier workerIdSupplier;


    @Transactional
    public ReportDto setUnderstockReport(ReportCreationDto reportCreationDto) {
        ZonedDateTime time = timeSupplier.getTime();
        String entityName;
        if(reportCreationDto.reportScope().equals(ReportScope.WAREHOUSE)){
           Warehouse warehouse = workerWarehouseService.getWarehouse(workerIdSupplier.getUserId(), reportCreationDto.reportScopeId())
                   .orElseThrow(()->new WorkerNotHaveAccess("Worker does not have access to warehouse with id "+reportCreationDto.reportScopeId()));
              entityName = warehouse.getName();
        } else {
            WarehouseGroup group = groupWorkerService.getGroup(workerIdSupplier.getUserId(), reportCreationDto.reportScopeId())
                    .orElseThrow(()->new WorkerNotHaveAccess("Worker does not have access to group with id "+reportCreationDto.reportScopeId()));
            entityName = group.getName();
        }
        Report report = new Report(time, reportCreationDto.reportInterval(), reportCreationDto.email(),
                reportCreationDto.reportScope(), reportCreationDto.reportScopeId(), workerIdSupplier.getUserId());
        report= reportRepository.save(report);
        setToken(report);
        return ReportMapper.toDto(report, entityName);
    }

    private void setToken(Report report){
        String salt = basicTokenGenerator.generateSalt();
        String token= basicTokenGenerator.generateToken(report.getId().toString(), salt);
        report.setToken(token);
    }

    public List<ReportDto> getUserReports(){
        List<Report> reports = reportRepository.findByOwnerId(workerIdSupplier.getUserId());
        if(reports.isEmpty()){
            return Collections.emptyList();
        }
        Map<ReportScope, Set<UUID>> entities = reports.stream().collect(Collectors.groupingBy(Report::getReportScope,
                Collectors.mapping(Report::getScopeEntityId, Collectors.toSet())));
        Map<UUID, Warehouse> warehouses= workerWarehouseService.getWarehouses( workerIdSupplier.getUserId(), entities.get(ReportScope.WAREHOUSE)).
                stream().collect(Collectors.toMap(Warehouse::getId, warehouse -> warehouse));
        Map<UUID, WarehouseGroup> groups = groupWorkerService.getGroups( workerIdSupplier.getUserId(), entities.get(ReportScope.GROUP)).stream().
                collect(Collectors.toMap( WarehouseGroup::getId, group -> group));
        List<ReportDto> response = reports.stream().map(report -> {
            if(report.getReportScope().equals(ReportScope.WAREHOUSE)){
                return ReportMapper.toDto(report, warehouses.get(report.getScopeEntityId()).getName());
            } else {
                return ReportMapper.toDto(report, groups.get(report.getScopeEntityId()).getName());
            }
        }).collect(Collectors.toList());
        return response;
    }
}
