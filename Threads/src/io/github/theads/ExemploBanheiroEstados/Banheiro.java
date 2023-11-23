package io.github.theads.ExemploBanheiroEstados;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Banheiro {
	
	private Lock lock = new ReentrantLock();
	private boolean ehSujo = true;

	public void fazNumero1() {

	    String nome = Thread.currentThread().getName();
	    
	    System.out.println(nome+ " batendo na porta");

	    synchronized (this) {

	        System.out.println(nome + " entrando no banheiro");
	        while (this.ehSujo) { //novo, trocando if com while
	            esperaLaFora(nome);
	        }
	        System.out.println(nome + " fazendo coisa rápida");
	        dormindoUmPouco(5000);
	        
	        this.ehSujo = true;

	        System.out.println(nome + " dando descarga");
	        System.out.println(nome + " lavando a mao");
	        System.out.println(nome + " saindo do banheiro");
	    }
	}

	public void fazNumero2() {
	    String nome = Thread.currentThread().getName();
	    System.out.println(nome+ " batendo na porta");

	    synchronized (this) {
	        System.out.println(nome + " entrando no banheiro");
	        
	        while (this.ehSujo) { //novo, trocando if com while
	            esperaLaFora(nome);
	        }
	        
	        System.out.println(nome + " fazendo coisa demorada");
	        dormindoUmPouco(10000);
	        this.ehSujo = true;

	        System.out.println(nome + " dando descarga");
	        System.out.println(nome + " lavando a mao");
	        System.out.println(nome + " saindo do banheiro");
	    }
	}

	private void dormindoUmPouco(long millis) {
		try {
		    Thread.sleep(millis);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}

	
	private void esperaLaFora(String nome) {

	    System.out.println(nome + ", eca, banheiro está sujo");
	    try {
	        this.wait();
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
	
	public void limpa() {

	    String nome = Thread.currentThread().getName();
	    System.out.println(nome + " batendo na porta");

	    synchronized (this) {
	        System.out.println(nome + " entrando no banheiro");

	        if (!this.ehSujo) {
	            System.out.println(nome + ", não está sujo, vou sair");
	            return;
	        }

	        System.out.println(nome + " limpando o banheiro");
	        this.ehSujo = false;

	        try {
	            Thread.sleep(13000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        this.notifyAll();

	        System.out.println(nome + " saindo do banheiro");
	    }
	}
	
}
