package br.edu.ifba.todolist.dto;

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

    @NotNull
    private Long id;

    @NotEmpty
    @Size(max = 100)
    private String title;

    @NotEmpty
    @Size(max = 500)
    private String description;

    @NotNull
    private LocalDateTime createdAt;

    private String expiresIn;
}
