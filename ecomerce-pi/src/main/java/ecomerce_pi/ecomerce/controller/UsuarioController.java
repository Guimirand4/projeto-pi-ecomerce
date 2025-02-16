package ecomerce_pi.ecomerce.controller;

import ecomerce_pi.ecomerce.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    public UsuarioController() {
        UsuarioRepository.criarTabela(); // Criar a tabela ao iniciar
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@RequestParam String email, @RequestParam String senha) {
        UsuarioRepository.cadastrarUsuario(email, senha);
        return "Usuário cadastrado com sucesso!";
    }

    @PostMapping("/login")
    public String autenticarUsuario(@RequestParam String email, @RequestParam String senha) {
        if (UsuarioRepository.autenticarUsuario(email, senha)) {
            return "Login bem-sucedido!";
        }
        return "Credenciais inválidas!";
    }
}
