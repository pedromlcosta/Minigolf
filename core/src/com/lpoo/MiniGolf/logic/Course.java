package com.lpoo.MiniGolf.logic;

import java.util.ArrayList;

public class Course {
	ArrayList<Element> elementos;

	public Course() {
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

}
