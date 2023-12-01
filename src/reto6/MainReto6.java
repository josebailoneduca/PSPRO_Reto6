package reto6;


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
public class MainReto6 {
	
	
	
	public static void main(String[] args) {
		
		Estadistica.configurar();
		Filosofo[]filosofos=new Filosofo[Config.N_FILOSOFOS];
		

		//crear filosofos
		for (int i=0;i<Config.N_FILOSOFOS;i++) {
			int[]vecinos= {i-1,(i+1)%Config.N_FILOSOFOS};
			if (vecinos[0]<0)
				vecinos[0]=Config.N_FILOSOFOS-1;
			filosofos[i]=new Filosofo(i, vecinos, filosofos);
		}
		
		//configurar asignación asimetrica de tenedores
		Tenedores.configurar(Config.N_FILOSOFOS);
		
		//iniciar filosofos
		for(int i=0;i<Config.N_FILOSOFOS;i++) {
			filosofos[i].start();
		}
		
	}

}
