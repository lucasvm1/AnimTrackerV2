package com.lucasvm.animtrackerv2.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cenas")
@Data
public class CenaModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String numero_codigo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCena status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstagioCena estagio;

    private int frames;

    private BigDecimal duracao; // em segundos

    private int pontuacao;

    private LocalDate data_inicio;

    private LocalDate data_previsao;

    private LocalDate data_conclusao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @CreationTimestamp
    private LocalDateTime data_cadastro;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private ProjetoModel projeto;

    @Getter
    public enum StatusCena {
        NAO_INICIADA("Não Iniciada"),
        EM_PRODUCAO("Em Produção"),
        PAUSADA("Pausada"),
        APROVADA("Aprovada");

        private final String displayName;

        StatusCena(String displayName) {
            this.displayName = displayName;
        }

    }

    @Getter
    public enum EstagioCena {
        POSE("Pose"),
        ANIMACAO("Animação"),
        CORRECAO("Correção");

        private final String displayName;

        EstagioCena(String displayName) {
            this.displayName = displayName;
        }

    }
}