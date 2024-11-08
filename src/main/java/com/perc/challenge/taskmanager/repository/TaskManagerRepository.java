package com.perc.challenge.taskmanager.repository;

import com.perc.challenge.taskmanager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TaskManagerRepository extends JpaRepository<Task, Integer> {

    Page<Task> findByCompleted(boolean completed, Pageable pageable);

    Page<Task> findByCompletedAndDueDate(boolean completed, LocalDate dueDate, Pageable pageable);
    Page<Task> findByDueDate(LocalDate dueDate, Pageable pageable);

}
