package com.perc.challenge.taskmanager.service;

import com.perc.challenge.taskmanager.constant.ConstantValues;
import com.perc.challenge.taskmanager.entity.Task;
import com.perc.challenge.taskmanager.exception.NotFoundException;
import com.perc.challenge.taskmanager.repository.TaskManagerRepository;
import com.perc.challenge.taskmanager.util.CommonUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
public class TaskManagerService {

    private TaskManagerRepository repository;

    public TaskManagerService(TaskManagerRepository TaskManagerRepository) {
        this.repository = TaskManagerRepository;
    }

    public Task findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), ConstantValues.Message.ERROR_TASK_NOT_FOUND));
    }

    public Task save(Task task) {

        return repository.save(task);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Page<Task> findAllTasks(Boolean completed, String dateAsString, int page, int size) throws DateTimeParseException {

        Pageable pageable = PageRequest.of(page, size);

        if (dateAsString != null) {
            LocalDate dueDate = CommonUtils.dateFormatter(dateAsString);
            return repository.findByDueDate(dueDate, pageable);
        } else if (completed != null) {
            return repository.findByCompleted(completed, pageable);
        } else if(completed != null && dateAsString != null) {
            LocalDate dueDate = CommonUtils.dateFormatter(dateAsString);
            return repository.findByCompletedAndDueDate(completed, dueDate, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

}

