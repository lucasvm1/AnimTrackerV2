package com.lucasvm.animtrackerv2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private UUID id;
    private String nome;
    private LocalDate data_nascimento;
    private String email;
    private String senha;
    private String status;
    private String auth_provider;
}
