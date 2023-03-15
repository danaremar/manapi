package com.manapi.manapiproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manapi.manapiproject.model.column.Column;

public interface ColumnRepository extends JpaRepository<Column, Long> {
    
}
