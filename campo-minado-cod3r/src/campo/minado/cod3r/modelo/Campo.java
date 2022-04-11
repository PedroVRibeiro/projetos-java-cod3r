package campo.minado.cod3r.modelo;

import java.util.ArrayList;
import java.util.List;

import campo.minado.cod3r.excecao.ExplosaoException;

// Implementa a lógica referente a CADA CAMPO presente no tabuleiro, com seus conteúdos.
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

// * Método que verifica a posição dos campos e seus possíveis vizinhos. *
//
// São verificadas as diferenças dos índices dos atributos de linha e coluna para cada campo e seus valores absolutos.
// Havendo diferenças e sendo a diferença igual a 1, temos um vizinho na vertical ou horizonta; sendo igual a 2, 
// temos um vizinho na diagonal. Diferenças maiores indicam que o campo não é vizinho, não sendo adicionado ao array de vizinhos. 
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
	
// Alterna a marcação de um campo, para informar que este é potencialmente uma MINA.
	void alternarMarcacao() {
		if(!aberto) {
			marcado = !marcado;
		}
	}
	
// * Método para abrir um campo *
// 
// Cada campo só pode ser aberto se já não o estiver e se não estiver marcado como mina. 
// Se, após a abertura, o campo estiver minado, é lançada uma exceção referente à explosão para parar o programa.
// Se não houver mina, executa-se o método "vizinhancaSegura", que abrirá os vizinhos daquele campo.
// O método vizinhança segura será executado RECURSIVAMENTE, enquanto não houver minas adjacentes nos vizinhos.
// Ao chegar no ponto em que houver pelo menos uma mina em um vizinho, o método não será executado e o vizinho ficará
// marcado com o número correspondente de minas ao redor.
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
		
// * Método para verificar segurança dos campos adjacentes *
//
// Utiliza stream para verificar se a vizinhança imediata do campo não apresenta minas.
// Se houver PELO MENOS UMA mina nos campos adjacentes a vizinhança não será considerada segura.
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
	
// * Método para verificar ESTADO de um campo *
//
// O objetivo de um campo é considerado alcançado se ele for desvendado (não havendo mina nele)
// ou se ele estiver marcado como possuindo mina (e efetivamente houver).
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}
	
// * Método para contagem de campos MINADOS *
//
// Conta quantos campos vizinhos estão minados, retornado o valor.
	long minasNaVizinhanca() {
		return vizinhos.stream().filter(v -> v.minado).count();
	}
	
// Retorna o estado dos atributos de cada campo para o estado inicial.
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
	}
	
// * Método para VISUALIZAÇÃO do ESTADO de cada campo *
//
// Imprime em cada campo seu estado atual, seja MARCADO, com EXPLOSÃO, FECHADO, e ABERTO, caso no qual será
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
