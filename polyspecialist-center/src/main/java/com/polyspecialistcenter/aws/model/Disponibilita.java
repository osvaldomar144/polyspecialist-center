package com.polyspecialistcenter.aws.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Disponibilita {
	
	public static final String DIR_PAGES_DISP = "information/disponibilita/";
	public static final String DIR_ADMIN_PAGES_DISP = "admin/disponibilita/";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String data;
	
	@NotBlank
	private String oraInizio;
	
	@NotBlank
	private String oraFine;
	
	@ManyToOne
	private Professionista professionista;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOraInizio() {
		return oraInizio;
	}

	public void setOraInizio(String oraInizio) {
		this.oraInizio = oraInizio;
	}

	public String getOraFine() {
		return oraFine;
	}

	public void setOraFine(String oraFine) {
		this.oraFine = oraFine;
	}

	public Professionista getProfessionista() {
		return professionista;
	}

	public void setProfessionista(Professionista professionista) {
		this.professionista = professionista;
	}
	
}
