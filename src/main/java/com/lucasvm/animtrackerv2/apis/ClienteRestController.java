package com.lucasvm.animtrackerv2.apis;

import com.lucasvm.animtrackerv2.dtos.ApiResponse;
import com.lucasvm.animtrackerv2.dtos.ClienteDTO;
import com.lucasvm.animtrackerv2.models.UsuarioModel;
import com.lucasvm.animtrackerv2.services.ClienteService;
import com.lucasvm.animtrackerv2.services.UsuarioService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteDTO>>> listarClientes(Principal principal) {
        List<ClienteDTO> clientes = clienteService.listarTodosPorUsuario(
                usuarioService.getUsuarioAutenticado(principal).getId());
        return ResponseEntity.ok(new ApiResponse<>(clientes, true, "Clientes listados com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> buscarCliente(@PathVariable UUID id, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        ClienteDTO cliente = clienteService.buscarPorId(id, usuarioAutenticado.getId());
        return cliente == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(new ApiResponse<>(cliente, true, "Cliente encontrado com sucesso"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteDTO>> salvarCliente(@RequestBody ClienteDTO clienteDTO, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        ClienteDTO cliente = clienteService.salvar(clienteDTO, usuarioAutenticado.getId());
        return ResponseEntity.ok(new ApiResponse<>(cliente, true, "Cliente salvo com sucesso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> atualizarCliente(@PathVariable UUID id, @RequestBody ClienteDTO clienteDTO, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        return clienteService.atualizar(id, clienteDTO, usuarioAutenticado.getId())
                .map(cliente -> ResponseEntity.ok(new ApiResponse<>(cliente, true, "Cliente atualizado com sucesso")))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> removerCliente(@PathVariable UUID id, Principal principal) {
        UsuarioModel usuarioAutenticado = usuarioService.getUsuarioAutenticado(principal);
        clienteService.remover(id, usuarioAutenticado.getId());
        return ResponseEntity.ok(new ApiResponse<>(null, true, "Cliente removido com sucesso"));
    }

}
