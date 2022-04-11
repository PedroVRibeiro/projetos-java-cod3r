package campo.minado.cod3r.visao;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import campo.minado.cod3r.excecao.ExplosaoException;
import campo.minado.cod3r.excecao.SairException;
import campo.minado.cod3r.modelo.Tabuleiro;

// Implementa a visualiza��o do jogo em CONSOLE e recebe as instru��es do jogador.
public class TabuleiroConsole {

	private Tabuleiro tabuleiro;
	private Scanner entrada = new Scanner(System.in);

	public TabuleiroConsole(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
		
		executarJogo();
	}
	
// D� in�cio ao jogo, executando seu ciclo at� que o jogador ven�a, perca ou deseje sair.
// Caso o jogador deseje jogar outra partida, � reiniciado o tabuleiro.
	private void executarJogo() {
		try {
			boolean continuar = true;
			
			while(continuar) {
				cicloDoJogo();
				
				System.out.println("Deseja jogar outra partida? (S/n) ");
				String resposta = entrada.nextLine();
				
				if("n".equalsIgnoreCase(resposta)) {
					continuar = false;
					System.out.println("Voc� saiu do jogo.");
				} else {
					tabuleiro.reiniciar();
				}
			}
		} catch (SairException e) {
			System.out.println("Voc� saiu do jogo.");
		} finally {
			entrada.close();
		}
	}

// Executa o ciclo do jogo, enquanto todos os campos n�o estiverem na condi��o de "objetivo alcan�ado",
// Mostrando o tabuleiro e recebendo uma nova instru��o a cada rodada, abrindo um campo ou o marcando.
// � utilizado um ITERATOR para receber as coordenadas, separadas em valores individuais e convertidas em inteiros.
	private void cicloDoJogo() {
		try {
			
			while(!tabuleiro.objetivoAlcancado()) {
				System.out.println("Escolha um campo, ou digite SAIR para encerrar a partida.");
				System.out.println(tabuleiro);
				
				String digitado = capturarValorDigitado("Digite (x, y): ");
				
				Iterator<Integer> xy =  Arrays.stream(digitado.split(","))
					.map(e -> Integer.parseInt(e.trim()))
					.iterator();
				
				digitado = capturarValorDigitado("1 - Abrir ou 2 - Marcar/Desmarcar, ou digite SAIR para encerrar: \n");
				
				if("1".equals(digitado)) {
					tabuleiro.abrir(xy.next(), xy.next());
				} else if("2".equals(digitado)) {
					tabuleiro.alternarMarcacao(xy.next(), xy.next());
				}
						
			}
			
			System.out.println(tabuleiro);
			System.out.println("Voc� ganhou!");
		} catch(ExplosaoException e) {
			System.out.println(tabuleiro);
			System.out.println("Voc� perdeu!");
		}
	}
	
// Recebe o valor digitado e o informa.
// Lan�a exce��o de SA�DA caso o jogador deseje encerrar o jogo.
	private String capturarValorDigitado(String texto) {
		System.out.print(texto);
		String digitado = entrada.nextLine();
		
		if("sair".equalsIgnoreCase(digitado)) {
			throw new SairException();
		}
		
		return digitado;
	}
	
}
