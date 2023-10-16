package br.com.isabelerau.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.isabelerau.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        System.out.println("Chegou no controller");
        var idUsuario = request.getAttribute("idUsuario");
        taskModel.setIdUsuario((UUID) idUsuario);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getDtInicio()) || currentDate.isAfter(taskModel.getDtFim())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início/ data de fim não pode ser retroativa");
        }

        if (taskModel.getDtInicio().isAfter(taskModel.getDtFim())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início maior que a data final");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUsuario = request.getAttribute("idUsuario");
        var tasks = this.taskRepository.findByIdUsuario((UUID) idUsuario);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody TaskModel taskModel, HttpServletRequest request,
            @PathVariable UUID id) {
        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tarefa não encontrada");
        }

        var idUsuario = request.getAttribute("idUsuario");

        if (!task.getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário sem permissão para executar esta alteração");
        }
        Utils.copyNonNullProperties(taskModel, task);

        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }
}
