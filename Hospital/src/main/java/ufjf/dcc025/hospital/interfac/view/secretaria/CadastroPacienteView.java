package ufjf.dcc025.hospital.interfac.view.secretaria;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.interfac.view.BaseView; 
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class CadastroPacienteView extends BaseView { 
    
    private JTextField txtNome, txtCpf, txtEmail, txtTel, txtCidade, txtBairro, txtRua, txtNum;
    private JPasswordField txtSenha; // 2. Usar JPasswordField por segurança
    private Runnable callback;

    public CadastroPacienteView(Hospital hospital, Runnable callback) {
        super(hospital, "Cadastrar Novo Paciente");
        this.callback = callback;
        
        setSize(450, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        criarInterface(); 
        setVisible(true);
    }

    private void criarInterface() {
        JPanel painelCampos = new JPanel(new GridLayout(9, 2, 10, 10));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painelCampos.add(new JLabel("Nome:")); txtNome = new JTextField(); painelCampos.add(txtNome);
        painelCampos.add(new JLabel("CPF:")); txtCpf = new JTextField(); painelCampos.add(txtCpf);
        painelCampos.add(new JLabel("Email:")); txtEmail = new JTextField(); painelCampos.add(txtEmail);
        painelCampos.add(new JLabel("Senha:")); txtSenha = new JPasswordField(); painelCampos.add(txtSenha);
        painelCampos.add(new JLabel("Telefone:")); txtTel = new JTextField(); painelCampos.add(txtTel);
        painelCampos.add(new JLabel("Cidade:")); txtCidade = new JTextField(); painelCampos.add(txtCidade);
        painelCampos.add(new JLabel("Bairro:")); txtBairro = new JTextField(); painelCampos.add(txtBairro);
        painelCampos.add(new JLabel("Rua:")); txtRua = new JTextField(); painelCampos.add(txtRua);
        painelCampos.add(new JLabel("Número:")); txtNum = new JTextField(); painelCampos.add(txtNum);

        JButton btnSalvar = new JButton("Salvar Cadastro");
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> salvar());

        add(painelCampos, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);
    }

    private void salvar() {
        try {
            //  Pegar senha de forma segura
            String senha = new String(txtSenha.getPassword());

            Paciente p = new Paciente(
                txtNome.getText(), txtCpf.getText(), txtEmail.getText(), senha,
                txtTel.getText(), txtCidade.getText(), txtBairro.getText(), txtRua.getText(), txtNum.getText()
            );

            hospital.cadastrarPaciente(p);
            
           //  Salvar no JSON para não perder o cadastro
            hospital.salvarAlteracoes(); 
            
            mostrarInfo("Paciente cadastrado com sucesso!"); 
            
            if (callback != null) callback.run(); 
            dispose();
        } catch (Exception ex) {
            mostrarErro(ex.getMessage()); 
        }
    }
}