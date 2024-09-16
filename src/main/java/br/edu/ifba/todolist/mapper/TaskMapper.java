package br.edu.ifba.todolist.mapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.edu.ifba.todolist.dto.TaskDTO;
import br.edu.ifba.todolist.entity.Task;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    @Mapping(target = "expiresIn", expression = "java(getRemainingTimeString(source.getExpiresAt()))")
    public abstract TaskDTO toTaskDTO(Task source);

    public abstract Task toTask(TaskDTO source);

    public String getRemainingTimeString(LocalDateTime expirationTime) {
        var currentTime = LocalDateTime.now();
        var daysRemaining = currentTime.until(expirationTime, ChronoUnit.DAYS);
        if (daysRemaining == 1) {
            var hoursRemaining = currentTime.until(expirationTime, ChronoUnit.HOURS);
            return hoursRemaining + " hours";
        }
        return daysRemaining + " days";
    }
}
