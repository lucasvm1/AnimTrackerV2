package com.lucasvm.animtrackerv2.apis;

import com.lucasvm.animtrackerv2.dtos.ApiResponse;
import com.lucasvm.animtrackerv2.dtos.ProjetoDTO;
import com.lucasvm.animtrackerv2.models.UsuarioModel;
import com.lucasvm.animtrackerv2.services.ProjetoService;
import com.lucasvm.animtrackerv2.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoRestController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjetoDTO>>> listarProjetos(Principal principal) {
        List<ProjetoDTO> projetos = projetoService.listarTodosPorUsuario(
                usuarioService.getUsuarioAutenticado(principal).getId());
        return ResponseEntity.ok(new ApiResponse<>(projetos, true, "Projetos listados com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjetoDTO>> buscarProjeto(@PathVariable UUID id, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        ProjetoDTO projeto = projetoService.buscarPorId(id, usuarioAutenticado.getId());
        return projeto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(new ApiResponse<>(projeto, true, "Projeto encontrado com sucesso"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjetoDTO>> salvarProjeto(@RequestBody ProjetoDTO projetoDTO, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        ProjetoDTO projeto = projetoService.salvar(projetoDTO, usuarioAutenticado.getId());
        return ResponseEntity.ok(new ApiResponse<>(projeto, true, "Projeto salvo com sucesso"));
    }

   @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjetoDTO>> atualizarProjeto(@PathVariable UUID id, @RequestBody ProjetoDTO projetoDTO, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        return projetoService.atualizar(id, projetoDTO, usuarioAutenticado.getId())
                .map(projeto -> ResponseEntity.ok(new ApiResponse<>(projeto, true, "Projeto atualizado com sucesso")))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> removerProjeto(@PathVariable UUID id, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        projetoService.remover(id, usuarioAutenticado.getId());
        return ResponseEntity.ok(new ApiResponse<>(null, true, "Projeto removido com sucesso"));
    }

}
