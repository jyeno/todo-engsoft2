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
    public ResponseEntity<List<TaskDTO>> getAll() {
        var tasks = service.findAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable("id") Long id) {
        var taskDTO = service.findById(id);
        return ResponseEntity.ok(taskDTO);
    }

    @GetMapping("/status/{taskStatus}")
    public ResponseEntity<List<TaskDTO>> getAll(@PathVariable("taskStatus") TaskStatus status) {
        var tasks = service.findAllByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> create(@RequestBody @Valid TaskDTO taskDTO) {
        var task = service.create(taskDTO);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable("id") Long id, @RequestBody @Valid TaskDTO taskDTO) {
        taskDTO.setId(id);
        var task = service.update(taskDTO);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}/{taskStatus}")
    public ResponseEntity<TaskDTO> update(@PathVariable("id") Long id, @PathVariable("taskStatus") TaskStatus status) {
        var taskDTO = service.findById(id);
        taskDTO.setStatus(status);
        var task = service.update(taskDTO);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
