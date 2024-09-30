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
        List<Task> tasks = repository.findAllByStatus(status);
        List<TaskDTO> result = new ArrayList<>();

        tasks.forEach(p -> result.add(mapper.toTaskDTO(p)));

        return result;
    }

    public TaskDTO findById(Long id) throws TaskNotFoundException {
        Optional<Task> p = repository.findById(id);
        if (p.isPresent()) {
            return mapper.toTaskDTO(p.get());
        }
        throw new TaskNotFoundException("Requested id: " + id);
    }

    public void remove(Long id) throws TaskNotFoundException {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException("Requested id: " + id);
        }
        repository.deleteById(id);
    }

    public TaskDTO create(TaskDTO taskDTO) {
        var task = repository.save(mapper.toTask(taskDTO));
        return mapper.toTaskDTO(task);
    }

    public TaskDTO update(TaskDTO task) throws TaskNotFoundException {
        if (!repository.existsById(task.getId())) {
            throw new TaskNotFoundException("Requested id: " + task.getId());
        }
        return create(task);
    }
}
