package br.com.porject.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class DistribuirTarefasComIntercaoComServidor implements Runnable {

    private Socket socket;
	private ServidorTarefa servidor;

    public DistribuirTarefasComIntercaoComServidor(Socket socket, ServidorTarefa servidor) {
        this.socket = socket;
		this.servidor = servidor;
    }

    @Override
    public void run() {
        System.out.println("Distribuindo as tarefas para o cliente " +  socket);

        try {
			Scanner entradaCliente = new Scanner(socket.getInputStream());
			
			PrintStream saidaCliente = new PrintStream(socket.getOutputStream());
			
			
			while (entradaCliente.hasNextLine()) {
				String comando = entradaCliente.nextLine();
				System.out.println("Comando recebido do cliente: "+comando);
				
				switch (comando) {
				case "c1": {
					System.out.println("Confirmando comando C1");
					break;
				}
				case "c2": {
					System.out.println("Confirmando comando C2");
					break;
				}
				case "fim": {
					System.out.println("Finalizando conexão");
					this.servidor.parar();
					break;
				}
				default:
					System.out.println("Comando não encontrado");
					//throw new IllegalArgumentException("Unexpected value: " + comando);
				}
				
			}

            Thread.sleep(20000);
            saidaCliente.close();
            entradaCliente.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

