package campo.minado.cod3r.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

// Implementa a l�gica do tabuleiro, que conter� os campos e acessar� seus m�todos.
public class Tabuleiro implements CampoObservador{

	private final int linhas;
	private final int colunas;
	private final int minas;
	
// Cada campo do tabuleiro � guardado em um ArrayList da classe CAMPO.
	private final List<Campo> campos = new ArrayList<>();
	
//Array respons�vel por armazenar os observadores para os resultados dos eventos (ganhar ou perder).
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

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
	
// Implementa um ForEach, aplicando uma fun��o em cada integrante do array 'campos'.
	public void paraCadaCampo(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	
// * Registro e notifica��o de OBSERVADORES *
	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(boolean resultado) {
		observadores.stream()
			.forEach(o -> o.accept(new ResultadoEvento(resultado)));
	}
	
// * Abertura de campo *
//
// Chama o m�todo de abertura de campo a partir das coordenadas passadas como par�metros.
// � realizado um filtro no array de campos para verificar a exist�ncia do campo pedido.
// Se ele existir, ser� aberto. No caso de explos�o, ser� capturada a exce��o lan�ada.
	public void abrir(int linha, int coluna) {
			campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst()
			.ifPresent(c -> c.abrir());

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
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
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
				Campo campo = new Campo(linha, coluna);
				campo.registrarObservador(this);
				campos.add(campo);
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
	
	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

// Realiza a NOTIFICA��O dos eventos para os observadores.
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if(evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		} else if (objetivoAlcancado()) {
			notificarObservadores(true);
		}
	}
	
// Revela as minas restantes no tabuleiro.
		@SuppressWarnings("unused")
		private void mostrarMinas() {
			campos.stream()
				.filter(c -> c.isMinado())
				.forEach(c -> c.setAberto(true));
		}
	
}
