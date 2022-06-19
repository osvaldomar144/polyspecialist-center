package com.polyspecialistcenter.aws.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Prenotazione {
	
	public static final String DIR_PAGES_PREN = "information/prenotazione/";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private Professionista professionista;
	
	@ManyToOne
	private Servizio servizio;
	
	@OneToOne
	private Disponibilita disponibilita;
	
	@ManyToOne
	private Utente cliente;
	
	private boolean isValid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Professionista getProfessionista() {
		return professionista;
	}

	public void setProfessionista(Professionista professionista) {
		this.professionista = professionista;
	}

	public Servizio getServizio() {
		return servizio;
	}

	public void setServizio(Servizio servizio) {
		this.servizio = servizio;
	}

	public Disponibilita getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(Disponibilita disponibilita) {
		this.disponibilita = disponibilita;
	}

	public Utente getCliente() {
		return cliente;
	}

	public void setCliente(Utente cliente) {
		this.cliente = cliente;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}
