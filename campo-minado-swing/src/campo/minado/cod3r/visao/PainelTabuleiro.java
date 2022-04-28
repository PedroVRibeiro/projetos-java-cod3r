package campo.minado.cod3r.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import campo.minado.cod3r.modelo.Tabuleiro;

// Classe respons�vel pelo GRID do tabuleiro, adicionando um bot�o para cada campo gerado.

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel{

	public PainelTabuleiro(Tabuleiro tabuleiro) {
	
		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
		
		tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));
		
		tabuleiro.registrarObservador(e -> {
			SwingUtilities.invokeLater(() -> {
				if(e.isGanhou()) {
					JOptionPane.showMessageDialog(this, "Ganhou!");
				} else {
					JOptionPane.showMessageDialog(this, "Perdeu...");
				}
				
				tabuleiro.reiniciar();
			});
		});
	}
}
