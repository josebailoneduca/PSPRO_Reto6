package reto6b;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Representa un filosofo. Se configura de manera inicial con un id unico, un
 * array de referencias a los filosofos y un array con los identificadores de
 * los filosofos vecinos.
 * 
 * En su carrera ejecuta el proceso del problema:
 * <ul>
 * <li>Pensar</li>
 * <li>Estar habriento e intentar coger los tenedores
 * </ul>
 * <li>Comer</li>
 * <li>Dar los tenedores a los vecinos</li>
 * </ul>
 * 
 * Este proceso lo hacen en un bucle infinito.
 */
public class Filosofo extends Thread {

	/**
	 * Identificador del filosofo. Coincide con el indice en el que aparece en el
	 * array de filosofos
	 */
	private int id;

	private Filosofo vecinoDerecho;
	private Filosofo vecinoIzquierdo;
	private Tenedor tenedorDerecho;
	private Tenedor tenedorIzquierdo;
	private Semaphore sTenDer;
	private Semaphore sTenIzq;
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
	 * 
	 * @param id        Identificador del filosofo. Coincide con el indice en el que
	 *                  aparece en el array de filosofos
	 * @param vecinos   Array de id de filosofos vecinos con los cuales comparte
	 *                  tenedores
	 * @param filosofos Array de todos los filosofos que intervienen en el proceso
	 */
	public Filosofo(int id) {
		this.id = id;
		this.hambriento = false;
		this.comiendo = false;
		this.sTenDer=new Semaphore(1);
		this.sTenIzq=new Semaphore(1);
	}

	public void setVecinoDerecho(Filosofo vecinoDerecho) {
		this.vecinoDerecho = vecinoDerecho;
	}

	public void setVecinoIzquierdo(Filosofo vecinoIzquierdo) {
		this.vecinoIzquierdo = vecinoIzquierdo;
	}

	public void setTenedorDerecho(Tenedor tenedorDerecho) {
		try {
			this.sTenDer.acquire();
			this.tenedorDerecho = tenedorDerecho;
			this.sTenDer.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setTenedorIzquierdo(Tenedor tenedorIzquierdo) {
		try {
			this.sTenIzq.acquire();
			this.tenedorIzquierdo = tenedorIzquierdo;
			this.sTenIzq.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Carrera del filosofo. Es un bucle infinito en el cual hace las fases del
	 * proceso: pensar, coger tenedores, comer, dejar tenedores. Ademas avisa a la
	 * clase Estadistica de su estado y del tiempo que ha pasado hambriento
	 */
	@Override
	public void run() {
		while (true) {
			Estadistica.pensando(id);
			pensar();
			Estadistica.hambriento(id);
			long iniTiempoHambriento = System.currentTimeMillis();
			cogerTenedores();
			Estadistica.tiempoHambriento(id, System.currentTimeMillis() - iniTiempoHambriento);
			Estadistica.comiendo(id);
			comer();
			dejarTenedores();
		}
	}

	public void cederTenedorDerecho() {
		try {
			this.sTenDer.acquire();
			if (tenedorDerecho!=null && !comiendo &&!(hambriento && tenedorDerecho.getPropietario().equals(this))) {
				vecinoDerecho.setTenedorIzquierdo(tenedorDerecho);
				tenedorDerecho=null;
			}
			this.sTenDer.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void cederTenedorIzquierdo() {
		try {
			this.sTenIzq.acquire();
			if (tenedorIzquierdo!=null && !comiendo &&!(hambriento && tenedorIzquierdo.getPropietario().equals(this))) {
				vecinoIzquierdo.setTenedorDerecho(tenedorIzquierdo);
				tenedorIzquierdo=null;
			}
			this.sTenIzq.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Simulacion de comer. Consiste en dormir el hilo durante un tiempo aleatorio
	 * finito
	 */
	private void comer() {
		Random r = new Random();
		try {
			Thread.currentThread().sleep(r.nextLong(Config.MIN_COMER, Config.MAX_COMER));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Simulacion de pensar. Consiste en dormir el hilo durante un tiempo aleatorio
	 * finito
	 */
	private void pensar() {
		Random r = new Random();
		try {
			Thread.currentThread().sleep(r.nextLong(Config.MIN_PENSAR, Config.MAX_PENSAR));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Proceso de adquirir los tenedores. Establece que esta hambriento y pide
	 * prestados los tenedores que no se tienen hasta tener ambos.
	 */
	 private void cogerTenedores() {
		this.hambriento = true;
		while (tenedorDerecho == null || tenedorIzquierdo == null) {
			if (tenedorDerecho == null)
				vecinoDerecho.cederTenedorIzquierdo();
			if (tenedorIzquierdo == null)
				vecinoIzquierdo.cederTenedorDerecho();
		}
		this.comiendo=true;
		try {
			this.sTenDer.acquire();
			this.sTenIzq.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Representa la finalizacion del proceso de comer. Desactiva sus estados de
	 * comiendo y hambriento y da la propiedad de los tenedores a los vecinos ademas
	 * de pasarselos. Notifica a los hilos que hubiese esperando.
	 */
	 private void dejarTenedores() {
		comiendo = false;
		hambriento = false;
 

			tenedorDerecho.setPropietario(vecinoDerecho);
			vecinoDerecho.setTenedorIzquierdo(tenedorDerecho);
			tenedorDerecho=null;
			this.sTenDer.release();
			

			tenedorIzquierdo.setPropietario(vecinoIzquierdo);
			vecinoIzquierdo.setTenedorDerecho(tenedorIzquierdo);
			tenedorIzquierdo=null;
			this.sTenIzq.release();
 
	}

}
