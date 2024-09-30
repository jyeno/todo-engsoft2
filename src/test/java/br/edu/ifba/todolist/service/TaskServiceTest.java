package br.edu.ifba.todolist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.edu.ifba.todolist.dto.TaskDTO;
import br.edu.ifba.todolist.builder.TaskDTOBuilder;
import br.edu.ifba.todolist.builder.TaskBuilder;
import br.edu.ifba.todolist.entity.Task;
import br.edu.ifba.todolist.entity.TaskStatus;
import br.edu.ifba.todolist.mapper.TaskMapper;
import br.edu.ifba.todolist.exception.TaskNotFoundException;
import br.edu.ifba.todolist.service.TaskService;
import br.edu.ifba.todolist.repository.TaskRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TaskServiceTest {

    @Autowired
    private TaskService service;

    @Autowired
    private TaskMapper mapper;

    @MockBean
    private TaskRepository repository;

    private void assertTasksEqual(TaskDTO expected, TaskDTO received) {
        assertEquals(expected.getId(), received.getId());
        assertEquals(expected.getTitle(), received.getTitle());
        assertEquals(expected.getDescription(), received.getDescription());
        assertEquals(expected.getStatus(), received.getStatus());
        assertEquals(expected.getCreatedAt(), received.getCreatedAt());
        assertEquals(expected.getUpdatedAt(), received.getUpdatedAt());
        assertEquals(expected.getExpiresAt(), received.getExpiresAt());
    }

    @Test
    void shouldFindAll() throws Exception {
        var expected = TaskBuilder.buildTask("some title", TaskStatus.DOING);

        given(repository.findAll()).willReturn(List.of(expected));

        List<TaskDTO> response = service.findAll();
        var received = response.get(0);

        assertTasksEqual(mapper.toTaskDTO(expected), received);
    }

    @Test
    void shouldFindAllByStatus() throws Exception {
        var status = TaskStatus.DOING;
        var expected = TaskBuilder.buildTask("some title", status);

        given(repository.findAllByStatus(status)).willReturn(List.of(expected));

        List<TaskDTO> response = service.findAllByStatus(status);
        var received = response.get(0);

        assertTasksEqual(mapper.toTaskDTO(expected), received);
    }

    @Test
    void shouldFindById() throws Exception {
        var id = 1L;
        var expected = TaskBuilder.buildTask("some title", TaskStatus.DOING);

        given(repository.findById(id)).willReturn(Optional.of(expected));

        var received = service.findById(id);

        assertTasksEqual(mapper.toTaskDTO(expected), received);
    }

    @Test
    void shouldNotFindById() throws Exception {
        var invalidId = 100_000L;
        given(repository.findById(invalidId)).willReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.findById(invalidId));
    }

    @Test
    void shouldCreate() throws Exception {
        var originalDto = TaskDTOBuilder.buildTaskDTO("some title", TaskStatus.DOING);
        var task = mapper.toTask(originalDto);

        given(repository.save(task)).willReturn(task);

        var received = service.create(originalDto);

        assertTasksEqual(originalDto, received);
    }

    @Test
    void shouldUpdate() throws Exception {
        var originalDto = TaskDTOBuilder.buildTaskDTO("some title", TaskStatus.DOING);
        // simulating a change to the task, so we can test if they differ as they should
        Task updatedTask = TaskBuilder.buildTask(originalDto.getId(), originalDto.getTitle(),
                           originalDto.getDescription(), TaskStatus.DONE,
                           originalDto.getCreatedAt(), LocalDateTime.now(),
                           originalDto.getExpiresAt());

        given(repository.existsById(originalDto.getId())).willReturn(true);
        given(repository.save(updatedTask)).willReturn(updatedTask);

        var received = service.update(mapper.toTaskDTO(updatedTask));

        assertEquals(originalDto.getId(), received.getId());
        assertEquals(originalDto.getTitle(), received.getTitle());
        assertEquals(originalDto.getDescription(), received.getDescription());
        assertNotEquals(originalDto.getStatus(), received.getStatus());
        assertEquals(originalDto.getCreatedAt(), received.getCreatedAt());
        assertNotEquals(originalDto.getUpdatedAt(), received.getUpdatedAt());
        assertEquals(originalDto.getExpiresAt(), received.getExpiresAt());
    }

    @Test
    void shouldNotUpdate() throws Exception {
        var dto = TaskDTOBuilder.buildTaskDTO("some title", TaskStatus.DOING);
        given(repository.existsById(dto.getId())).willReturn(false);

        assertThrows(TaskNotFoundException.class, () -> service.update(dto));
    }

    @Test
    void shouldRemove() throws Exception {
        var id = 1L;
        given(repository.existsById(id)).willReturn(true);
        doNothing().when(repository).deleteById(id);

        service.remove(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void shouldNotRemove() throws Exception {
        var invalidId = 100_000L;
        given(repository.existsById(invalidId)).willReturn(false);

        assertThrows(TaskNotFoundException.class, () -> service.remove(invalidId));
    }
}
