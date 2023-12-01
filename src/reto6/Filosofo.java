package reto6;

import java.util.Random;


/**
 * Representa un filosofo. Se configura de manera inicial con un id unico, un array de referencias a los filosofos
 * y un array con los identificadores de los filosofos vecinos.
 * 
 * En su carrera ejecuta el proceso del problema:
 * <ul>
 * <li>Pensar</li>
 * <li>Estar habriento e intentar coger los tenedores</ul>
 * <li>Comer</li>
 * <li>Dar los tenedores a los vecinos</li>
 * </ul>
 * 
 * Este proceso lo hacen en un bucle infinito.
 */
public class Filosofo extends Thread {


	
	
	/**
	 * Identificador del filosofo. Coincide con el indice en el que aparece en el array de filosofos
	 */
	private int idPropia;
	
	/**
	 * Array de id de filosofos vecinos con los cuales comparte tenedores
	 */
	private int[] vecinos;
	
	/**
	 * Array de todos los filosofos que intervienen en el proceso
	 */
	private Filosofo[] filosofos;
	
	/**
	 * True si esta hambriento
	 */
	private boolean hambriento;
	
	/**
	 * True si esta comiendo
	 */
	private boolean comiendo;
	
	
	
	
	/**
	 * Constructor
	 * @param idPropia Identificador del filosofo. Coincide con el indice en el que aparece en el array de filosofos
	 * @param vecinos Array de id de filosofos vecinos con los cuales comparte tenedores
	 * @param filosofos Array de todos los filosofos que intervienen en el proceso
	 */
	public Filosofo(int idPropia, int[] vecinos, Filosofo[] filosofos) {
		this.idPropia = idPropia;
		this.vecinos = vecinos;
		this.filosofos = filosofos;
		this.hambriento=false;
		this.comiendo=false;
	}
	

	
	/**
	 * Carrera del filosofo. Es un bucle infinito en el cual hace las fases del proceso: pensar, coger tenedores, comer, dejar tenedores. 
	 * Ademas avisa a la clase Estadistica de su estado y del tiempo que ha pasado hambriento
	 */
	@Override
	public void run() {
		 	while(true) {
		 		Estadistica.pensando(idPropia);
		 		pensar();
		 		
		 		Estadistica.hambriento(idPropia);
		 		long iniHambriento=System.currentTimeMillis();
		 		entrarMesa();
		 		Estadistica.tiempoHambriento(idPropia, System.currentTimeMillis()-iniHambriento);

		 		Estadistica.comiendo(idPropia);
		 		comer();
		 		
		 		salirMesa();
		 	}
	}
	
	
	
	/**
	 * Cede el tenedor compartido a otro filosofo a menos que este comiendo o que este hambriento y  
	 * el tenedor sea de su propiedad, en cuyo caso el hilo que ha ejecutado el método entra en espera.
	 * 
	 * @param idOtro Id del filosofo al que ceder el tenedor
	 */
	synchronized public void cederTenedor (int idOtro) {
		if ((comiendo) || (hambriento && Tenedores.posee(idPropia, idOtro))) {
			try { this.wait();} catch (InterruptedException e) {	e.printStackTrace();}
		}
		else Tenedores.ceder(idPropia, idOtro) ;
		}
	

	/**
	 * Simulacion de comer. Consiste en dormir el hilo durante un tiempo aleatorio finito
	 */
	private void comer() {
		Random r=new Random();
		try {
			Thread.currentThread().sleep(r.nextLong(Config.MIN_COMER,Config.MAX_COMER));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Simulacion de pensar. Consiste en dormir el hilo durante un tiempo aleatorio finito
	 */
	private void pensar() {
		Random r=new Random();
		try {
			Thread.currentThread().sleep(r.nextLong(Config.MIN_PENSAR,Config.MAX_PENSAR));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Proceso de adquirir los tenedores. Establece que esta hambriento y pide prestados 
	 * los tenedores que no se tienen hasta tener ambos.
	 */
	synchronized private void entrarMesa() {
		this.hambriento=true;
		//cantidad de tenedores que se tienen en la mano o el indice de uno que falta
		int tenedorQueFalta;
		//pedir prestados los tenedores hasta que se tenga los dos tenedores en la mano. 
		// Si "tenedorQueFalta" es menor que 2 entonces coincide con la id en el array de vecinos 
		// del vecino con el que comparte un tenedor que no se tiene y por consiguiente se pide
		// Si "tenedorQueFalta" es 2 es que se tienen ambos tenedores y el bucle de busqueda de tenedores termina
		while ( (tenedorQueFalta = tenedorRequerido()) < 2)
			filosofos[vecinos[tenedorQueFalta]].cederTenedor(idPropia) ;
		comiendo=true;
	}
	
	
	/**
	 * Representa la finalizacion del proceso de comer. Desactiva sus estados de comiendo y hambriento y 
	 * da la propiedad de los tenedores a los vecinos ademas de pasarselos. Notifica a los hilos que hubiese
	 * esperando.
	 */
	synchronized private void salirMesa() {
		comiendo = false ;
		hambriento = false ;
		//dar tenedores a vecinos
		for (int i = 0; i < 2; i ++) {
		  Tenedores.ceder(idPropia, vecinos[i]);
		  Tenedores.darPropiedad(idPropia, vecinos[i]);
		}
		notifyAll ();
		}

	
	/**
	 * Comprueba si los tenedores que requiere los tiene en la mano.
	 * Si un tenedor no lo tiene en la mano devuelve el indice en el array de vecinos
	 * del vecino con el que comparte el tenedor que no se tiene
	 * @return El indice del array de vecinos con el que se comparte un tenedor que no se tiene o 2 si se tienen los dos tenedores
	 */
	synchronized private int tenedorRequerido() {
		int tenedorRequerido = 0 ;
		//comprobar tenedores y parar cuando se compruebe que se sujetan 2 o cuando no se sujete alguno
		while (tenedorRequerido < 2 && Tenedores.sujeta(idPropia, vecinos[tenedorRequerido])) tenedorRequerido++ ;
		
		return tenedorRequerido ;

	}
	

}
