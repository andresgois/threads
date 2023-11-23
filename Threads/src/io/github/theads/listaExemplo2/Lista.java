package io.github.theads.listaExemplo2;

public class Lista {
    private String[] elementos = new String[100];
    private int indice = 0;

    public synchronized void adicionaElementos(String elemento) {
        this.elementos[indice] = elemento;
        this.indice++;
        
        // ctrl + shift + c comenta o código
//        try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
        
        // Notifica a thread após Aadicionar os elementos
        if(this.indice == this.elementos.length) {
        	System.out.println("Lista cheia, notificando");
        	this.notify();
        }
    } 

    public int tamanho() {
        return this.elementos.length;
    }

    public String pegaElemento(int posicao) {
        return this.elementos[posicao];
    }

	public boolean estaCheia() {
		return this.indice == this.tamanho();
	}
}
