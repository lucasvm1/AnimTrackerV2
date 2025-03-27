package com.lucasvm.animtrackerv2.repositories;

import com.lucasvm.animtrackerv2.models.CenaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CenaRepository extends JpaRepository<CenaModel, UUID> {
    // Buscar cenas por projeto
    List<CenaModel> findByProjetoId(UUID projetoId);

    // Buscar cenas por usuario
    List<CenaModel> findByProjetoClienteUsuarioId(UUID usuarioId);

    // Buscar cenas por projeto e status
    List<CenaModel> findByProjetoIdAndStatus(UUID projetoId, CenaModel.StatusCena status);

    // Buscar cena por ID e projeto (com verificação de acesso)
    Optional<CenaModel> findByIdAndProjetoId(UUID id, UUID projetoId);

    // Buscar cenas por projeto onde o projeto pertence a um usuário
    List<CenaModel> findByProjetoIdAndProjetoClienteUsuarioId(UUID projetoId, UUID usuarioId);

    // Buscar cena específica verificando se pertence ao usuário
    Optional<CenaModel> findByIdAndProjetoClienteUsuarioId(UUID id, UUID usuarioId);
}