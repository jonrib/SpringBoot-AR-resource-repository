package com.jonrib.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonrib.app.model.History;

public interface HistoryRepository extends JpaRepository<History, Long> {

}
