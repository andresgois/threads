package io.github.theads;

public class Principal {

	public static void main(String[] args) {
		
		System.err.println("Isso é uma thread");
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
