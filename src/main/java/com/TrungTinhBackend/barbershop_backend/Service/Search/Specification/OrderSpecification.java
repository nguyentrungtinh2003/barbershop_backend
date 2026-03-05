package com.TrungTinhBackend.barbershop_backend.Service.Search.Specification;

import com.TrungTinhBackend.barbershop_backend.Entity.Orders;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class OrderSpecification {
    public static Specification<Orders> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("username")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("phoneNumber").as(String.class)), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("address")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("email")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("shop").get("name")), pattern),
                    criteriaBuilder.like(root.get("id").as(String.class), pattern)
            );
        };
    }
}
