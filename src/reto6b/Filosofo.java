package reto6b;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Representa un filosofo. Contiene referencias a cuales son sus vecinos, cuales
 * son los tenedores que puede usar, si esta hambriento y si esta comiendo.
 * 
 * En su carrera ejecuta el proceso del problema:
 * <ul>
 * <li>Pensar</li>
 * <li>Estar habriento e intentar coger los tenedores</li>
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
	/**
	 * Referencia a su vecino derecho
	 */
	private Filosofo vecinoDerecho;

	/**
	 * Referencia a su vecino izquierdo
	 */
	private Filosofo vecinoIzquierdo;

	/**
	 * Referencia a su tenedor derecho (lo tenga o no en la mano)
	 */
	private Tenedor tenedorDerecho;

	/**
	 * Referencia a su tenedor izquierdo (lo tenga o no en la mano)
	 */
	private Tenedor tenedorIzquierdo;

	/**
	 * True si esta hambriento
	 */
	private boolean hambriento;

	/**
	 * True si esta comiendo
	 */
	private boolean comiendo;

	/**
	 * Semaforo para conseguir mutex durante la comprobacion de si se puede comer
	 * sin que antes de decidir que se esta comiendo cambien los tenedores a manos
	 * de un vecino
	 */

	private Semaphore comprobarPoderComer = new Semaphore(1);

	/**
	 * Constructor
	 * 
	 * @param id        Identificador del filosofo.
	 * @param vecinos   Array de id de filosofos vecinos con los cuales comparte
	 *                  tenedores
	 * @param filosofos Array de todos los filosofos que intervienen en el proceso
	 */
	public Filosofo(int id, Tenedor tenedorDerecho, Tenedor tenedorIzquierdo) {
		this.id = id;
		this.hambriento = false;
		this.comiendo = false;
		this.tenedorDerecho = tenedorDerecho;
		this.tenedorIzquierdo = tenedorIzquierdo;

	}

	public void setVecinos(Filosofo vecinoDerecho, Filosofo vecinoIzquierdo) {
		this.vecinoDerecho = vecinoDerecho;
		this.vecinoIzquierdo = vecinoIzquierdo;
	}

	/**
	 * Carrera del filosofo. Es un bucle infinito en el cual hace las fases del
	 * proceso: pensar, coger tenedores, comer, dejar tenedores. Ademas avisa a la
	 * clase Estadistica de su estado y del tiempo que ha pasado hambriento
	 */
	@Override
	public void run() {
		Estadistica.pensando(id);
		while (true) {
			pensar();
			Estadistica.hambriento(id);
			long iniTiempoHambriento = System.currentTimeMillis();
			cogerTenedores();
			Estadistica.tiempoHambriento(id, System.currentTimeMillis() - iniTiempoHambriento);
			Estadistica.comiendo(id);
			comer();
			Estadistica.pensando(id);
			dejarTenedores();
		}
	}

	/**
	 * Cede el tenedor derecho a su vecino derecho si: no se esta comiendo y no se 
	 * esta hambriento teniendo la propiedad del tenedor
	 */
	public void cederTenedorDerecho() {
		try {
			// tomar semaforo para mutex de decidir comer
			comprobarPoderComer.acquire();

			// ceder el tenedor si no se esta comiendo y no se esta hambriento teniendo la
			// propiedad del tenedor
			if (!comiendo && !(hambriento && tenedorDerecho.getPropietario().equals(this)))
				tenedorDerecho.setUsuario(vecinoDerecho);

			// abrir semaforo para mutex de decidir comer
			comprobarPoderComer.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Cede el tenedor izquierdo a su vecino izquierdo si: no se esta comiendo y no
	 * se esta hambriento teniendo la propiedad del tenedor El proceso no se realiza
	 * mientras este cerrado el semaforo que sirve para la mutex de estar deciciendo
	 * si se puede comer o no
	 */
	public void cederTenedorIzquierdo() {
		try {
			// semaforo para mutex de decidir comer
			comprobarPoderComer.acquire();

			// ceder el tenedor si no se esta comiendo y no se esta hambriento teniendo la
			// propiedad del tenedor
			if (!comiendo && !(hambriento && tenedorIzquierdo.getPropietario().equals(this)))
				tenedorIzquierdo.setUsuario(vecinoIzquierdo);

			// abrir semaforo para mutex de decidir comer
			comprobarPoderComer.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Proceso de adquirir los tenedores. Establece que esta hambriento y pide
	 * prestados los tenedores que no se tengan hasta tenerlos todos.
	 */
	private void cogerTenedores() {
		// iniciar estado de hambriento
		this.hambriento = true;

		// intentar coger ambos tenedores hasta estar comiendo
		while (!comiendo) {
			try {
				// coger tenedor compartido con el vecino derecho si no se tiene
				if (!tenedorDerecho.getUsuario().equals(this))
					vecinoDerecho.cederTenedorIzquierdo();

				// coger tenedor compartido con el vecino izquierdo si no se tiene
				if (!tenedorIzquierdo.getUsuario().equals(this))
					vecinoIzquierdo.cederTenedorDerecho();

				
				
				// iniciar mutex para comprobar si se tienen ambos tenedores
				comprobarPoderComer.acquire();

				// si se tienen ambos tenedores se establece que se esta comiendo
				if (tenedorIzquierdo.getUsuario().equals(this) && tenedorDerecho.getUsuario().equals(this))
					this.comiendo = true;

				// terminar mutex
				comprobarPoderComer.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Representa la finalizacion del proceso de comer. Desactiva sus estados de
	 * comiendo y hambriento y da la propiedad y uso de los tenedores a los vecinos
	 */
	private void dejarTenedores() {
		comiendo = false;
		hambriento = false;

		tenedorDerecho.setPropietario(vecinoDerecho);
		tenedorDerecho.setUsuario(vecinoDerecho);

		tenedorIzquierdo.setPropietario(vecinoIzquierdo);
		tenedorIzquierdo.setUsuario(vecinoIzquierdo);

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

}
