package campo.minado.cod3r.modelo;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import campo.minado.cod3r.excecao.ExplosaoException;

public class CampoTeste {

	private Campo campo;
	
	@BeforeEach
	void iniciarCampo() {
		campo = new Campo(3, 3);
	}
	
	@Test
	void testeVizinhoDistancia1Esquerda() {
		Campo vizinho = new Campo(3, 2);		
		boolean resultado = campo.adicionarVizinho(vizinho);		
		assertTrue(resultado);
	}
	
	@Test
	void testeVizinhoDistancia1Direita() {
		Campo vizinho = new Campo(3, 4);		
		boolean resultado = campo.adicionarVizinho(vizinho);		
		assertTrue(resultado);
	}
	
	@Test
	void testeVizinhoDistancia1EmCima() {
		Campo vizinho = new Campo(2, 3);		
		boolean resultado = campo.adicionarVizinho(vizinho);		
		assertTrue(resultado);
	}
	
	@Test
	void testeVizinhoDistancia1EmBaixo() {
		Campo vizinho = new Campo(4, 3);		
		boolean resultado = campo.adicionarVizinho(vizinho);		
		assertTrue(resultado);
	}
	
	@Test
	void testeVizinhoDistancia2EsquerdaEmCima() {
		Campo vizinho = new Campo(2, 2);
		boolean resultado = campo.adicionarVizinho(vizinho);		
		assertTrue(resultado);
	}
	
	@Test
	void testeVizinhoDistancia2DireitaEmCima() {
		Campo vizinho = new Campo(2, 4);
		boolean resultado = campo.adicionarVizinho(vizinho);		
		assertTrue(resultado);
	}
	
	@Test
	void testeVizinhoDistancia2EsquerdaEmBaixo() {
		Campo vizinho = new Campo(4, 2);
		boolean resultado = campo.adicionarVizinho(vizinho);		
		assertTrue(resultado);
	}
	
	@Test
	void testeVizinhoDistancia2EmBaixo() {
		Campo vizinho = new Campo(4, 4);
		boolean resultado = campo.adicionarVizinho(vizinho);		
		assertTrue(resultado);
	}
	
	@Test
	void testeNaoVizinho() {
		Campo vizinho = new Campo(1, 1);		
		boolean resultado = campo.adicionarVizinho(vizinho);		
		assertFalse(resultado);
	}
	
	@Test
	void testeValorPadraoAtributoMarcado() {
		assertFalse(campo.isMarcado());
	}
	
	@Test
	void testeAlternarMarcacao() {
		campo.alternarMarcacao();
		assertTrue(campo.isMarcado());
	}
	
	@Test
	void testeAlternarMarcacaoDuasChamadas() {
		campo.alternarMarcacao();
		campo.alternarMarcacao();
		assertFalse(campo.isMarcado());
	}
	
	@Test
	void testeEstaMinado() {
		campo.minar();
		campo.isMinado();
	}
	
	@Test 
	void receberLinha() {
		campo.getLinha();
	}
	
	@Test 
	void receberColuna() {
		campo.getColuna();
	}
	
	@Test
	void testeAbrirNaoMinadoNaoMarcado() {
		assertTrue(campo.abrir());
	}
	
	@Test
	void testeAbrirNaoMinadoMarcado() {
		campo.alternarMarcacao();
		assertFalse(campo.abrir());
	}
	
	@Test
	void testeAbrirMinadoMarcado() {
		campo.alternarMarcacao();
		campo.minar();
		assertFalse(campo.abrir());
	}
	
	@Test
	void testeAbrirMinadoNaoMarcado() {
		campo.minar();
		
		assertThrows(ExplosaoException.class, () -> {
			campo.abrir();
		});
	}
	
	@Test
	void testeAbrirComVizinhos1() {
		
		Campo campo11 = new Campo(1, 1);
		Campo campo22 = new Campo(2, 2);
		
		campo22.adicionarVizinho(campo11);
		
		campo.adicionarVizinho(campo22);
		campo.abrir();
		
		assertTrue(campo22.isAberto() && campo11.isAberto());
	}
	
	@Test
	void testeAbrirComVizinhos2() {
		
		Campo campo11 = new Campo(1, 1);
		Campo campo12 = new Campo(1, 2);
		campo12.minar();

		Campo campo22 = new Campo(2, 2);
		
		campo22.adicionarVizinho(campo11);
		campo22.adicionarVizinho(campo12);
		
		campo.adicionarVizinho(campo22);
		campo.abrir();
		
		assertTrue(campo22.isAberto() && campo11.isFechado());
	}
	
	@Test
	void testeMinasVizinhas() {
		
		Campo campo23 = new Campo(2, 3);
		Campo campo24 = new Campo(2, 4);
		campo23.minar();
		campo24.minar();

		Campo campo22 = new Campo(2, 2);
		
		campo.adicionarVizinho(campo22);
		campo.adicionarVizinho(campo23);
		campo.adicionarVizinho(campo24);
		
		assertTrue(campo.minasNaVizinhanca() == 2);
		
	}
	
	@Test
	void ObjetivoAlcancado() {
		Campo campo22 = new Campo(2, 2);
		campo22.minar();
		campo22.alternarMarcacao();
		
		campo.abrir();
		
		assertTrue(campo.objetivoAlcancado() && campo22.objetivoAlcancado());
	}
	
	@Test
	void testeReiniciar() {
		campo.reiniciar();
		assertFalse(campo.isAberto() && campo.isMinado() && campo.isMarcado());
	}
}
