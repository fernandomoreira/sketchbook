package View;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Contorller.Controller;
import ui.util.layouts.ParagraphLayout;



public class FrameTela extends JFrame {
	private JPanel painel;
	private JLabel lblOpcoes;
	private JComboBox cmbOpcoes;
	private String [] mensagem = {"OLÁ JAVA", "CAFÉ com java"};
	private JButton btnRight, btnLeft, btnFront,  btnBack;
	private JButton btnFecharConexao;
	private Controller listener;
	public FrameTela(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(200,300);
		criaPainel();
		setVisible(true);
	}

	private void criaPainel(){
		listener = new Controller(this);
		painel = new JPanel();
		painel.setLayout( new ParagraphLayout());
		painel.setBorder(BorderFactory.createTitledBorder("Exemplo comunicação com Arduino"));
		add(painel);
		lblOpcoes = new JLabel("Selecione");
		painel.add(lblOpcoes);
		cmbOpcoes = new JComboBox(mensagem);
		btnFront = new JButton("Frente");
		btnFront.setSize(50, 50);
		btnFront.addActionListener(listener);
		painel.add(btnFront, ParagraphLayout.NEW_PARAGRAPH);
		btnBack = new JButton("Ré");
		btnBack.addActionListener(listener);
		btnBack.setSize(350, 50);
		painel.add(btnBack, ParagraphLayout.NEW_PARAGRAPH);
		btnRight = new JButton("Direita");
		btnRight.setSize(50, 50);
		btnRight.addActionListener(listener);
		painel.add(btnRight, ParagraphLayout.NEW_PARAGRAPH);
		btnLeft = new JButton("Esquerda");
		btnRight.setSize(50, 50);
		btnLeft.addActionListener(listener);
		painel.add(btnLeft);
		btnFecharConexao = new JButton("Fechar Conexão");
		btnFecharConexao.setName("fechar");
		btnFecharConexao.addActionListener(listener);
		painel.add(btnFecharConexao, ParagraphLayout.NEW_PARAGRAPH);
		btnRight.setName("btnRight");
		btnBack.setName("btnBack");
		btnLeft.setName("btnLeft");
		btnFront.setName("btnFront");
	}

	public JComboBox getCmbOpcoes() {
		return cmbOpcoes;
	}

	public void setCmbOpcoes(JComboBox cmbOpcoes) {
		this.cmbOpcoes = cmbOpcoes;
	}

	public static void main(String[] args) {
		new FrameTela();
	}
}
