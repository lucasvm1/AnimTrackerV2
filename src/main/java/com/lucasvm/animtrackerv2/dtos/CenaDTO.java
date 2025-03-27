package com.lucasvm.animtrackerv2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CenaDTO {
    private UUID id;
    private String numero_codigo;
    private String descricao;
    private String status; // Valor do enum StatusCena
    private String estagio; // Valor do enum EstagioCena
    private int frames;
    private BigDecimal duracao;
    private int pontuacao;
    private LocalDate data_inicio;
    private LocalDate data_previsao;
    private LocalDate data_conclusao;
    private String observacoes;
    private UUID projetoId; // ID do projeto relacionado
}
