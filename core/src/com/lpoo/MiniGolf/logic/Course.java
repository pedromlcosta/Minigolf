package com.lpoo.MiniGolf.logic;

import java.util.ArrayList;

public class Course {
	
	private ArrayList<Element> elementos;
	private String nome;

	public Course() {
		elementos = new ArrayList<Element>();
	}

	public Course(ArrayList<Element> elementos) {
		this.elementos = elementos;
	}

	public void addEle(Element ele) {
		elementos.add(ele);
	}

	public Element getElement(int i) {
		if (i > elementos.size() - 1)
			return null;

		return elementos.get(i);
	}

	public ArrayList<Element> getElementos() {
		return elementos;
	}

	public void setElementos(ArrayList<Element> elementos) {
		this.elementos = elementos;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
