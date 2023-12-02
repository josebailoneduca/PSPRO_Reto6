package reto6a;


/**
 * Contiene el estado de los tenedores. En mano de que filosofos estan y a que filosofo pertenecen.
 * Tiene metodos para comprobar si un filosofo sujeta un tenedor, si lo posee, para pasar la propiedad 
 * de un tenedor a un filosofo y para ceder su uso
 */
public class Tenedores {
	
	/**
	 * Almacena en un array bidimiensional la posesion de los tenedores.
	 * La lectura de indices es [A][B]=valor
	 * Valor define si A tiene la propiedad del tenedor que comparte con B siendo el tamano 
	 * del array[num filosofos][numfilosofos]
	 * 
	 */
	private static boolean [][] posee;

	/**
	 * Almacena en un array bidimiensional la tenencia en la mano de un tenedor para ser usado.
	 * La lectura de indices es [A][B]=valor
	 * Valor define si A tiene en la mano el tenedor que comparte con B  siendo el tamano 
	 * del array[num filosofos][numfilosofos]
	 */
	private static boolean [][] sujeta;
	
	
	/**
	 * Lanza la configuracion de los arrays creandolos y rellenandolos de manera asimetrica 
	 * asignando el primer y segundo tenedor al primer filosofo, el resto de tenedores a los siguientes 
	 * filosofos y ningun tenedor al ultimo filosofo
	 * 
	 * @param nFilosofos Numero de filosofos que intervienen
	 */
	public static void configurar(int nFilosofos) {
		posee=new boolean[nFilosofos][nFilosofos];
		sujeta=new boolean[nFilosofos][nFilosofos];

		//inicializacion asimetrica

		//el primero tiene dos tenederes.
		ceder(nFilosofos-1,0);
		//el resto tiene 1 tenedor excepto el ultimo que no tine tenedores
		darPropiedad(nFilosofos-1,0);
		for(int i=0;i<nFilosofos-1;i++) {
			ceder(i+1,i);
			darPropiedad(i+1,i);
		}
	}
	
	
	
	/**
	 * Devuelve si 'a' sujeta el tenedor entre 'a' y 'b'
	 * @param a Indice del filosofo a comprobar
	 * @param b Indice del filosofo con el que 'a' comparte el tenedor
	 * @return True si 'a' sujeta el tenedor y False en caso contrario
	 */
	public static boolean sujeta(int a, int b) {
		return sujeta[a][b];
	}
	
	/**
	 * Devuelve si el tenedor entre 'a' y 'b' es propiedad de 'a'
	 * @param a Indice del filosofo a comprobar
	 * @param b Indice del filosofo con el que 'a' comparte el tenedor
	 * @return True si 'a' tiene la propiedad del tenedor y False en caso contrario
	 */
	public static boolean posee(int a, int b) {
		return posee[a][b];
	}
	
	
	/**
	 * Cambia el tenedor compartido entre 'a' y 'b' a la mano de filosofo 'b'
	 * @param a Filosofo origen
	 * @param b Filosofo destino
	 */
	public static void ceder(int a, int b) {
		//quitar tenedor de la mano de a
		sujeta[a][b]=false;
		//poner tenedor en la mano de b
		sujeta[b][a]=true;
	}
	
	/**
	 *  Cambia la propiedad del tenedor compartido entre 'a' y 'b' dandole la propiedad a 'b'
	 * @param a Filosofo origen
	 * @param b Filosofo destino
	 */
	public static void darPropiedad(int a, int b) {
		//quitar posesion a 'a'
		posee[a][b]=false;
		//poner posesion a 'b'
		posee[b][a]=true;
	}
 
}
