package br.edu.ifba.todolist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifba.todolist.dto.TaskDTO;
import br.edu.ifba.todolist.entity.TaskStatus;
import br.edu.ifba.todolist.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping
    public List<TaskDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable("id") Long id) {
        var taskDTO = service.findById(id);
        if (taskDTO != null) {
            return ResponseEntity.ok(taskDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{taskStatus}")
    public List<TaskDTO> getAll(@PathVariable("taskStatus") TaskStatus status) {
        return service.findAllByStatus(status);
    }

    @PostMapping
    public TaskDTO create(@RequestBody @Valid TaskDTO taskDTO) {
        return service.create(taskDTO);
    }

    @PutMapping("/{id}")
    public TaskDTO update(@PathVariable("id") Long id, @RequestBody @Valid TaskDTO taskDTO) {
        taskDTO.setId(id);
        return service.update(taskDTO);
    }

    @PutMapping("/{id}/{taskStatus}")
    public TaskDTO update(@PathVariable("id") Long id, @PathVariable("taskStatus") TaskStatus status) {
        var taskDTO = service.findById(id);
        taskDTO.setStatus(status);
        return service.update(taskDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
