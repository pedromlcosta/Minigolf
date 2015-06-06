package com.lpoo.MiniGolf.logic;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Course implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Element> elementos;
	private ArrayList<Vector2> positions;
	private String nome;

	public Course() {
		elementos = new ArrayList<Element>();
		positions = new ArrayList<Vector2>();
	}

	public Course(ArrayList<Element> elementos) {
		this.elementos = elementos;
		positions = new ArrayList<Vector2>();
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

	public void addCourseElement(Element ele) {
		elementos.add(ele);
	}

	public ArrayList<Vector2> getPositions() {
		return positions;
	}

	public void setPositions(ArrayList<Vector2> positions) {
		this.positions = positions;
	}
	
	public void addPosition(Vector2 pos){
		positions.add(pos);
	}

}
