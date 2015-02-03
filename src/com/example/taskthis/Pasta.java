package com.example.taskthis;

public enum Pasta {
	INBOX("Inbox"), NEXT("Next"), COMMITMENT("Commitment"), WAITING("Waiting"),
	PROJECT("Project");

	public String valor;

	Pasta(String valor) {
		this.valor = valor;
	}
	
	public String getValor() {
		return valor;
	}
}
