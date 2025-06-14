package com.TrungTinhBackend.barbershop_backend.Service.Search.Specification;

import com.TrungTinhBackend.barbershop_backend.Entity.Feedbacks;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class FeedbackSpecification {
    public static Specification<Feedbacks> searchByKeyword(String keyword) {
        return (root,query,criteriaBuilder) -> {
            if(keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("comment")),pattern)
            );
        };
    }
}
