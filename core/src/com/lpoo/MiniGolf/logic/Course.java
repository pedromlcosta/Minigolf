package com.lpoo.MiniGolf.logic;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

// TODO: Auto-generated Javadoc
/**
 * Class representing a minigolf course, with all its obstacles and ball starting positions.
 */
public class Course implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The elements/obstacles of the course. */
	private ArrayList<Element> elementos;
	
	/** The positions where the players will start. */
	private ArrayList<Vector2> positions;
	
	/** The name of this course. Usually "Course" + id */
	private String nome;

	/**
	 * Instantiates a new course. with the obstacles and positions.
	 */
	public Course() {
		elementos = new ArrayList<Element>();
		positions = new ArrayList<Vector2>();
	}

	/**
	 * Instantiates a new course.
	 *
	 * @param elementos the obstacles that will figure in the course
	 */
	public Course(ArrayList<Element> elementos) {
		this.elementos = elementos;
		positions = new ArrayList<Vector2>();
	}

	/**
	 * Adds a element to the course.
	 *
	 * @param ele the element to be added.
	 */
	public void addEle(Element ele) {
		elementos.add(ele);
	}

	/**
	 * Gets the element.
	 *
	 * @param i index of the element wanted
	 * @return the element with the index i
	 */
	public Element getElement(int i) {
		if (i > elementos.size() - 1)
			return null;

		return elementos.get(i);
	}

	/**
	 * Gets the array with all the elements.
	 *
	 * @return the ArrayList with the elements of the course.
	 */
	public ArrayList<Element> getElementos() {
		return elementos;
	}

	/**
	 * Sets the elementos.
	 *
	 * @param elementos the new array of elements of the course
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
	 * Adds an element to the course
	 *
	 * @param ele the element to be added.
	 */
	public void addCourseElement(Element ele) {
		elementos.add(ele);
	}

	/**
	 * Gets the starting positions of the players.
	 *
	 * @return the starting positions of the players
	 */
	public ArrayList<Vector2> getPositions() {
		return positions;
	}

	/**
	 * Sets the starting positions of the players.
	 *
	 * @param positions the new starting positions of the players
	 */
	public void setPositions(ArrayList<Vector2> positions) {
		this.positions = positions;
	}
	
	/**
	 * Adds the position to the array.
	 *
	 * @param pos the position of a player
	 */
	public void addPosition(Vector2 pos){
		positions.add(pos);
	}

}
