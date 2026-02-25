package com.prajjwal.securebanking.service.spec;

import com.prajjwal.securebanking.dto.AuditLogFilterRequestDto;
import com.prajjwal.securebanking.model.AuditLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AuditLogSpecification {

    public static Specification<AuditLog> filter(AuditLogFilterRequestDto request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getUsername() != null) {
                predicates.add(cb.equal(root.get("username"), request.getUsername()));
            }
            if (request.getAction() != null) {
                predicates.add(cb.equal(root.get("action"), request.getAction()));
            }
            if (request.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), request.getFromDate()));
            }
            if (request.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), request.getToDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}