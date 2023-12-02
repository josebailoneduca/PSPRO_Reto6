package reto6b;


/**
 * Punto de entrada del Reto 6 version B.
 * 
 * Implementación de la solucion propuesta en el paper de Wim H. Hesselink del 31st August 2001 
 * para el problema de la cena de los filosofos.
 * 
 * En esta clase se configura el estado inicial del sistema y se pone a funcionar.
 * 
 * Configura la clase de estadisticas, genera los tenedores, los filosofos y 
 * hace la asignación asimetrica inicial de uso y propiedad de los tenedores 
 * 
 * Inicia la carrera de la hebra de cada filosofo.
 */
public class MainReto6B {
	
	
	
	public static void main(String[] args) {
		//array de tenedores
		Tenedor[] tenedores;
		
		//array de filososos
		Filosofo[]filosofos;
		
		//configuracion inicial de la estadistica
		Estadistica.configurar();

		//crear tenedores
		tenedores = new Tenedor[Config.N_FILOSOFOS];
		for (int i=0;i<Config.N_FILOSOFOS;i++)
			tenedores[i]=new Tenedor();

		//crear filosofos y pasar referencia a los tenedores que usaran con cada mano
		filosofos=new Filosofo[Config.N_FILOSOFOS];
		for (int i=0;i<Config.N_FILOSOFOS;i++) {
			filosofos[i]=new Filosofo(i,tenedores[i],tenedores[(i+1)%Config.N_FILOSOFOS]);
		}
		
		//asignar vecinos a cada lado de cada filosofo
		for (int i=0;i<Config.N_FILOSOFOS;i++) {
			Filosofo vecinoDerecho=filosofos[(i-1+Config.N_FILOSOFOS)%Config.N_FILOSOFOS];
			Filosofo vecinoIzquierdo=filosofos[(i+1)%Config.N_FILOSOFOS];
			filosofos[i].setVecinos(vecinoDerecho,vecinoIzquierdo);
		}
		
		//Establecer la pertenencia y uso de los tenedores
		//Dos tenedores al primer filosofo
		tenedores[0].setPropietario(filosofos[0]);
		tenedores[0].setUsuario(filosofos[0]);
		tenedores[Config.N_FILOSOFOS-1].setPropietario(filosofos[0]);
		tenedores[Config.N_FILOSOFOS-1].setUsuario(filosofos[0]);
	
		//Un solo tenedor al resto menos el ultimo filosofo que se queda sin ninguno en la mano
		for (int i=1;i<Config.N_FILOSOFOS-1;i++) {
			tenedores[i].setPropietario(filosofos[i]);
			tenedores[i].setUsuario(filosofos[i]);
		}
		
		
		
		//Iniciar carrera de los filosofos
		for(int i=0;i<Config.N_FILOSOFOS;i++) {
			filosofos[i].start();
		}
		
	}

}
