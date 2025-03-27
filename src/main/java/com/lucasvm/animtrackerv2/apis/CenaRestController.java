package com.lucasvm.animtrackerv2.apis;

import com.lucasvm.animtrackerv2.dtos.ApiResponse;
import com.lucasvm.animtrackerv2.dtos.CenaDTO;
import com.lucasvm.animtrackerv2.models.UsuarioModel;
import com.lucasvm.animtrackerv2.services.CenaService;
import com.lucasvm.animtrackerv2.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cenas")
public class CenaRestController {

    @Autowired
    private CenaService cenaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CenaDTO>>> listarCenas(Principal principal) {
        List<CenaDTO> cenas = cenaService.listarTodosPorUsuario(
                usuarioService.getUsuarioAutenticado(principal).getId());
        return ResponseEntity.ok(new ApiResponse<>(cenas, true, "Cenas listadas com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CenaDTO>> buscarCena(@PathVariable UUID id, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        CenaDTO cena = cenaService.buscarPorId(id, usuarioAutenticado.getId());
        return cena == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(new ApiResponse<>(cena, true, "Cena encontrada com sucesso"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CenaDTO>> salvarCena(@RequestBody CenaDTO cenaDTO, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        CenaDTO cena = cenaService.salvar(cenaDTO, usuarioAutenticado.getId());
        return ResponseEntity.ok(new ApiResponse<>(cena, true, "Cena salva com sucesso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CenaDTO>> atualizarCena(@PathVariable UUID id,@RequestBody CenaDTO cenaDTO, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        return cenaService.atualizar(id, cenaDTO, usuarioAutenticado.getId())
                .map(cena -> ResponseEntity.ok(new ApiResponse<>(cena, true, "Cena atualizada com sucesso")))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> removerCena(@PathVariable UUID id, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        cenaService.remover(id, usuarioAutenticado.getId());
        return ResponseEntity.ok(new ApiResponse<>(null, true, "Cena removida com sucesso"));
    }

}
