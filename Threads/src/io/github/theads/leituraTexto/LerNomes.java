package io.github.theads.leituraTexto;

public class LerNomes {

	public static void main(String[] args) {
		String nome = "da";
		
		Thread tr1 = new Thread(new TarefaBuscaTextual("assinaturas1.txt", nome));
		Thread tr2 = new Thread(new TarefaBuscaTextual("assinaturas2.txt", nome));
		Thread tr3 = new Thread(new TarefaBuscaTextual("autores.txt", nome));
		
		tr1.start();
		tr2.start();
		tr3.start();
		
		// Crie um programa com duas threads que imprimem números de 1 até 1000, além da ID da thread.
		Thread threadAtual = Thread.currentThread();
		long id = threadAtual.getId();
		System.out.println("Id da thread: " + id);
	}

}
