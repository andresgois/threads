package br.com.porject.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class DistribuirTarefasComRetornoAoCliente implements Runnable {

    private Socket socket;

    public DistribuirTarefasComRetornoAoCliente(Socket socket) {
        this.socket = socket;
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

