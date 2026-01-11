package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class CadastroPacienteView extends JDialog {
    private Hospital hospital;
    private JTextField txtNome, txtCpf, txtEmail, txtSenha, txtTel, txtCidade, txtBairro, txtRua, txtNum;

    public CadastroPacienteView(Frame parent, Hospital hospital) {
        super(parent, "Cadastrar Novo Paciente", true);
        this.hospital = hospital;
        setLayout(new BorderLayout());
        setSize(400, 500);
        setLocationRelativeTo(parent);

        JPanel painelCampos = new JPanel(new GridLayout(9, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelCampos.add(new JLabel("Nome:")); txtNome = new JTextField(); painelCampos.add(txtNome);
        painelCampos.add(new JLabel("CPF:")); txtCpf = new JTextField(); painelCampos.add(txtCpf);
        painelCampos.add(new JLabel("Email:")); txtEmail = new JTextField(); painelCampos.add(txtEmail);
        painelCampos.add(new JLabel("Senha (min 4):")); txtSenha = new JTextField(); painelCampos.add(txtSenha);
        painelCampos.add(new JLabel("Telefone:")); txtTel = new JTextField(); painelCampos.add(txtTel);
        painelCampos.add(new JLabel("Cidade:")); txtCidade = new JTextField(); painelCampos.add(txtCidade);
        painelCampos.add(new JLabel("Bairro:")); txtBairro = new JTextField(); painelCampos.add(txtBairro);
        painelCampos.add(new JLabel("Rua:")); txtRua = new JTextField(); painelCampos.add(txtRua);
        painelCampos.add(new JLabel("Número:")); txtNum = new JTextField(); painelCampos.add(txtNum);

        JButton btnSalvar = new JButton("Salvar Cadastro");
        btnSalvar.addActionListener(e -> salvar());

        add(painelCampos, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void salvar() {
        try {
            // Criando o paciente com os parâmetros que sua classe exige
            Paciente p = new Paciente(
                txtNome.getText(), txtCpf.getText(), txtEmail.getText(), txtSenha.getText(),
                txtTel.getText(), txtCidade.getText(), txtBairro.getText(), txtRua.getText(), txtNum.getText()
            );
            hospital.cadastrarPaciente(p); // Certifique-se que o método no hospital é este
            JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage());
        }
    }
}