# Threads em Java 2: programação concorrente avançada

- [O projeto Servidor de tarefas](#anc1)
- [Reuso de threads](#anc2)
- [Melhorando o cliente](#anc3)
- [Entendendo Volatile](#anc4)
- [Distribuindo comandos e tratamento de erro](#anc5)
- [Retornos no Futuro](#anc6)
- [Produzindo e Consumindo com Threads](#anc7)



## O projeto Servidor de tarefas

<a name="anc1"></a>

- Nele veremos as classes e interfaces principais do pacote java.util.concurrent

### O projeto Servidor de tarefas

- Para continuarmos a aprender mais sobre as threads da JVM, escolhemos um projeto prático onde introduzimos passo a passo novos recursos sobre threads. O objetivo é criar um servidor onde podemos submeter tarefas a executar. O servidor pode ou não confirmar o recebimento das tarefas e, claro, deve executá-las em paralelo. 

![Server 1](../img_readme/thread2_server_client.png)

### Socket e TCP/IP
- Por conta da necessidade de dois computadores se comunicarem, surgiram diversos protocolos que permitissem tal troca de informação. O protocolo que vamos usar aqui é o TCP (Transmission Control Protocol*).

- Através do TCP, é possível criar um fluxo entre dois ou mais computadores - como é mostrado no diagrama abaixo:

É possível conectar mais de um cliente ao mesmo servidor, como é o caso de diversos banco de dados, servidores web, servidores de e-mail ou ftp, etc.

Ao escrever um programa em Java que se comunique com outra aplicação, não é necessário se preocupar com um nível tão baixo quanto o protocolo. As classes que trabalham com eles já foram disponibilizadas para serem usadas por nós no pacote java.net.

A vantagem de se usar o TCP, em vez de criar nosso próprio protocolo de bytes, é que o TCP vai garantir a entrega dos pacotes que transferimos e criar um protocolo base para isto é algo bem complicado.

Por outro lado, o TCP não é um protocolo de aplicação e sim de transporte. Isso significa que não é preciso se preocupar em como os dados serão transmitidos. O TCP garante que os dados serão transmitidos de maneira confiável, mas não se preocupa com o significado desses dados.

Isso é tarefa do protocolo de aplicação. Através dele, dependendo do protocolo, como o HTTP ou o FTP, podemos então definir que queremos acessar um arquivo no servidor, enviar parâmetros de pesquisa ou submeter dados de um formulário.

Em outras palavras, o TCP garante que os dados são transmitidos e o protocolo de aplicação define o significado desses dados.

Abrindo Portas
Nosso objetivo é estabelecer uma conexão e já mencionamos que diversos clientes podem se conectar a um só servidor. Cada cliente vai manter uma conexão com o servidor, mas como o servidor saberá distinguir entre os clientes?

Assim como existe o IP para identificar uma máquina, a porta é a solução para identificar diversos clientes em uma máquina. Esta porta é um número de 2 bytes, varia de 0 a 65535. Se todas as portas de uma máquina estiverem ocupadas, não é possível se conectar a ela enquanto nenhuma for liberada. Então, além do IP, também é preciso saber a porta!

O que é um Socket?
Já sabemos que vamos utilizar o TCP e que precisamos do IP da máquina servidora e a porta. Todos esses detalhes do protocolo são abstraídos no mundo Java através de um socket. Um socket é o ponto-final de um fluxo de comunicação entre duas aplicações, através de uma rede. É exatamente isso que estamos procurando!

![Server 2](../img_readme/thread2_server_client2.png)

> Vimos como aceitar um cliente através de um ServerSocket.
```
ServerSocket servidor = new ServerSocket(12345);
Socket socket = servidor.accept();COPIAR CÓDIGO
```
- O que podemos dizer sobre o método accept()?

	- É bloqueante e trava a thread.	


## Reuso de threads

<a name="anc2"></a>


### Reaproveitando threads

- Já estabelecemos uma conexão entre cliente e servidor. A implementação é ainda bem simples, mas à medida que avançarmos no treinamento, vamos introduzir mais complexidade.

- O nosso foco neste treinamento na verdade não são os sockets, mas sim as threads! Criamos esse exemplo para ter uma aplicação real e poder aplicar novos recursos sobre threads. E é exatamente isso que vamos fazer agora.

- Reparem que criamos para cada novo cliente, uma nova thread dedicada. Se temos 10 clientes, vamos ter 10 threads, proporcionalmente. Como aprendemos, uma thread é mapeada para uma thread nativa do sistema operacional. Isso tem o seu custo, nós sempre devemos ter cuidado e pensar antecipadamente quantas threads a nossa aplicação pode criar para melhorar o uso dos recursos.

- Felizmente o Java já vem preparado para reaproveitar as threads através de um pool. Um pool nada mais é do que um gerenciador de objetos. Ele possui um limite de objetos que podemos estabelecer. Além disso, podemos reaproveitar esses objetos! Quem conhece um pool de conexões do mundo de banco de dados, é exatamente isso que queremos utilizar para o mundo de threads.

- Para usar um pool de threads, devemos utilizar a classe Executors. Ela possui vários métodos estáticos para criar o pool específico. No exemplo, através do método newFixedThreadPool criaremos um pool com uma quantidade de threads pré-definidas:

```
ExecutorService poolDeThreads = Executors.newFixedThreadPool(5); 
```

### Testando o ExecutorService
- Através do nosso ExecutorService podemos atender no máximo 2 clientes. Se precisarmos de mais uma thread, o service bloqueia a execução e espera até que um outro cliente devolva uma thread.

- Quando rodarmos o nosso servidor e conectarmos mais de dois clientes, repare que o terceiro cliente fica bloqueado e a saída não aparece no console do servidor.

- Como já falamos, quando não sabemos direito qual é o valor máximo ideal de threads, podemos utilizar o CachedThreadPool:

```
ExecutorService threadPool = Executors.newCachedThreadPool();
```
![Socker](../img_readme/threads_socket.png)

> Um pool de threads é um gerenciador de objetos do tipo Thread. Ele gerencia uma quantidade de threads estabelecida, que fica aguardando por tarefas fornecidas pelos clientes. A sua grande vantagem é que além de controlarmos a quantidade de threads disponível para uso dos clientes, também podemos fazer o reuso de threads por clientes diferentes, não tendo o gasto de CPU de criar uma nova thread para cada cliente que chega no servidor.

### Tipos de pool's de threads 
- A **newFixedThreadPool** é o pool de threads em que definimos previamente a quantidade de threads que queremos utilizar. Assim, se por exemplo estabelecermos que queremos no máximo 4 threads, este número nunca será extrapolado e elas serão reaproveitadas.

- A **newCachedThreadPool** é o pool de threads que cresce dinamicamente de acordo com as solicitações. É ideal quando não sabemos o número exato de quantas threads vamos precisar. O legal deste pool é que ele também diminuí a quantidade de threads disponíveis quando uma thread fica ociosa por mais de 60 segundos.

- A **newSingleThreadExecutor** é o pool de threads que só permite uma única thread.

### As interfaces do Pool de threads
- Todas as implementações tem em comum que eles implementam a mesma interface: [link do JavaDoc](https://docs.oracle.com/javase/10/docs/api/java/util/concurrent/ExecutorService.html)

- interface ExecutorService estende a interface Executor.

- Existe mais uma interface, a ScheduledExecutorService que por sua vez estende o ExecutorService.

![Executor](../img_readme/executors.png)

- Também temos uma implementação dela já pronta para usar:

```
Runnable tarefa = ....;
ScheduledExecutorService pool = Executors.newScheduledThreadPool(4);
```
- Através desse pool podemos agendar e executar uma tarefa periodicamente, por exemplo:

```
pool.scheduleAtFixedRate(tarefa, 0, 60, TimeUnit.MINUTES); 
```
- executamos uma tarefa a cada 60 minutos

## Melhorando o cliente

<a name="anc3"></a>

### Capturando a entrada
- Para capturar a entrada, vamos utilizar o nosso teclado que já temos em mãos. Ou seja, através do Scanner, vamos ler os comandos do teclado e essa entrada enviaremos para o servidor:

### Lendo dados do Servidor
- O nosso servidor também pode devolver dados para o cliente, por exemplo, a confirmação do comando ou algum resultado de um comando submetido. Para podermos receber os dados, devemos utilizar o InputStream do nosso cliente:

### Usando threads no cliente
- Agora não há muitas novidades e já vimos isso em outros exemplos. Vamos separar o recebimento e o envio dos dados, cada um em uma thread. Mãos à obra!

- Poderíamos criar classes separadas para cada tarefa (Runnable), mas vamos usar um atalho, que é criar duas classes anônimas. Repare no código abaixo que já instanciamos o Runnable na criação da thread:

### Juntando as Threads
- thread main vai esperar 
```
threadEnviaComandos.join();
```
- Quando a thread main executa o método join, ela sabe que precisa esperar a execução da thread que envia os comandos . A thread main ficará esperando até a outra thread acabar.

![Envio e resposta cliente servidor](../img_readme/envio_respostaserver_cliente.png)

- Uma outra ideia para um projeto cliente-servidor seria usar as Threads para criar um bate papo. Novamente poderíamos ajustar o nosso projeto para tal. A diferença seria que o servidor precisasse manter todos os sockets dos clientes para espalhar as mensagens enviadas.

![Bate papo](../img_readme/bate-papo.png)

## Entendendo Volatile

<a name="anc4"></a>
- Agora temos a seguinte situação: o nosso cliente envia o comando fim, que é recebido através de uma thread dedicada e chama o método parar() que por sua vez manipula o atributo estaRodando.

![Exemplo1](../img_readme/Exemplo_thead.png)

- Repare que o acesso ao atributo acontece em uma outra thread, e até poderia acontecer em paralelo.

### Simulando o problema
- O acesso ao atributo através de várias threads pode sim criar problemas inesperados. No nosso exemplo é um pouco difícil de mostrar o problema acontecendo, pois temos clientes remotos, mas vamos simular o problema em um novo projeto.

- No Eclipse, criaremos um Java Project com o nome experimento. Nele, criaremos a classe ServidorDeTeste, no pacote br.com.alura.threads. Copiaremos a classe abaixo, que é bem parecida com nossa classe ServidorTarefas*:*

```
package br.com.alura.threads;

public class ServidorDeTeste {

    private boolean estaRodando = false;

    public static void main(String[] args) throws InterruptedException {
        ServidorDeTeste servidor = new ServidorDeTeste();
        servidor.rodar();
        servidor.alterandoAtributo();
    }

    private void rodar() {
        new Thread(new Runnable() {

            public void run() {
                System.out.println("Servidor começando, estaRodando = " + estaRodando );

                while(!estaRodando) {}

                System.out.println("Servidor rodando, estaRodando = " + estaRodando );

                while(estaRodando) {}

                System.out.println("Servidor terminando, estaRodando = " + estaRodando );
            }
        }).start();
    }

    private void alterandoAtributo() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("Main alterando estaRodando = true");
        estaRodando = true;

        Thread.sleep(5000);
        System.out.println("Main alterando estaRodando = false");
        estaRodando = false;        
    }
}
```

- Esperado ao rodar a classe
```
Servidor começando, estaRodando = false
Main alterando estaRodando = true
Servidor rodando, estaRodando = true
Main alterando estaRodando = false
Servidor terminando, estaRodando = false
```
- Real acontecido
```
Servidor começando, estaRodando = false
Main alterando estaRodando = true
Main alterando estaRodando = false
```
- Por quê?
### Entendendo o volatile
- O problema é que uma thread pode cachear variáveis e é muito provável que isso acontecerá! Cada thread é mapeada para uma thread nativa do sistema operacional, e esses caches nativos vão aproveitar o cache da CPU. Enquanto o nosso atributo fica na memória principal, a thread o guarda no cache da CPU!



![Exemplo2](../img_readme/cache1.png)

- Então, como podemos dizer que não queremos usar esse cache? Isso é muito fácil e o Java possui uma palavra chave para tal: volatile.

- O nosso atributo será volatile, que significa que cada thread deve acessar diretamente a memória principal.
```
private volatile boolean estaRodando = false;
```

![Exemplo3](../img_readme/cache2.png)

### Usando variáveis atômicas
- Há uma alternativa ao uso da palavra chave volatile. Em vez de usar volatile diretamente, podemos utilizar classes do pacote java.util.concurrent.atomic. Nesse pacote encontraremos uma classe com o nome AtomicBoolean que garante que todas as threads usam essa variável de maneira atômica, sem cache.

- Vamos voltar ao nosso projeto real, servidor-tarefas, na classe ServidorTarefas, e definir a variável estaRodando como o tipo AtomicBoolean.
```
private AtomicBoolean estaRodando;
```
- E no construtor:
```
public ServidorTarefas() throws IOException {
    System.out.println("---- Iniciando Servidor ----");
    this.servidor = new ServerSocket(12345);
    this.threadPool = Executors.newCachedThreadPool();
    this.estaRodando = new AtomicBoolean(true); // devemos dar new em AtomicBoolean
}
```
- Para recuperar o valor, devemos chamar um método get. Aplicaremos isso no nosso laço while do método rodar() da classe ServidorTarefas:

```
while (this.estaRodando.get()) {
```

- E para alterar o valor, usamos o método set dentro do método parar():

```
public void parar() throws IOException {
    this.estaRodando.set(false);
    this.threadPool.shutdown();
    this.servidor.close();
}
```

- Vimos na aula a classe AtomicBoolean para não precisar usar volatile ou syncronized ao acessar a uma variável. A classe faz parte do pacote java.util.concurrent.atomic onde podemos encontrar outras classes, [como por exemplo AtomicInteger e AtomicLong.](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/atomic/package-summary.html)

- No tutorial da Oracle é apresentado um pequeno exemplo como seria uma classe usando syncronized comparado com AtomicInteger. 
[Vale à pena conferir:](https://docs.oracle.com/javase/tutorial/essential/concurrency/atomicvars.html)


## Distribuindo comandos e tratamento de erro

<a name="anc5"></a>

## Retornos no Futuro

<a name="anc6"></a>

## Produzindo e Consumindo com Threads

<a name="anc7"></a>