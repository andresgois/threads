package br.com.porject.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTarefa {

	public static void main(String[] args) throws IOException {
		
		
		try {
			Socket socket = new Socket("localhost", 12345);
			System.out.println("Conexão estabelecida");
			
			Thread threadEnviaComanbdo = new Thread(new Runnable() {
				PrintStream saida = null;
				Scanner teclado = null;
				@Override
				public void run() {
					try {
						saida = new PrintStream(socket.getOutputStream());
						//saida.println("c1");
						
						teclado = new Scanner(System.in);
						//  envia dados para o servidor
						while(teclado.hasNextLine()){
							String linha = teclado.nextLine();
							
							if(linha.trim().equals("")) {
								break;
							}
							
							saida.println(linha);
						}
						saida.close();
						teclado.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					
				}
			});
			
			
			// recebe dados do servidor
			Thread threadRecebeResposta = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						System.out.println("Recebendo dados do servidor");
						Scanner respostaServidor = new Scanner(socket.getInputStream());
						while(respostaServidor.hasNextLine()) {
							String linha = respostaServidor.nextLine();
							System.out.println(linha);
						}
						respostaServidor.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			});
			
			threadEnviaComanbdo.start();
			threadRecebeResposta.start();
			
			// espera a threadRecebeResposta acabar
			threadEnviaComanbdo.join();
			
			
			System.out.println("Fechando thread");
			socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
