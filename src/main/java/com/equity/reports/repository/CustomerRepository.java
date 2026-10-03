package com.equity.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equity.reports.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
