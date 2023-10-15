package br.com.isabelerau.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByIdUsuario(UUID idUsuario);

    // TaskModel findByIdAndByIdUsuario(UUID id, UUID idUsuario);

}
