package br.edu.ifba.todolist.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.ifba.todolist.dto.TaskDTO;
import br.edu.ifba.todolist.entity.Task;
import br.edu.ifba.todolist.entity.TaskStatus;
import br.edu.ifba.todolist.mapper.TaskMapper;
import br.edu.ifba.todolist.repository.TaskRepository;
import br.edu.ifba.todolist.service.TaskService;
import br.edu.ifba.todolist.exception.TaskNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    public List<TaskDTO> findAll() {
        List<Task> tasks = repository.findAll();
        List<TaskDTO> result = new ArrayList<>();

        tasks.forEach(p -> result.add(mapper.toTaskDTO(p)));
        return result;
    }

    public List<TaskDTO> findAllByStatus(TaskStatus status) {
        List<Task> tasks = repository.findAll();
        List<TaskDTO> result = new ArrayList<>();

        tasks.stream()
        .filter(task -> task.getStatus() == status)
        .forEach(p -> result.add(mapper.toTaskDTO(p)));

        return result;
    }

    public TaskDTO findById(Long id) {
        Optional<Task> p = repository.findById(id);
        if (p.isPresent()) {
            return mapper.toTaskDTO(p.get());
        }
        return null;
    }

    public void remove(Long id) throws TaskNotFoundException {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException("Requested id: " + id);
        }
        repository.deleteById(id);
    }

    public TaskDTO create(TaskDTO taskDTO) {
        Task task = repository.save(mapper.toTask(taskDTO));
        return mapper.toTaskDTO(task);
    }

    public TaskDTO update(TaskDTO p) throws TaskNotFoundException {
        if (!repository.existsById(p.getId())) {
            throw new TaskNotFoundException("Requested id: " + p.getId());
        }
        return create(p);
    }
}
