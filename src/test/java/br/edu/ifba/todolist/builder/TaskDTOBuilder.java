package br.edu.ifba.todolist.builder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import br.edu.ifba.todolist.dto.TaskDTO;
import br.edu.ifba.todolist.entity.TaskStatus;

public final class TaskDTOBuilder {

    private TaskDTOBuilder() {}

    public static TaskDTO buildTaskDTO(String title, TaskStatus status) {
        return buildTaskDTO(1L, title, "some description", status,
                            LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(7));
    }

    public static TaskDTO buildTaskDTO(Long id, String title, String description, TaskStatus status,
                                       LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime expiresAt) {
        return TaskDTO.builder()
               .id(id)
               .title(title)
               .description(description)
               .status(status)
               .createdAt(createdAt)
               .updatedAt(updatedAt)
               .expiresAt(expiresAt)
               .expiresIn(createdAt.until(expiresAt, ChronoUnit.DAYS) + " days")
               .build();
    }
}
