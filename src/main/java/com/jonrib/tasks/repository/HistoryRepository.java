package com.jonrib.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.tasks.model.History;

public interface HistoryRepository extends JpaRepository<History, Long> {

}
