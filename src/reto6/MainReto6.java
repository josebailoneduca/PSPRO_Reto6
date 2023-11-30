package reto6;

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
		Tenedores.imprimir();
		//iniciar filosofos
		
		for(int i=0;i<Config.N_FILOSOFOS;i++) {
			filosofos[i].start();
		}
		
	}

}
