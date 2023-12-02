package reto6b;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Representa un tenedor. 
 * Contiene los atributos de quien es su propietario 
 * y quien es su usuario actual(quien lo tiene en la mano) 
 */
public class Tenedor {
	/**
	 * Filosofo que tiene la propiedad del tenedor
	 */
	private Filosofo propietario;
	
	/**
	 * Filosofo que tiene el tenedor en la mano
	 */
	private AtomicReference<Filosofo> usuario =new AtomicReference<Filosofo>();
 	

	/**
	 * Devuelve quien es el pripietaro
	 * @return El propietario
	 */
	public Filosofo getPropietario() {
		return propietario;
	}
	
	/**
	 * Define quien es el propietario
	 * @param propietario El nuevo propietario
	 */
	public void setPropietario(Filosofo propietario) {
		this.propietario = propietario;
	}
	
	/**
	 * Devuelve el filosofo que tiene su uso
	 * @return El filosofo que tiene su uso
	 */
	public Filosofo getUsuario() {
 		return usuario.get();
	}
	
	/**
	 * Define el filosofo que tiene su uso
	 * @param usuario El nuevo filosofo que tiene su uso
	 */
	public void setUsuario(Filosofo usuario) {
			this.usuario.set(usuario);
	}
 	
}
