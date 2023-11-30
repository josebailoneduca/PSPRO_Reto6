package reto6;

public class Tenedores {
	
	public static int [] posee;
	public static int [] tiene;
	
	public static void configurar(int nFilosofos) {
		posee=new int[nFilosofos];
		tiene=new int[nFilosofos];

		//inicializacion asimetrica
		//el primero tiene dos tenederes.
		//el resto tiene 1 tenedor 
		//el ultimo no tine tenedores
		move(nFilosofos-1,0);
		give(nFilosofos-1,0);
		for(int i=0;i<nFilosofos-1;i++) {
			move(i+1,i);
			give(i+1,i);
		}
		
	}
	
	
	
	/**
	 * Devuelve si q tiene el uso del tenedor entre q y r
	 * @param q
	 * @param r
	 * @return
	 */
	synchronized public static boolean holds(int q, int r) {
		return tiene[getIdTenedor(q,r)]==r;
	}




	synchronized public static boolean owns(int q, int r) {
		return posee[getIdTenedor(q,r)]==q;
	}
	
	/**
	 * cambia el que tiene el uso
	 * @param q origen
	 * @param r destino
	 */
	synchronized public static void move(int q, int r) {
 		tiene[getIdTenedor(q,r)]=q;
	}
	
	/**
	 * Cambia el que posee
	 * @param q origen
	 * @param r destino
	 */
	synchronized public static void give(int q, int r) {
		posee[getIdTenedor(q,r)]=r;
	}
	
	private static int getIdTenedor(int q, int r) {
		int menor=(q<r)?q:r;
		int mayor=(q>r)?q:r;
		int idTenedor=menor;
		if (menor==0&&mayor==Config.N_FILOSOFOS-1)
			idTenedor=mayor;
		return idTenedor;
	}
}
