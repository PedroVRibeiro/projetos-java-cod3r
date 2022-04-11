package campo.minado.cod3r.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import campo.minado.cod3r.excecao.ExplosaoException;

// Implementa a l�gica do tabuleiro, que conter� os campos e acessar� seus m�todos.
public class Tabuleiro {

	private int linhas;
	private int colunas;
	private int minas;
	
// Cada campo do tabuleiro � guardado em um ArrayList da classe CAMPO.
	private final List<Campo> campos = new ArrayList<>();

// Na cria��o do tabuleiro, s�o gerados os campos, feitas as associa��es de vizinhos entre eles e
// neles s�o sorteadas as minas.
	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	
// * Abertura de campo *
//
// Chama o m�todo de abertura de campo a partir das coordenadas passadas como par�metros.
// � realizado um filtro no array de campos para verificar a exist�ncia do campo pedido.
// Se ele existir, ser� aberto. No caso de explos�o, ser� capturada a exce��o lan�ada.
	public void abrir(int linha, int coluna) {
		try {
			campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst()
			.ifPresent(c -> c.abrir());
		} catch(ExplosaoException e) {
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}
	}
	
// * Marca��o de campo *
//
// Mesma l�gica da abertura de campo, por�m, apenas marcando o campo selecionado como potencialmente minado.
	public void alternarMarcacao(int linha, int coluna) {
		campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst()
			.ifPresent(c -> c.alternarMarcacao());
	}

// * Sorteio de minas *
//
// Em cada la�o do/while gera um �ndice aleat�rio, entre 0 e o valor da quantidade de campos.
// O campo correnspondente ao �ndice gerado � ent�o preenchido com uma mina, e o processo se repetir�
// at� que a quantidade de campos minados n�o corresponda ao total de minas definido no atributo "minas".
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		
		do {
			int aleatorio = (int) (Math.random() * campos.size());
			minasArmadas = campos.stream().filter(minado).count();
			campos.get(aleatorio).minar();
		} while(minasArmadas < minas);
	}

// * Associa��o de vizinhos *
//
// O ForEach percorre o ArrayList, gerando associa��es de vizinhan�a entre todos os campos e seus campos adjacentes.
	private void associarVizinhos() {
		for(Campo c1: campos) {
			for(Campo c2: campos)  {
				c1.adicionarVizinho(c2);
			}
		}
	}

// * Gerar campos
//
// Adiciona um campo no ArrayList para cada par de coordenadas linha/coluna.
	private void gerarCampos() {
		for(int linha = 0; linha < linhas; linha++) {
			for(int coluna = 0; coluna < colunas; coluna++) {
				campos.add(new Campo(linha, coluna));
			}
		}
	}
	
// Verifica se todos os campos est�o na condi��o de "objetivo alcan�ado".
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}	
	
// Reinicia cada campo e, por consequ�ncia, o jogo.
	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}
	
// Utiliza-se o StringBuilder para constru��o da VISUALIZA��O do TABULEIRO.
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("   ");
		for(int coluna = 0; coluna < colunas; coluna++) {
			sb.append(coluna);
			sb.append(" ");
		}
		
		sb.append("\n");
		
		int i = 0;
		
		for(int linha = 0; linha < linhas; linha++) {
			sb.append(linha);
			sb.append(" ");
			for(int coluna = 0; coluna < colunas; coluna++) {
				sb.append("|");
				sb.append(campos.get(i));
				sb.append("");
				i++;
			}
			sb.append("|\n");
		}
		
		return sb.toString();
	}
	
}
