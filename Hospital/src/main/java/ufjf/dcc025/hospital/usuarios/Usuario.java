package ufjf.dcc025.hospital.usuarios;

public abstract class Usuario {

    protected String nome;
    protected String email;
    protected String senha;

    public Usuario(String nome, String email, String senha) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome inválido");

        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email inválido");

        if (senha == null || senha.isBlank())
            throw new IllegalArgumentException("Senha inválida");

        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }

    public boolean validarSenha(String senha) {
        return this.senha.equals(senha);
    }

    public abstract String getTipo();
}