package models;

/**
 * Cliente model, default constructor.
 * @author Willian Lopes
 *
 */
public class Park {
	
	/**
	 * Private class attributes
	 */
	private int _id;
	private String placa;
	private String hora_entrada;
	private String hora_saida;
	private String tipo;
	
	/**
	 * Getters and setters for private class attributes.
	 * @return
	 */
	public int getId() {
		return _id;
	}
	
	public void setId(int id) {
		this._id = id;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getHora_entrada() {
		return hora_entrada;
	}

	public void setHora_entrada(String hora_entrada) {
		this.hora_entrada = hora_entrada;
	}

	public String getHora_saida() {
		return hora_saida;
	}

	public void setHora_saida(String hora_saida) {
		this.hora_saida = hora_saida;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	
	
}
