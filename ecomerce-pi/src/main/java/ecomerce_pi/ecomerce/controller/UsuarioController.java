package ecomerce_pi.ecomerce.controller;


import ecomerce_pi.ecomerce.model.Usuario;
import ecomerce_pi.ecomerce.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@RequestBody Usuario usuario) {
        usuarioService.cadastrarUsuario(usuario);
        return "Usuário cadastrado com sucesso!";
    }

    @PostMapping("/login")
    public String autenticarUsuario(@RequestParam String email, @RequestParam String senha) {
        if (usuarioService.autenticarUsuario(email, senha)) {
            return "Login bem-sucedido!";
        }
        return "Credenciais inválidas!";
    }
}