/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.perc.challenge.taskmanager.controller;


import com.perc.challenge.taskmanager.constant.ConstantValues;
import com.perc.challenge.taskmanager.dto.TaskDTO;
import com.perc.challenge.taskmanager.entity.Task;
import com.perc.challenge.taskmanager.exception.TaskManagerException;
import com.perc.challenge.taskmanager.model.Summary;
import com.perc.challenge.taskmanager.model.TaskResponse;
import com.perc.challenge.taskmanager.service.TaskManagerService;
import com.perc.challenge.taskmanager.util.CommonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/tasks")
public class TaskManagerController {

    @Autowired
    private TaskManagerService service;

    ModelMapper mapper = new ModelMapper();

    // ================================================= //
    @GetMapping
    @Operation(description = "Get all tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = Summary.class)))
    })
    public ResponseEntity<Page<Task>> getAllTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String dueDate,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) throws DateTimeParseException {

        Page<Task> tasks = service.findAllTasks(completed, dueDate, page, size);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping(value = ConstantValues.ServiceUrl.ID_PARAM)
    @Operation(description = "Get task by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Integer id) throws TaskManagerException {

        TaskResponse response = new TaskResponse();

        Task task = service.findById(id);
        response.setTask(task);
        response.setSummary(new Summary(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), ""));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @Operation(description = "Create new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskDTO taskDTO) throws DateTimeParseException {

        TaskResponse response = new TaskResponse();
        Task task = mapper.map(taskDTO, Task.class);
        task.setDueDate(CommonUtils.dateFormatter(taskDTO.getDueDate()));
        service.save(task);
        response.setSummary(new Summary(HttpStatus.OK.value(), ConstantValues.Message.TASK_CREATED_SUCCESFULLY, ConstantValues.Message.WITHOUT_ERRORS_RESPONSE));
        response.setTask(task);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(ConstantValues.ServiceUrl.ID_PARAM)
    @Operation(description = "Update task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Summary> updateTask(@PathVariable Integer id, @RequestBody TaskDTO taskDTO) throws TaskManagerException {

        service.findById(id);
        Task task = this.mapper.map(taskDTO, Task.class);
        task.setDueDate(CommonUtils.dateFormatter(taskDTO.getDueDate()));
        task.setId(id);
        service.save(task);

        Summary response = new Summary(HttpStatus.OK.value(), ConstantValues.Message.TASK_UPDATED_SUCCESFULLY, ConstantValues.Message.WITHOUT_ERRORS_RESPONSE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(ConstantValues.ServiceUrl.ID_PARAM)
    @Operation(description = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Summary> deleteTask(@PathVariable Integer id) throws TaskManagerException {

        service.findById(id);
        service.deleteById(id);

        Summary response = new Summary(HttpStatus.OK.value(), ConstantValues.Message.TASK_DELETED_SUCCESFULLY, ConstantValues.Message.WITHOUT_ERRORS_RESPONSE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

// ============================================================================ //

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<TaskResponse>
    handleConstraintViolationException(ConstraintViolationException ex) {
        TaskResponse response = new TaskResponse();
        String finalMessage = "";
        boolean flag = true;

        for (ConstraintViolation c : ex.getConstraintViolations()) {
            if(!flag) {
                finalMessage = finalMessage.concat(", ").concat(c.getPropertyPath().toString());
                break;
            }
            finalMessage = c.getPropertyPath().toString();
            flag = false;
        }

        response.setSummary(new Summary(HttpStatus.BAD_REQUEST.value(), ConstantValues.Message.ERROR_RESPONSE, ConstantValues.Message.NULL_OR_EMPTY_DATA.concat(finalMessage)));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaskManagerException.class)
    public ResponseEntity<Summary>
    handleTaskManagerException(TaskManagerException ex) {
        Summary response = new Summary(ex.getStatusCode(), ConstantValues.Message.ERROR_RESPONSE, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Summary>
    handleParseException(DateTimeParseException ex) {
        Summary response = new Summary(HttpStatus.INTERNAL_SERVER_ERROR.value(), ConstantValues.Message.ERROR_RESPONSE, ConstantValues.Message.ERROR_DATE_PATTERN);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
