package br.com.porject.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServidorTarefa {
	
	private ServerSocket servidor;
	private ExecutorService threadPool;
	private AtomicBoolean estaRodando;
	/*
	 * cada thread nativamente vai ter sua memória e copia os dados para otimizar o acesso
	 * Como posso agora falar para o Java que não quero que ele use esses caches
	 * ara isso existe uma palavra-chave justamente para desabilitar esse cache. Você chama esses atributos que deveriam ser manipulados através de threads na memória principal, e não no cache, você chama esses atributos voláteis, volatile. 
	 * Colocando aqui volatile na frente, palavra-chave do Java, nós desabilitamos esse cache.
	 */

	public ServidorTarefa() throws IOException{
		System.out.println("---- Iniciado o servidor LimitandoNumeroDeThreds-----");
		this.servidor = new ServerSocket(12345);
		this.threadPool = Executors.newCachedThreadPool();
		this.estaRodando = new AtomicBoolean(true);
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		//System.out.println("Threads Normal");
		//thredNormal();
		
		// Aceita 2 e bloqueia o resto
		//System.out.println("Numero de threads fixa");
		//limitandoNumeroDeThreds();
		
		// Número dinâmico de threads
		//numeroDinameicoDeThreds();
		
		
		// Dando resposta ao cliente
		//devolvendoRespostaAoCliente();
		
		entendendoVolatile();
		
	}

	private static void thredNormal() throws IOException {
		System.out.println("---- Iniciado o servidor  ThredNormal-----");
		
		ServerSocket servidor = new ServerSocket(12345);
		
		while (true) {
			Socket socket = servidor.accept();    
            System.out.println("Aceitando novo cliente na porta " + socket.getPort());
            DistribuirTarefas distribuirTarefas = new DistribuirTarefas(socket);
            new Thread(distribuirTarefas).start();
		}
	}
	
	private static void limitandoNumeroDeThreds() throws IOException {
		System.out.println("---- Iniciado o servidor LimitandoNumeroDeThreds-----");
		ServerSocket servidor = new ServerSocket(12345);
		
		// limitando o número de threads | Pool de threads
		ExecutorService threadPool = Executors.newFixedThreadPool(2);
	
		while (true) {
			Socket socket = servidor.accept();    
            System.out.println("Aceitando novo cliente na porta " + socket.getPort());
            DistribuirTarefas distribuirTarefas = new DistribuirTarefas(socket);
            threadPool.execute(distribuirTarefas);
		}
	}

	private static void numeroDinameicoDeThreds() throws IOException {
		System.out.println("---- Iniciado o servidor LimitandoNumeroDeThreds-----");
		ServerSocket servidor = new ServerSocket(12345);
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
	
		while (true) {
			Socket socket = servidor.accept();    
            System.out.println("Aceitando novo cliente na porta " + socket.getPort());
            DistribuirTarefas distribuirTarefas = new DistribuirTarefas(socket);
            threadPool.execute(distribuirTarefas);
		}
	}

	private static void devolvendoRespostaAoCliente() throws IOException {
		System.out.println("---- Iniciado o servidor LimitandoNumeroDeThreds-----");
		ServerSocket servidor = new ServerSocket(12345);
		ExecutorService threadPool = Executors.newCachedThreadPool();
	
		while (true) {
			Socket socket = servidor.accept();    
            System.out.println("Aceitando novo cliente na porta " + socket.getPort());
            DistribuirTarefasComRetornoAoCliente distribuirTarefas = 
            		new DistribuirTarefasComRetornoAoCliente(socket);
            threadPool.execute(distribuirTarefas);
		}
		
	}
	
	private static void entendendoVolatile() throws IOException {
		ServidorTarefa servidor = new ServidorTarefa();
		servidor.rodar();
		servidor.parar();
	}

	public void parar() throws IOException {
		this.estaRodando.set(false);
		servidor.close();
		threadPool.shutdown();
	}

	public void rodar() throws IOException {
		while (this.estaRodando.get()) {
			try {
				Socket socket = servidor.accept();
				System.out.println("Aceitando novo cliente na porta " + socket.getPort());
				DistribuirTarefasComIntercaoComServidor distribuirTarefas = 
						new DistribuirTarefasComIntercaoComServidor(socket, this);
				threadPool.execute(distribuirTarefas);
			} catch (SocketException e) {
				System.out.println("SocketException, está rodando? "+this.estaRodando);
			}
		}
	}
}
