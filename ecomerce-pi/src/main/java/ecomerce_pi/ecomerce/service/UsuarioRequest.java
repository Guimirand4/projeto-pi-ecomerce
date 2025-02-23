package ecomerce_pi.ecomerce.service;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import ecomerce_pi.ecomerce.model.Role;

public class UsuarioRequest {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @NotBlank(message = "A confirmação da senha é obrigatória")
    private String confirmarSenha;

    private Role role;

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
    public String getSenha() { return senha; }
    public String getConfirmarSenha() { return confirmarSenha; }
    public Role getRole() { return role; }
}
