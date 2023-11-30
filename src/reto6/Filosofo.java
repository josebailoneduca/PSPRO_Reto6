package reto6;

import java.util.Random;

public class Filosofo extends Thread {



	private static final int GRADO=2;
	private int yo;
	private int[] vecinos;
	private Filosofo[] filosofos;
	private boolean hambriento;
	private boolean comiendo;
	
	
	
	
	
	public Filosofo(int yo, int[] vecinos, Filosofo[] filosofos) {
		super();
		this.yo = yo;
		this.vecinos = vecinos;
		this.filosofos = filosofos;
		this.hambriento=false;
		this.comiendo=false;
	}
	

	
	@Override
	public void run() {
		 	while(true) {
		 		Estadistica.pensando(yo);
		 		pensar();
		 		long iniHambriento=System.currentTimeMillis();
		 		Estadistica.hambriento(yo);
		 		toTable();
		 		Estadistica.tiempoHambriento(yo, System.currentTimeMillis()-iniHambriento);
		 		Estadistica.comiendo(yo);
		 		comer();
		 		fromTable();
		 	}
	}
	

	private void comer() {
		Estadistica.comiendo(yo);
		Random r=new Random();
		try {
			Thread.currentThread().sleep(r.nextLong(Config.MIN_COMER,Config.MAX_COMER));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	private void pensar() {
		Estadistica.pensando(yo);
		Random r=new Random();
		try {
			Thread.currentThread().sleep(r.nextLong(Config.MIN_PENSAR,Config.MAX_PENSAR));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	synchronized void toTable() {
		this.hambriento=true;
		int mf;
		while ( (mf = missingFork()) < 2)
			filosofos[vecinos[mf]].yieldForkTo(yo) ;

	}
	
	synchronized void fromTable() {
		comiendo = false ;
		hambriento = false ;
		for (int k = 0; k < 2; k ++) {
		  Tenedores.move(yo, vecinos[k]) ;
		  Tenedores.give(yo, vecinos[k]) ;
		}
		notifyAll () ;
		}

	
	synchronized int  missingFork() {
		int mf = 0 ;
		while (mf < 2 && Tenedores.holds(yo, vecinos[mf])) mf++ ;
		if (mf == 2) comiendo = true ;
		return mf ;

	}
	
	
	synchronized void yieldForkTo (int idOtro) {
		if (comiendo || hambriento && Tenedores.owns(yo, idOtro)) {
			try { this.wait();} catch (InterruptedException e) {	e.printStackTrace();}
		}
		else Tenedores.move(yo, idOtro) ;
		}
}
