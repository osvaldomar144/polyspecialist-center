package com.polyspecialistcenter.aws.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Utente {

	public static final String DIR_FOLDER_IMG = "user/profilo";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String cognome;
	
	/*@NotBlank
	private LocalDate dataDiNascita;*/
	
	private String img;
	
	@OneToOne (mappedBy="utente")
	private Credentials credentials;
	
	@OneToMany
	private List<Prenotazione> prenotazioni;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	/*public LocalDate getDataDiNascita() {
		return dataDiNascita;
	}

	public void setDataDiNascita(LocalDate dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}*/

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public List<Prenotazione> getPrenotazioni() {
		return prenotazioni;
	}

	public void setPrenotazioni(List<Prenotazione> prenotazioni) {
		this.prenotazioni = prenotazioni;
	}

}

