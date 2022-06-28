package calculadora.cod3r.visao;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Botao extends JButton {

	public Botao(String texto, Color cor) {
		setText(texto);
		setBackground(cor);
		setOpaque(true);
		setFont(new Font("courier", Font.PLAIN, 25));
		setForeground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
}
