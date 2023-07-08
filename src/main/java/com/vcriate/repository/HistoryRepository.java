package com.vcriate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vcriate.entity.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

}
