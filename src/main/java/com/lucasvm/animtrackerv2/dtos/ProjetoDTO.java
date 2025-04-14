package com.lucasvm.animtrackerv2.dtos;

import com.lucasvm.animtrackerv2.models.ProjetoModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjetoDTO {
    private UUID id;
    private String nome;
    private String descricao;
    private ProjetoModel.statusProjeto status; // Alterado de String para enum
    private LocalDateTime data_inicio;
    private LocalDateTime data_previsao;
    private LocalDateTime data_conclusao;
    private ProjetoModel.tipoAnimacao tipo_animacao; // Já está como enum
    private BigDecimal duracao_segundos;
    private String responsavel;
    private String pasta_arquivos;
    private String observacoes;
    private LocalDateTime data_cadastro;
    private UUID cliente_id;
}