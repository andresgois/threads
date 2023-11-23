package io.github.theads.simulandoPoolDeConexao;

public class TarefaAcessaBancoProcedimento implements Runnable {

	private PoolDeConexao pool;
	private GerenciadorDeTransacao tx;

	public TarefaAcessaBancoProcedimento(PoolDeConexao pool, GerenciadorDeTransacao tx) {
		this.pool = pool;
		this.tx = tx;
	}

	@Override
	public void run() {
		// dentro do método run da classe TarefaAcessaBancoProcedimento
		synchronized (tx) { 

		    System.out.println("Peguei a chave da tx");
		    pool.getConnection();

		    synchronized (tx) { 

		        System.out.println("Peguei a chave da tx");
		        tx.begin();
		    }
		}
	}

}
