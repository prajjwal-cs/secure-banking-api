package com.prajjwal.securebanking.service.spec;

import com.prajjwal.securebanking.dto.TransactionFilterRequestDto;
import com.prajjwal.securebanking.model.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<Transaction> filter(TransactionFilterRequestDto request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getAccountNumber() != null) {
                Predicate fromPredicate = criteriaBuilder
                        .equal(root.get("fromAccount").get("accountNumber"), request.getAccountNumber());

                Predicate toPredicate = criteriaBuilder
                        .equal(root.get("toAccount").get("accountNumber"), request.getAccountNumber());

                predicates.add(criteriaBuilder.or(fromPredicate, toPredicate));
            }
            if (request.getType() != null) {
                predicates.add(criteriaBuilder
                        .equal(root.get("type"), request.getType()));
            }
            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder
                        .equal(root.get("status"), request.getStatus()));
            }
            if (request.getFromDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"), request.getFromDate()
                ));
            }
            if (request.getToDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"), request.getToDate()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}