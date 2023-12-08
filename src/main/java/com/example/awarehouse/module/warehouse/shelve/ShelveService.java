package com.example.awarehouse.module.warehouse.shelve;

import com.example.awarehouse.module.product.dto.ProductCreationDto;
import com.example.awarehouse.module.product.dto.ProductDTO;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveCreationDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@AllArgsConstructor
public class ShelveService {

    public List<ProductDTO> createShelve(ShelveCreationDto shelveDto) {
        return null;
    }
}
