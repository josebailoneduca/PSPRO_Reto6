package reto6;

public class Tenedores {
	
	private static boolean [][] posee;
	private static boolean [][] tiene;
	
	public static void configurar(int nFilosofos) {
		posee=new boolean[nFilosofos][nFilosofos];
		tiene=new boolean[nFilosofos][nFilosofos];

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
		return tiene[q][r];
	}
	synchronized public static boolean owns(int q, int r) {
		return posee[q][r];
	}
	
	/**
	 * cambia el que tiene el uso
	 * @param q origen
	 * @param r destino
	 */
	synchronized public static void move(int q, int r) {
		tiene[q][r]=false;
		tiene[r][q]=true;
	}
	
	/**
	 * Cambia el que posee
	 * @param q origen
	 * @param r destino
	 */
	synchronized public static void give(int q, int r) {
		posee[q][r]=false;
		posee[r][q]=true;
	}
	
	public static void imprimir() {
		System.out.println("POSEE");
		for (boolean[]fila:posee) {
			for (boolean po:fila){
				System.out.print(po+ " ");
			}
			System.out.println();
		}
		
		System.out.println("TIENE");
		for (boolean[]fila:tiene) {
			for (boolean po:fila){
				System.out.print(po+ " ");
			}
			System.out.println();
		}
	}
}
