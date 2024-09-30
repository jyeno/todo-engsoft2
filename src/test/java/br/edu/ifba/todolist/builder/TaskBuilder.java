package br.edu.ifba.todolist.builder;

import java.time.LocalDateTime;

import br.edu.ifba.todolist.entity.Task;
import br.edu.ifba.todolist.entity.TaskStatus;

public final class TaskBuilder {

    private TaskBuilder() {}

    public static Task buildTask(String title, TaskStatus status) {
        return buildTask(1L, title, "some description", status,
                         LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(7));
    }

    public static Task buildTask(Long id, String title, String description, TaskStatus status,
                                 LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime expiresAt) {
        return Task.builder()
               .id(id)
               .title(title)
               .description(description)
               .status(status)
               .createdAt(createdAt)
               .updatedAt(updatedAt)
               .expiresAt(expiresAt)
               .build();
    }
}
