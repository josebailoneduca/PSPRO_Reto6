package reto6b;

import java.util.concurrent.Semaphore;

public class Tenedor {
	Filosofo propietario;
	Filosofo usuario;
	Semaphore sUsuario=new Semaphore(1);
	Semaphore sUsando =new Semaphore(1);
	
	
	public Tenedor(Filosofo propietario ) {
		this.propietario = propietario;
	}
	public Filosofo getPropietario() {
		return propietario;
	}
	
	public void setPropietario(Filosofo propietario) {
		this.propietario = propietario;
	}
	
	
	
	public Filosofo getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Filosofo usuario) {
		this.usuario = usuario;
	}
	public void usar() {
		
	}
	
}
