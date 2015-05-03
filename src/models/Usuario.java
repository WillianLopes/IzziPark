package models;

/**
 * Cliente model, default constructor.
 * @author Willian Lopes
 *
 */
public class Usuario {
	
	/**
	 * Private class attributes
	 */
	private int _id;
	private String usuario;
	private String senha;
	private String cpf;
	private String email;
	
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

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
