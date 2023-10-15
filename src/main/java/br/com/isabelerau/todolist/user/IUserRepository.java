package br.com.isabelerau.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IUserRepository extends JpaRepository<UserModel, UIID> {

    UserModel findByUsername(String username);
}