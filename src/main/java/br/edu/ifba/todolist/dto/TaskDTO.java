package br.edu.ifba.todolist.dto;

import br.edu.ifba.todolist.entity.TaskStatus;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {

    private Long id;

    @NotEmpty
    @Size(max = 100)
    private String title;

    @NotEmpty
    @Size(max = 500)
    private String description;

    @NotNull
    private TaskStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotNull
    private LocalDateTime expiresAt;

    private String expiresIn;
}
