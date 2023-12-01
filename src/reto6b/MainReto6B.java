package reto6b;


/**
 * Punto de entrada del Reto 6.
 * 
 * Implementación de la solucion propuesta en el paper de Wim H. Hesselink del 31st August 2001 
 * para el problema de la cena de los filosofos.
 * 
 * En esta clase se configura el estado inicial del sistema y se pone a funcionar.
 * 
 * Configura la clase de estadisticas, genera los filosofos y arrays compartidos, 
 * lanza la configuracion del estado asimetrico de pertenencia de los tenedores y 
 * arranca la hebra de cada filosofo.

 */
public class MainReto6B {
	
	
	
	public static void main(String[] args) {
		
		Estadistica.configurar();
		Filosofo[]filosofos=new Filosofo[Config.N_FILOSOFOS];
		

		//crear filosofos
		for (int i=0;i<Config.N_FILOSOFOS;i++) {
			filosofos[i]=new Filosofo(i);
		}
		
		//asignar vecinos
		//primero
		filosofos[0].setVecinoIzquierdo(filosofos[1]);
		filosofos[0].setVecinoDerecho(filosofos[Config.N_FILOSOFOS-1]);
		//ultimo
		filosofos[Config.N_FILOSOFOS-1].setVecinoIzquierdo(filosofos[0]);
		filosofos[Config.N_FILOSOFOS-1].setVecinoDerecho(filosofos[Config.N_FILOSOFOS-2]);
		
		//el resto
		for (int i=1;i<Config.N_FILOSOFOS-1;i++) {
			filosofos[i].setVecinoDerecho(filosofos[i-1]);
			filosofos[i].setVecinoIzquierdo(filosofos[i+1]);
		}
		
		//asignar tenedores
		//primero
		filosofos[0].setTenedorDerecho(new Tenedor(filosofos[0]));
		filosofos[0].setTenedorIzquierdo(new Tenedor(filosofos[0]));
		
		//resto menos el ultimo
		for (int i=1;i<Config.N_FILOSOFOS-1;i++) {
			filosofos[i].setTenedorIzquierdo(new Tenedor(filosofos[i]));
		}
		
		
		//iniciar filosofos
		for(int i=0;i<Config.N_FILOSOFOS;i++) {
			filosofos[i].start();
		}
		
	}

}
