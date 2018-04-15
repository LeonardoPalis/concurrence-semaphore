/*
* Exercicio do robo
* Aluno: Leonardo Andrade Ferreira Palis
*/

import java.util.concurrent.Semaphore;
import java.util.Random;

class semaforo extends Thread{

	private static final int PA = 1, PAR = 3, GRADE = 5; 	//itens
	private static Semaphore grade_pa, grade_par, par_pa, mesa; 	// semaforos
	private static int peca1, peca2;	//pecas
			
	public static void main(String args[]) throws InterruptedException{
		grade_pa = new Semaphore(1); 	//tamanho maximo do semaforo = 1
		grade_par = new Semaphore(1);   //tamanho maximo do semaforo = 1
		par_pa = new Semaphore(1);      //tamanho maximo do semaforo = 1
		mesa = new Semaphore(1);        //tamanho maximo do semaforo = 1
		/* todos robos comecam esperando a mesa	*/	
		grade_pa.acquire();		
		grade_par.acquire();
		par_pa.acquire();

	       /** Criacao de threads, uma para cada objeto (robo1,robo2,robo3 e mesa) **/
	      new Thread() {
			
			@Override
			public void run()  {
				
				try{
				while(true){
					ROBO_PAR();
				}
				}catch(InterruptedException e){
					System.out.println("OH NAO, FALHA NAS THREADS");	
				}
			}
		}.start();

		new Thread() {
			
			@Override
			public void run()  {
				while(true)
				try{
					ROBO_PA();
				}catch(InterruptedException e){
					System.out.println("OH NAO, FALHA NAS THREADS");	
				}
			}
		}.start();
		

		new Thread() {
			
			@Override
			public void run()  {
				while(true)
				try{
					ROBO_GRADE();
				}catch(InterruptedException e){
					System.out.println("OH NAO, FALHA NAS THREADS");	
				}
			}
		}.start();

		new Thread() {
			
			@Override
			public void run()  {
				
				try{
					while(true){
						mesa();
						Thread.sleep(2000);	//a mesa gera itens a cada 2 segundos
					}
				
				}catch(InterruptedException e){
					System.out.println("OH NAO, FALHA NAS THREADS");	
				}
			}
		}.start();

	}


		

	public static void ROBO_PAR() throws InterruptedException{

		
		grade_pa.acquire(); //down no semaforo
		mesa.acquire();	    //down na mesa
		pegar_pecas(PAR);   //pega as pecas
		mesa.release();	    //libera a mesa
		construir();	    //trabalha


	}

	public static void ROBO_PA() throws InterruptedException{

		
		grade_par.acquire();	//down no semaforo	
		mesa.acquire();		//down na mesa
		pegar_pecas(PA);	 //pega as pecas
		mesa.release();		 //libera a mesa
		construir();		 //trabalha


	}

	public static void ROBO_GRADE() throws InterruptedException{

		
		par_pa.acquire();	//down no semaforo
		mesa.acquire();		//down na mesa
		pegar_pecas(GRADE);	//pega as pecas
		mesa.release();		 //libera a mesa
		construir();		//trabalha

	}
	
	public static void pegar_pecas(final int robo){

		switch(robo){

			case 1: 
				System.out.println("* ROBO PA PEGOU PECAS *");
				break;
			case 3:
				System.out.println("* ROBO PARAFUSO PEGOU PECAS *");
				break;
			case 5:
				System.out.println("* ROBO GRADE PEGOU PECAS *");
				break;
		
		}
		
		
	}

	public static void construir(){

		System.out.println("Construindo...\n");
	
	}

	public static void mesa() throws InterruptedException{

		mesa.acquire();	//sinaliza que usara a mesa
		System.out.println("A mesa esta gerando itens...");
		//gera itens diferentes		
		do{	
			peca1 = geraItens();
			peca2 = geraItens();
		}while(peca1 == peca2);
		
		
		switch( peca1 + peca2 ){

			case 4:	
				System.out.println("Gerou PARAFUSO e PA");
				
				par_pa.release();				
				break;

			case 6:
				System.out.println("Gerou GRADE e PA");
				
				grade_pa.release();
				
				break;

			case 8:
				System.out.println("Gerou GRADE e PARAFUSO");
				
				grade_par.release();
				break; 
			default:
				System.out.println("Erro ao gerar item!");
				break;


		}
		
		mesa.release();		//libera a mesa


	}


	/*
	* Este metodo gera numeros aleatorios e retorna o item gerado 
	*/
	public static int geraItens(){

		Random rand = new Random();
		int tmp = rand.nextInt(51);	//gera itens de 0 a 51


		if( tmp < 11 ){
			return 1;	//gerou pa
		}
		if( tmp >= 11 && tmp < 31 ){
			return 3;	//gerou parafuso
		}
		if( tmp >= 31 && tmp < 51 ){
			return 5;	//gerou grade
		}
		
		return 0;

	}

	

}
