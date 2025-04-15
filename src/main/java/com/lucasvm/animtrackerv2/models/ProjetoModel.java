package com.lucasvm.animtrackerv2.models;

import com.lucasvm.animtrackerv2.utils.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "projetos")
@Data
public class ProjetoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Convert(converter = AttributeEncryptor.class)
    private String nome;

    @Convert(converter = AttributeEncryptor.class)
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private statusProjeto status;

    private LocalDateTime data_inicio;

    private LocalDateTime data_previsao;

    private LocalDateTime data_conclusao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private tipoAnimacao tipo_animacao;

    private BigDecimal duracao_segundos;

    @Convert(converter = AttributeEncryptor.class)
    private String responsavel;

    @Convert(converter = AttributeEncryptor.class)
    private String pasta_arquivos;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @CreationTimestamp
    private LocalDateTime data_cadastro;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteModel cliente;

    public enum statusProjeto {
        NAO_INICIADO("Não Iniciado"),
        EM_ANDAMENTO("Em Andamento"),
        CONCLUIDO("Concluído"),
        CANCELADO("Cancelado");

        private final String displayName;

        statusProjeto(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum tipoAnimacao {
        TRADICIONAL("Animação Tradicional"),
        ANIMACAO_2D("Animação 2D"),
        ANIMACAO_3D("Animação 3D"),
        MOTION_GRAPHICS("Motion Graphics"),
        STOP_MOTION("Stop Motion"),
        CUTOUT("Cutout"),
        OUTROS("Outros");

        private final String displayName;

        tipoAnimacao(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

    }

}
