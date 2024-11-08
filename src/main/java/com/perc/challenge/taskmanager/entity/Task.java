
package com.perc.challenge.taskmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name="Task")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull
    @NotBlank
    private String name;

    @Column
    private String description;

    @Column
    private Boolean completed = false;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Acepta el formato yyyy-MM-dd?
    private LocalDate dueDate;

}
