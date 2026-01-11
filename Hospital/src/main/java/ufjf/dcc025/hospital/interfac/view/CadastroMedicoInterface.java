package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class CadastroMedicoInterface extends JFrame {

    private final Hospital hospital;
    private JTextField txtNome, txtCpf, txtEmail, txtEspecialidade;
    private JPasswordField txtSenha;

    public CadastroMedicoInterface(Hospital hospital) {
        this.hospital = hospital;
        
        setTitle("Cadastrar Novo Médico");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Apenas fecha esta janela
        setLayout(new BorderLayout(10, 10));

        // Painel de Formulário
        JPanel pnlCampos = new JPanel(new GridLayout(6, 2, 10, 10));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pnlCampos.add(new JLabel("Nome Completo:"));
        txtNome = new JTextField();
        pnlCampos.add(txtNome);

        pnlCampos.add(new JLabel("CPF (somente números):"));
        txtCpf = new JTextField();
        pnlCampos.add(txtCpf);

        pnlCampos.add(new JLabel("E-mail:"));
        txtEmail = new JTextField();
        pnlCampos.add(txtEmail);

        pnlCampos.add(new JLabel("Especialidade:"));
        txtEspecialidade = new JTextField();
        pnlCampos.add(txtEspecialidade);

        pnlCampos.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        pnlCampos.add(txtSenha);

        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar Cadastro");
        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        btnSalvar.addActionListener(e -> salvar());

        add(pnlCampos, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().replaceAll("[^0-9]", ""); // Limpa o CPF
        String email = txtEmail.getText().trim();
        String especialidade = txtEspecialidade.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
            return;
        }

        try {
            Medico novoMedico = new Medico(nome, cpf, email, senha, especialidade);
            hospital.cadastrarMedico(novoMedico);
            
            JOptionPane.showMessageDialog(this, "Médico " + nome + " cadastrado com sucesso!");
            this.dispose(); // Fecha o formulário
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage());
        }
    }
}