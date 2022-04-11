package campo.minado.cod3r.visao;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import campo.minado.cod3r.excecao.ExplosaoException;
import campo.minado.cod3r.excecao.SairException;
import campo.minado.cod3r.modelo.Tabuleiro;

// Implementa a visualização do jogo em CONSOLE e recebe as instruções do jogador.
public class TabuleiroConsole {

	private Tabuleiro tabuleiro;
	private Scanner entrada = new Scanner(System.in);

	public TabuleiroConsole(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
		
		executarJogo();
	}
	
// Dá início ao jogo, executando seu ciclo até que o jogador vença, perca ou deseje sair.
// Caso o jogador deseje jogar outra partida, é reiniciado o tabuleiro.
	private void executarJogo() {
		try {
			boolean continuar = true;
			
			while(continuar) {
				cicloDoJogo();
				
				System.out.println("Deseja jogar outra partida? (S/n) ");
				String resposta = entrada.nextLine();
				
				if("n".equalsIgnoreCase(resposta)) {
					continuar = false;
					System.out.println("Você saiu do jogo.");
				} else {
					tabuleiro.reiniciar();
				}
			}
		} catch (SairException e) {
			System.out.println("Você saiu do jogo.");
		} finally {
			entrada.close();
		}
	}

// Executa o ciclo do jogo, enquanto todos os campos não estiverem na condição de "objetivo alcançado",
// Mostrando o tabuleiro e recebendo uma nova instrução a cada rodada, abrindo um campo ou o marcando.
// É utilizado um ITERATOR para receber as coordenadas, separadas em valores individuais e convertidas em inteiros.
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
			System.out.println("Você ganhou!");
		} catch(ExplosaoException e) {
			System.out.println(tabuleiro);
			System.out.println("Você perdeu!");
		}
	}
	
// Recebe o valor digitado e o informa.
// Lança exceção de SAíDA caso o jogador deseje encerrar o jogo.
	private String capturarValorDigitado(String texto) {
		System.out.print(texto);
		String digitado = entrada.nextLine();
		
		if("sair".equalsIgnoreCase(digitado)) {
			throw new SairException();
		}
		
		return digitado;
	}
	
}
