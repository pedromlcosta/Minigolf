package com.lpoo.MiniGolf.logic;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

// TODO: Auto-generated Javadoc
/**
 * The Class Course.
 */
public class Course implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The elementos. */
	private ArrayList<Element> elementos;
	
	/** The positions. */
	private ArrayList<Vector2> positions;
	
	/** The nome. */
	private String nome;

	/**
	 * Instantiates a new course.
	 */
	public Course() {
		elementos = new ArrayList<Element>();
		positions = new ArrayList<Vector2>();
	}

	/**
	 * Instantiates a new course.
	 *
	 * @param elementos the elementos
	 */
	public Course(ArrayList<Element> elementos) {
		this.elementos = elementos;
		positions = new ArrayList<Vector2>();
	}

	/**
	 * Adds the ele.
	 *
	 * @param ele the ele
	 */
	public void addEle(Element ele) {
		elementos.add(ele);
	}

	/**
	 * Gets the element.
	 *
	 * @param i the i
	 * @return the element
	 */
	public Element getElement(int i) {
		if (i > elementos.size() - 1)
			return null;

		return elementos.get(i);
	}

	/**
	 * Gets the elementos.
	 *
	 * @return the elementos
	 */
	public ArrayList<Element> getElementos() {
		return elementos;
	}

	/**
	 * Sets the elementos.
	 *
	 * @param elementos the new elementos
	 */
	public void setElementos(ArrayList<Element> elementos) {
		this.elementos = elementos;
	}

	/**
	 * Gets the nome.
	 *
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Sets the nome.
	 *
	 * @param nome the new nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Adds the course element.
	 *
	 * @param ele the ele
	 */
	public void addCourseElement(Element ele) {
		elementos.add(ele);
	}

	/**
	 * Gets the positions.
	 *
	 * @return the positions
	 */
	public ArrayList<Vector2> getPositions() {
		return positions;
	}

	/**
	 * Sets the positions.
	 *
	 * @param positions the new positions
	 */
	public void setPositions(ArrayList<Vector2> positions) {
		this.positions = positions;
	}
	
	/**
	 * Adds the position.
	 *
	 * @param pos the pos
	 */
	public void addPosition(Vector2 pos){
		positions.add(pos);
	}

}
