package com.lucasvm.animtrackerv2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private UUID id;
    private String nome;
    private String tipo;
    private String email_principal;
    private String telefone_principal;
    private String site;
    private String nome_contato;
    private String cargo_contato;
    private String email_secundario;
    private String telefone_secundario;
    private String categoria;
    private String observacoes;
    private LocalDateTime data_cadastro;
    private UUID usuario_id;
}
