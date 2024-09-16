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
import br.edu.ifba.todolist.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable("id") Long id) {
        TaskDTO p = service.findById(id);
        if (p != null) {
            return ResponseEntity.ok(p);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/title/{title}")
    public List<TaskDTO> getByTitle(@PathVariable("title") String title) {
        return service.findByTitle(title);
    }

    @GetMapping
    public List<TaskDTO> getAll() {
        return service.findAll();
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
