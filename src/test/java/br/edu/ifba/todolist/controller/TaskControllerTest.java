package br.edu.ifba.todolist.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.ifba.todolist.dto.TaskDTO;
import br.edu.ifba.todolist.entity.Task;
import br.edu.ifba.todolist.entity.TaskStatus;
import br.edu.ifba.todolist.mapper.TaskMapper;
import br.edu.ifba.todolist.exception.TaskNotFoundException;
import br.edu.ifba.todolist.exception.TaskControllerAdvice;
import br.edu.ifba.todolist.service.TaskService;
import br.edu.ifba.todolist.controller.TaskController;

@ContextConfiguration(classes = {TaskController.class, TaskControllerAdvice.class})
@WebMvcTest(controllers = {TaskController.class})
@AutoConfigureMockMvc
class TaskControllerTest {

    @MockBean
    private TaskService taskService;

    @Mock
    private TaskMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private TaskController taskController;

    private final String baseurl = "/tasks";

    private TaskDTO generateMockTask(Long id, TaskStatus status) {
        var createdAt = LocalDateTime.now();
        var updatedAt = createdAt;
        var expiresAt = createdAt.plusDays(7);
        var title = "ToDo thing";
        var description = "lorem ipsum lorem ipsum lorem ipsum";
        var task = new Task(id, title, description, status, createdAt, updatedAt, expiresAt);
        return mapper.toTaskDTO(task);
    }

    private String buildTaskDTOJson(Long id, TaskStatus status) {
        return new StringBuilder().append("{")
               .append("\"title\": \"task " + id + "\"")
               .append(",")
               .append("\"description\": \"description " + id + "\"")
               .append(",")
               .append("\"status\": \"" + status + "\"")
               .append(",")
               .append("\"createdAt\": \"2024-08-23T08:44:50.558036\"")
               .append(",")
               .append("\"updatedAt\": \"2024-08-23T08:44:50.558036\"")
               .append(",")
               .append("\"expiresAt\": \"2024-09-23T08:44:50.558036\"")
               .append("}")
               .toString();
    }

    @Test
    void getById() throws Exception {
        Long taskId = 1L;
        var taskExpected = generateMockTask(taskId, TaskStatus.DOING);

        when(taskService.findById(taskId)).thenReturn(taskExpected);

        mockMvc.perform(get(baseurl + "/" + taskId))
        .andExpect(status().isOk());
    }

    @Test
    void getByInvalidId() throws Exception {
        Long taskId = 1000L;
        when(taskService.findById(taskId)).thenThrow(new TaskNotFoundException(""));

        mockMvc.perform(get(baseurl + "/" + taskId))
        .andExpect(status().isNotFound());
    }

    @Test
    void getAll() throws Exception {
        List<TaskDTO> tasks = new ArrayList<>();
        for (Long i = 1L; i <= 10; i += 1) {
            tasks.add(generateMockTask(i, TaskStatus.DOING));
        }
        when(taskService.findAll()).thenReturn(tasks);

        mockMvc.perform(get("/tasks"))
        .andExpect(status().isOk());
    }

    @Test
    void getAllByStatus() throws Exception {
        var status = TaskStatus.DOING;
        List<TaskDTO> tasks = new ArrayList<>();
        for (Long i = 1L; i <= 10; i += 1) {
            tasks.add(generateMockTask(i, status));
        }
        when(taskService.findAllByStatus(status)).thenReturn(tasks);

        mockMvc.perform(get(baseurl + "/status/" + status))
        .andExpect(status().isOk());
    }

    @Test
    void getAllByInvalidStatus() throws Exception {
        mockMvc.perform(get(baseurl + "/status/INVALID"))
        .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTask() throws Exception {
        var taskId = 1L;
        var status = TaskStatus.DONE;
        var newTask = generateMockTask(taskId, status);
        when(taskService.create(newTask)).thenReturn(newTask);

        mockMvc.perform(post(baseurl).contentType(MediaType.APPLICATION_JSON)
                        .content(buildTaskDTOJson(taskId, status)))
        .andExpect(status().isOk());
    }

    @Test
    void testCreateInvalidTask() throws Exception {
        mockMvc.perform(post(baseurl).contentType(MediaType.APPLICATION_JSON)
                        .content(buildTaskDTOJson(0L, null)))
        .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTask() throws Exception {
        var taskId = 1L;
        var status = TaskStatus.DONE;
        var updatedTask = generateMockTask(taskId, status);
        when(taskService.update(updatedTask)).thenReturn(updatedTask);

        mockMvc.perform(put(baseurl + "/" + taskId).contentType(MediaType.APPLICATION_JSON)
                        .content(buildTaskDTOJson(taskId, status)))
        .andExpect(status().isOk());
    }

    @Test
    void testUpdateInvalidTask() throws Exception {
        mockMvc.perform(put(baseurl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildTaskDTOJson(1L, null)))
        .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteTask() throws Exception {
        var taskId = 1L;
        var taskExpected = generateMockTask(taskId, TaskStatus.DOING);

        when(taskService.findById(taskId)).thenReturn(taskExpected);

        mockMvc.perform(delete(baseurl + "/" + taskId))
        .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteInvalidTask() throws Exception {
        var taskId = 100000L;
        doThrow(new TaskNotFoundException("")).when(taskService).remove(taskId);

        mockMvc.perform(delete(baseurl + "/" + taskId))
        .andExpect(status().isNotFound());
    }
}
