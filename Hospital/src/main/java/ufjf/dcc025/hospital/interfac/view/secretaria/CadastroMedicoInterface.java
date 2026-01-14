/*
    Mateus Gomes da Silva Matrícula: 202465565C 
    Marcos José De Oliveira Júnior Matrícula: 202135011 
    Sávio Machado Lima Matrícula: 202435014
*/
package ufjf.dcc025.hospital.interfac.view.secretaria;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.interfac.view.BaseView;
import ufjf.dcc025.hospital.usuarios.medico.Especialidade;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class CadastroMedicoInterface extends BaseView {

    private JTextField txtNome, txtCpf, txtEmail, txtEspecialidade;
    private JComboBox<Especialidade> comboEspecialidade;
    private JPasswordField txtSenha;
    private Runnable callbackAtualizacao; // Funcionalidade para atualizar a lista na secretaria

    // Construtor aceita o Hospital e uma ação de atualização
    public CadastroMedicoInterface(Hospital hospital, Runnable callbackAtualizacao) {
        super(hospital, "Cadastrar Novo Médico");
        this.callbackAtualizacao = callbackAtualizacao;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        configurarJanela();
        criarFormulario();

        setVisible(true);
    }

    private void configurarJanela() {
        setTitle(" Cadastrar Novo Médico");
        setSize(400, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
    }

    private void criarFormulario() {
        // Cabeçalho Bonito
        JPanel pnlTitulo = new JPanel();
        pnlTitulo.setBackground(new Color(41, 128, 185));
        JLabel lblTitulo = new JLabel("NOVO PROFISSIONAL");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        pnlTitulo.add(lblTitulo);
        add(pnlTitulo, BorderLayout.NORTH);

        // Painel de Campos
        JPanel pnlCampos = new JPanel(new GridLayout(5, 1, 5, 5));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        txtNome = criarCampo("Nome Completo:", pnlCampos);
        txtCpf = criarCampo("CPF (Somente números):", pnlCampos);
        txtEmail = criarCampo("E-mail:", pnlCampos);

        pnlCampos.add(new JLabel("Especialidade:"));
        comboEspecialidade = new JComboBox<>(Especialidade.values());
        pnlCampos.add(comboEspecialidade);

        // Campo de Senha Manual (GridLayout não suporta labels flutuantes bem)
        pnlCampos.add(new JLabel("Senha de Acesso:"));
        txtSenha = new JPasswordField();
        pnlCampos.add(txtSenha);

        // Botão Salvar (Visual Estilizado)
        JButton btnSalvar = new JButton("💾 SALVAR MÉDICO");
        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setPreferredSize(new Dimension(0, 50));
        btnSalvar.addActionListener(e -> salvar());

        add(pnlCampos, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);
    }

    private JTextField criarCampo(String label, JPanel painel) {
        painel.add(new JLabel(label));
        JTextField campo = new JTextField();
        painel.add(campo);
        return campo;
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().replaceAll("[^0-9]", "");
        String email = txtEmail.getText().trim();
        Especialidade especialidade = (Especialidade) comboEspecialidade.getSelectedItem();
        String senha = new String(txtSenha.getPassword());

        if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            mostrarAviso("Todos os campos são obrigatórios!");
            return;
        }

        try {
            Medico novoMedico = new Medico(nome, cpf, email, senha, especialidade);
            hospital.cadastrarMedico(novoMedico);
            hospital.salvarAlteracoes(); // Salva JSON

            mostrarInfo("Médico cadastrado com sucesso!");
            // Se houver uma tela de secretaria aberta, ela atualiza agora!
            if (callbackAtualizacao != null) {
                callbackAtualizacao.run();
            }

            this.dispose();
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }
}
