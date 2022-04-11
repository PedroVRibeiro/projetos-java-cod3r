package campo.minado.cod3r;

import campo.minado.cod3r.modelo.Tabuleiro;
import campo.minado.cod3r.visao.TabuleiroConsole;

// Utilizada para testar o jogo.
public class Aplicacao {

	public static void main(String[] args) {
		
		Tabuleiro tabuleiro = new Tabuleiro (6, 6, 5);
		
		new TabuleiroConsole(tabuleiro);
	}
}
