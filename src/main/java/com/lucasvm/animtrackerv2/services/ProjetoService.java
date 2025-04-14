package com.lucasvm.animtrackerv2.services;

import com.lucasvm.animtrackerv2.dtos.ProjetoDTO;
import com.lucasvm.animtrackerv2.models.ClienteModel;
import com.lucasvm.animtrackerv2.models.ProjetoModel;
import com.lucasvm.animtrackerv2.models.UsuarioModel;
import com.lucasvm.animtrackerv2.repositories.ClienteRepository;
import com.lucasvm.animtrackerv2.repositories.ProjetoRepository;
import com.lucasvm.animtrackerv2.repositories.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ProjetoDTO> listarTodosPorUsuario(UUID usuarioId) {
        List<ProjetoModel> projetos = projetoRepository.findByClienteUsuarioId(usuarioId);
        return projetos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProjetoDTO> listarTodosPorCliente(UUID usuarioID, UUID clienteID) {
        List<ProjetoModel> projetos = projetoRepository.findByClienteIdAndClienteUsuarioId(usuarioID, clienteID);
        return projetos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjetoDTO buscarPorId(UUID id, UUID usuarioId) {
        return projetoRepository.findByIdAndClienteUsuarioId(id, usuarioId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public ProjetoDTO salvar(ProjetoDTO dto, UUID clienteId) {
        Optional<ClienteModel> clienteOpt = clienteRepository.findById(clienteId);
        if (clienteOpt.isEmpty()) {
            return null;
        }

        ProjetoModel projeto = convertToEntity(dto);
        projeto.setCliente(clienteOpt.get());
        projeto = projetoRepository.save(projeto);
        return convertToDTO(projeto);
    }

    public Optional<ProjetoDTO> atualizar(UUID id, ProjetoDTO dto, UUID usuarioId) {
        return projetoRepository.findByIdAndClienteUsuarioId(id, usuarioId)
                .map(projeto -> {
                    BeanUtils.copyProperties(dto, projeto, "id", "cliente", "data_cadastro");
                    return convertToDTO(projetoRepository.save(projeto));
                });
    }

    public void remover(UUID id, UUID usuarioId) {
        projetoRepository.findByIdAndClienteUsuarioId(id, usuarioId)
                .ifPresent(projetoRepository::delete);
    }


    // No método convertToDTO
    public ProjetoDTO convertToDTO(ProjetoModel projeto) {
        ProjetoDTO dto = new ProjetoDTO();
        dto.setId(projeto.getId());
        dto.setNome(projeto.getNome());
        dto.setDescricao(projeto.getDescricao());
        dto.setStatus(projeto.getStatus()); // Alterado: diretamente o enum
        dto.setData_inicio(projeto.getData_inicio());
        dto.setData_previsao(projeto.getData_previsao());
        dto.setData_conclusao(projeto.getData_conclusao());
        dto.setTipo_animacao(projeto.getTipo_animacao());
        dto.setDuracao_segundos(projeto.getDuracao_segundos());
        dto.setResponsavel(projeto.getResponsavel());
        dto.setPasta_arquivos(projeto.getPasta_arquivos());
        dto.setObservacoes(projeto.getObservacoes());
        dto.setData_cadastro(projeto.getData_cadastro());
        dto.setCliente_id(projeto.getCliente().getId());
        return dto;
    }

    // No método convertToEntity
    public ProjetoModel convertToEntity(ProjetoDTO dto) {
        ProjetoModel projeto = new ProjetoModel();
        projeto.setId(dto.getId());
        projeto.setNome(dto.getNome());
        projeto.setDescricao(dto.getDescricao());
        projeto.setStatus(dto.getStatus()); // Alterado: não precisa mais de valueOf
        projeto.setData_inicio(dto.getData_inicio());
        projeto.setData_previsao(dto.getData_previsao());
        projeto.setData_conclusao(dto.getData_conclusao());
        projeto.setTipo_animacao(dto.getTipo_animacao());
        projeto.setDuracao_segundos(dto.getDuracao_segundos());
        projeto.setResponsavel(dto.getResponsavel());
        projeto.setPasta_arquivos(dto.getPasta_arquivos());
        projeto.setObservacoes(dto.getObservacoes());
        return projeto;
    }
}