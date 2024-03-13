package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.dto.ProductFilterField;
import com.example.awarehouse.module.product.dto.ProductWarehouseFilterField;
import com.example.awarehouse.module.product.dto.SortDirection;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProductSpecification {
    public static Specification<Product> containsLike(Map<ProductFilterField, String> searchConditions) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            searchConditions.forEach((attributeName, value) -> {
                switch (attributeName.getDataType()) {
                    case "string" ->
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(resolvePath(root, attributeName.getPath()).as(String.class)), "%" + value.toLowerCase() + "%"));
                    case "uuid" ->
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(resolvePath(root, attributeName.getPath()).as(String.class)), "%" + value + "%"));
                    case "number" ->
                            predicates.add(criteriaBuilder.equal(resolvePath(root, attributeName.getPath()).as(Double.class), Double.parseDouble(value)));
                }
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> hasGroupIn(List<UUID> groupIds){
        return (root, query, criteriaBuilder) -> root.get("group").get("id").in(groupIds);
    }

    public static Specification<Product> orderBy(Map<ProductFilterField, SortDirection> sortConditions) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                List<Order> orders = new ArrayList<>();

                sortConditions.forEach((field, direction) -> {
                    switch (direction) {
                        case ASC:
                            orders.add(criteriaBuilder.asc(resolvePath(root, field.getPath())));
                            break;
                        case DESC:
                            orders.add(criteriaBuilder.desc(resolvePath(root, field.getPath())));
                            break;
                    }
                });

                if (!orders.isEmpty()) {
                    query.orderBy(orders);
                }
            }
            return null;
        };
    }

    private static Path<Object> resolvePath(Root<Product> root, String field) {
        String[] parts = field.split("\\.");
        Path<Object> path = root.get(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            path = path.get(parts[i]);
        }
        return path;
    }
}
