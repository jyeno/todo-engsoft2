package br.edu.ifba.todolist.repository;

import java.util.List;

import br.edu.ifba.todolist.entity.Task;
import br.edu.ifba.todolist.entity.TaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>  {
    List<Task> findAllByStatus(TaskStatus status);
}
