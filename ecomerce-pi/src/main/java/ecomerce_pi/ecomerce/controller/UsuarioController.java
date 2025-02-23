package ecomerce_pi.ecomerce.controller;

import ecomerce_pi.ecomerce.model.Role;
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

@RestController
@RequestMapping("/usuarios")
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository; // Repositório para verificar CPF e email

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        // Verifica se as senhas coincidem
        if (!usuarioRequest.getSenha().equals(usuarioRequest.getConfirmarSenha())) {
            return ResponseEntity.badRequest().body("As senhas não coincidem!");
        }

        // Verifica se o email já está cadastrado
        if (usuarioRepository.findByEmail(usuarioRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado!");
        }

        // Verifica se o CPF já está cadastrado
        if (usuarioRepository.findByCpf(usuarioRequest.getCpf()).isPresent()) {
            return ResponseEntity.badRequest().body("CPF já cadastrado!");
        }

        // Criando novo usuário
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRequest.getNome());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioRequest.getSenha())); // Criptografa a senha
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

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Erro interno: " + e.getMessage());
    }
}
