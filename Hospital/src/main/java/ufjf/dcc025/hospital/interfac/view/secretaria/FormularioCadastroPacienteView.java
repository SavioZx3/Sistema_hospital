package ufjf.dcc025.hospital.interfac.view.secretaria;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.interfac.view.BaseView;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class FormularioCadastroPacienteView extends BaseView {

    private JTextField txtNome, txtCpf, txtEmail, txtTelefone;
    private JTextField txtCidade, txtBairro, txtRua, txtNumero;
    private JPasswordField txtSenha;

    public FormularioCadastroPacienteView(Hospital hospital) {
        
        super(hospital, "Cadastro de Novo Paciente");

        setTitle("Cadastro de Paciente - Dados Completos");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        criarFormulario();
        setVisible(true);
    }

    private void criarFormulario() {
        JPanel painelCentral = new JPanel(new GridLayout(10, 2, 10, 10));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Dados Pessoais
        painelCentral.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelCentral.add(txtNome);

        painelCentral.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        painelCentral.add(txtCpf);

        painelCentral.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        painelCentral.add(txtEmail);

        painelCentral.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        painelCentral.add(txtSenha);

        painelCentral.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelCentral.add(txtTelefone);

        // Endereço (Necessário para o construtor)
        painelCentral.add(new JLabel("Cidade:"));
        txtCidade = new JTextField();
        painelCentral.add(txtCidade);

        painelCentral.add(new JLabel("Bairro:"));
        txtBairro = new JTextField();
        painelCentral.add(txtBairro);

        painelCentral.add(new JLabel("Rua:"));
        txtRua = new JTextField();
        painelCentral.add(txtRua);

        painelCentral.add(new JLabel("Número:"));
        txtNumero = new JTextField();
        painelCentral.add(txtNumero);

        JButton btnSalvar = new JButton(" Finalizar Cadastro");
        btnSalvar.setBackground(new Color(41, 128, 185));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> salvar());

        add(new JScrollPane(painelCentral), BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);
    }

    private void salvar() {

        if (txtNome.getText().trim().isEmpty() || txtCpf.getText().trim().isEmpty()
                || txtSenha.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Nome, CPF e Senha são obrigatórios!");
            return;
        }
        try {
            // Criando o paciente exatamente como o seu construtor pede
            Paciente novo = new Paciente(
                    txtNome.getText(),
                    txtCpf.getText(),
                    txtEmail.getText(),
                    new String(txtSenha.getPassword()),
                    txtTelefone.getText(),
                    txtCidade.getText(),
                    txtBairro.getText(),
                    txtRua.getText(),
                    txtNumero.getText()
            );

            hospital.cadastrarPaciente(novo);

            hospital.salvarAlteracoes(); // Salva no JSON

            JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
            dispose();

        } catch (Exception ex) {
            mostrarErro(ex); // Usando método da BaseView        }
        }
    }
}