package campo.minado.cod3r.modelo;

import java.util.ArrayList;
import java.util.List;

import campo.minado.cod3r.excecao.ExplosaoException;

// Implementa a l�gica referente a CADA CAMPO presente no tabuleiro, com seus conte�dos.
public class Campo {

	private final int linha;
	private final int coluna;
	
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;
	
// Cada campo possui como atributo um ArrayList da classe CAMPO que informa seus campos vizinhos.
	private List<Campo> vizinhos = new ArrayList<>();
	
	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

// * M�todo que verifica a posi��o dos campos e seus poss�veis vizinhos. *
//
// S�o verificadas as diferen�as dos �ndices dos atributos de linha e coluna para cada campo e seus valores absolutos.
// Havendo diferen�as e sendo a diferen�a igual a 1, temos um vizinho na vertical ou horizonta; sendo igual a 2, 
// temos um vizinho na diagonal. Diferen�as maiores indicam que o campo n�o � vizinho, n�o sendo adicionado ao array de vizinhos. 
	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaLinha + deltaColuna;
		
		if(deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if(deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}
	}
	
// Alterna a marca��o de um campo, para informar que este � potencialmente uma MINA.
	void alternarMarcacao() {
		if(!aberto) {
			marcado = !marcado;
		}
	}
	
// * M�todo para abrir um campo *
// 
// Cada campo s� pode ser aberto se j� n�o o estiver e se n�o estiver marcado como mina. 
// Se, ap�s a abertura, o campo estiver minado, � lan�ada uma exce��o referente � explos�o para parar o programa.
// Se n�o houver mina, executa-se o m�todo "vizinhancaSegura", que abrir� os vizinhos daquele campo.
// O m�todo vizinhan�a segura ser� executado RECURSIVAMENTE, enquanto n�o houver minas adjacentes nos vizinhos.
// Ao chegar no ponto em que houver pelo menos uma mina em um vizinho, o m�todo n�o ser� executado e o vizinho ficar�
// marcado com o n�mero correspondente de minas ao redor.
	boolean abrir() {
		
		if(!aberto && !marcado) {
			aberto = true;
			
			if(minado) {
				throw new ExplosaoException();
			}
			
			if(vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
			
			return true;
		} else {
			return false;
		}
	}
		
// * M�todo para verificar seguran�a dos campos adjacentes *
//
// Utiliza stream para verificar se a vizinhan�a imediata do campo n�o apresenta minas.
// Se houver PELO MENOS UMA mina nos campos adjacentes a vizinhan�a n�o ser� considerada segura.
	boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
// Define o campo como minado, alterando o estado do atributo.
	void minar() {
		minado = true;
	}
	
	public boolean isMinado() {
		return minado;
	}

	public boolean isMarcado() {
		return marcado;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
	}

	public boolean isAberto() {
		return aberto;
	}
	
	public boolean isFechado() {
		return !isAberto();
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}
	
// * M�todo para verificar ESTADO de um campo *
//
// O objetivo de um campo � considerado alcan�ado se ele for desvendado (n�o havendo mina nele)
// ou se ele estiver marcado como possuindo mina (e efetivamente houver).
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}
	
// * M�todo para contagem de campos MINADOS *
//
// Conta quantos campos vizinhos est�o minados, retornado o valor.
	long minasNaVizinhanca() {
		return vizinhos.stream().filter(v -> v.minado).count();
	}
	
// Retorna o estado dos atributos de cada campo para o estado inicial.
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
	}
	
// * M�todo para VISUALIZA��O do ESTADO de cada campo *
//
// Imprime em cada campo seu estado atual, seja MARCADO, com EXPLOS�O, FECHADO, e ABERTO, caso no qual ser�
// impressa a contagem de minhas adjacentes.
	public String toString() {
		if(marcado) {
			return "X";
		} else if(aberto && minado) {
			return "*";
		} else if(aberto && minasNaVizinhanca() > 0) {
			return Long.toString(minasNaVizinhanca());
		} else if(aberto) {
			return " ";
		} else {
			return "?";
		}
		
	}
}
