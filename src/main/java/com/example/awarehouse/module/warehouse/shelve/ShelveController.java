package com.example.awarehouse.module.warehouse.shelve;

import com.example.awarehouse.module.product.dto.ProductCreationDto;
import com.example.awarehouse.module.product.dto.ProductDTO;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveCreationDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1+URI_SHELVE)
@AllArgsConstructor
public class ShelveController {
    private final ShelveService shelveService;

    @PostMapping
    public List<ProductDTO> createShelve(@RequestBody ShelveCreationDto shelveDto) {
        return shelveService.createShelve(shelveDto);
    }
}
