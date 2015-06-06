package com.lpoo.MiniGolf.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.lpoo.MiniGolf.logic.Course;

public class GameIO {
	ObjectOutputStream out;
	ObjectInputStream in;
	Course course = new Course();
	ArrayList<Course> courses = new ArrayList<Course>();

	public GameIO(Course course) {
		out = null;
		in = null;
		this.course = course;
	}

	public GameIO(ArrayList<Course> courses) {
		out = null;
		in = null;
		this.courses = courses;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}

	public void saveIndividualCourses(Course course) {

		try {
			// Serializing data object to a file
			out = new ObjectOutputStream(new FileOutputStream(course.getNome()));
			out.writeObject(course);
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveAllCourses(ArrayList<Course> courses) {

		try {
			// Serializing data object to a file
			out = new ObjectOutputStream(new FileOutputStream("AllCourses.sav"));
			out.writeObject(courses);
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Course loadIndividualCourses() {
		//
		// try {
		// for(int i=0;i<5;i++)
		// File test = new File("Courses.sav");
		// if (!test.exists()) {
		// break;
		// }
		// FileInputStream stuff = new FileInputStream("Courses.sav");
		// in = new ObjectInputStream(stuff);
		// this.course = (Course) in.readObject();
		// return course;
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		// return course;
		return null;
	}

	public Course loadAllCourses() throws ClassNotFoundException {

		try {
			File test = new File("Maze.sav");
			if (!test.exists()) {
				return null;
			}
			FileInputStream stuff = new FileInputStream("Maze.sav");
			in = new ObjectInputStream(stuff);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}