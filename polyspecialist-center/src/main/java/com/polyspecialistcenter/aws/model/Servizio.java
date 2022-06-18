package com.polyspecialistcenter.aws.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Servizio {
	
	public static final String DIR_PAGES_SERVIZIO = "information/servizio/";
	public static final String DIR_ADMIN_PAGES_SERVIZIO = "admin/servizio/";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String descrizione;
	
	/*@NotBlank*/
	private Float prezzo;
	
	@ManyToOne
	private Professionista professionista;
	
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Float prezzo) {
		this.prezzo = prezzo;
	}

	public Professionista getProfessionista() {
		return professionista;
	}

	public void setProfessionista(Professionista professionista) {
		this.professionista = professionista;
	}
	
}
