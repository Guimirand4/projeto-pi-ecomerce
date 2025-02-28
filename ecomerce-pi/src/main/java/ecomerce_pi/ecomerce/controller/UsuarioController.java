package ecomerce_pi.ecomerce.controller;

import ecomerce_pi.ecomerce.model.Usuario;
import ecomerce_pi.ecomerce.service.UsuarioService;
import ecomerce_pi.ecomerce.service.UsuarioRequest;
import ecomerce_pi.ecomerce.repository.UsuarioRepository;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        if (!usuarioRequest.getSenha().equals(usuarioRequest.getConfirmarSenha())) {
            return ResponseEntity.badRequest().body("As senhas não coincidem!");
        }

        if (usuarioRepository.findByEmail(usuarioRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado!");
        }

        if (usuarioRepository.findByCpf(usuarioRequest.getCpf()).isPresent()) {
            return ResponseEntity.badRequest().body("CPF já cadastrado!");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRequest.getNome());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));
        usuario.setRole(usuarioRequest.getRole());
        usuario.setCpf(usuarioRequest.getCpf());

        usuarioService.cadastrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> autenticarUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.autenticarUsuario(usuario.getEmail(), usuario.getSenha())) {
            return ResponseEntity.ok("Login bem-sucedido!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas!");
    }

    @GetMapping("/listar") // Adicionando o caminho correto
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        boolean atualizado = usuarioService.atualizarUsuario(id, usuarioAtualizado);
        if (atualizado) {
            return ResponseEntity.ok("Usuário atualizado com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> alterarStatus(@PathVariable Long id) {
        boolean alterado = usuarioService.alterarStatusUsuario(id);
        if (alterado) {
            return ResponseEntity.ok("Status do usuário atualizado!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Erro interno: " + e.getMessage());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }  
    
}
