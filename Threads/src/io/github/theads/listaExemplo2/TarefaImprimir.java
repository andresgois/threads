package io.github.theads.listaExemplo2;

public class TarefaImprimir implements Runnable {

	private Lista lista;

	public TarefaImprimir(Lista lista) {
		this.lista = lista;
	}

	@Override
	public void run() {
		
      try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// só pode imprimir quando o outro estiver pronto
		synchronized (lista) {
			
			if(!lista.estaCheia()) {				
				try {
					System.out.println("Aguardando notificação");
					lista.wait();
				} catch (Exception e) {
					System.out.println("Error: "+e.getMessage());
				}
			}

			for(int i = 0; i <lista.tamanho(); i++) {
				System.out.println(i+" - "+lista.pegaElemento(i));
			}
		}
	}
}
