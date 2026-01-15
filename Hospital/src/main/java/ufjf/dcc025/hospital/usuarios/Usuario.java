package ufjf.dcc025.hospital.usuarios;
/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.outros.Cpf;
import ufjf.dcc025.hospital.outros.Email;
import ufjf.dcc025.hospital.outros.Senha;

public abstract class Usuario {

    protected String nome;
    protected Cpf cpf;
    protected Email email;
    protected Senha senha;

    public Usuario(String nome, String cpf, String email, String senha) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome inválido");

        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email inválido");

        if (senha == null || senha.isBlank())
            throw new IllegalArgumentException("Senha inválida");

        this.nome = nome;
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
        this.senha = new Senha(senha);
    } 
    
    public abstract String getTipo();
    
    public String getNome(){ return nome; }
    
    public void setNome(String nome) throws DadosInvalidosException {
        if(nome == null || nome.trim().isEmpty())
            throw new DadosInvalidosException("Nome não informado");
        this.nome = nome;
    }
    
    public String getCpf(){ return cpf.getNumero(); }
    
    public void setCpf(String novoCpf) throws DadosInvalidosException {
        this.cpf = new Cpf(novoCpf);
    }
        
    public void setSenha(String novaSenha) throws DadosInvalidosException {
        this.senha = new Senha(novaSenha);
    }
    
    public Email getEmail() { 
        return email; 
    }
    
    public String getEnderecoEmail() { 
        return email.getEmail(); 
    }
    
    public void setEmail(String novoEmail) throws DadosInvalidosException {
        String antigo = this.email.getEmail();
        Email novoObj = new Email(novoEmail); // Valida formato e unicidade
        
        // Se o novo objeto foi criado sem disparar exception (unicidade garantida),
        // removemos o registro do e-mail antigo da lista global.
        Email.removerEmail(antigo);
        this.email = novoObj;
    }
    
    public boolean validarSenha(String senhaDigitada) {
        return senha.verificar(senhaDigitada);
    }
}