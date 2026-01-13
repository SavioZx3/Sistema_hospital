package ufjf.dcc025.hospital.interfac.view.secretaria;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class CadastroPacienteView extends JFrame {
    private Hospital hospital;
    private JTextField txtNome, txtCpf, txtEmail, txtSenha, txtTel, txtCidade, txtBairro, txtRua, txtNum;
    private Runnable callback; // Para atualizar a tabela da secretaria ao fechar

    public CadastroPacienteView(Hospital hospital, Runnable callback) {
        this.hospital = hospital;
        this.callback = callback;
        
        setTitle("Cadastrar Novo Paciente");
        setSize(450, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelCampos = new JPanel(new GridLayout(9, 2, 10, 10));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Adição dos campos (Simplificado para o exemplo)
        painelCampos.add(new JLabel("Nome:")); txtNome = new JTextField(); painelCampos.add(txtNome);
        painelCampos.add(new JLabel("CPF:")); txtCpf = new JTextField(); painelCampos.add(txtCpf);
        painelCampos.add(new JLabel("Email:")); txtEmail = new JTextField(); painelCampos.add(txtEmail);
        painelCampos.add(new JLabel("Senha:")); txtSenha = new JPasswordField(); painelCampos.add(txtSenha);
        painelCampos.add(new JLabel("Telefone:")); txtTel = new JTextField(); painelCampos.add(txtTel);
        painelCampos.add(new JLabel("Cidade:")); txtCidade = new JTextField(); painelCampos.add(txtCidade);
        painelCampos.add(new JLabel("Bairro:")); txtBairro = new JTextField(); painelCampos.add(txtBairro);
        painelCampos.add(new JLabel("Rua:")); txtRua = new JTextField(); painelCampos.add(txtRua);
        painelCampos.add(new JLabel("Número:")); txtNum = new JTextField(); painelCampos.add(txtNum);

        JButton btnSalvar = new JButton(" Salvar Cadastro");
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSalvar.addActionListener(e -> salvar());

        add(painelCampos, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);
    }

    private void salvar() {
        try {
            Paciente p = new Paciente(
                txtNome.getText(), txtCpf.getText(), txtEmail.getText(), txtSenha.getText(),
                txtTel.getText(), txtCidade.getText(), txtBairro.getText(), txtRua.getText(), txtNum.getText()
            );
            hospital.cadastrarPaciente(p);
            JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
            if (callback != null) callback.run(); // Executa a atualização da tabela
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}