package com.manapi.manapiproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manapi.manapiproject.model.task.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
}
