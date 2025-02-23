package ecomerce_pi.ecomerce.controller;

import ecomerce_pi.ecomerce.model.Usuario;
import ecomerce_pi.ecomerce.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarUsuario( @RequestBody Usuario usuario) {
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
