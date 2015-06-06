package com.lpoo.MiniGolf.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.lpoo.MiniGolf.logic.Course;

public class GameIO {
	ObjectOutputStream out;
	ObjectInputStream in;
	Course course = new Course();
	ArrayList<Course> courses = new ArrayList<Course>();

	public GameIO() {
		out = null;
		in = null;
		this.course = null;
		this.courses = null;
		System.out.print(Paths.get(".").toAbsolutePath().normalize().toString());
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

	public void saveIndividualCourse(Course course) {

		try {
			// Serializing data object to a file
			out = new ObjectOutputStream(new FileOutputStream(course.getNome()));
			out.writeObject(course);
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveAllIndividualCourses(ArrayList<Course> courses) {

		for (int i = 0; i < courses.size(); i++) {
			try {
				// Serializing data object to a file
				out = new ObjectOutputStream(new FileOutputStream(courses.get(i).getNome()));
				out.writeObject(courses.get(i));
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
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

	public ArrayList<Course> loadAllIndividualCourses() throws ClassNotFoundException {
		int i = 0;
		try {
			while (true) {
				String fileName = "course" + i + ".sav";
				File test = new File(fileName);
				if (test.exists()) {
					FileInputStream stuff = new FileInputStream(fileName);
					in = new ObjectInputStream(stuff);
					this.courses = (ArrayList<Course>) in.readObject();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return courses;

	}

	public ArrayList<Course> loadAllCourses() throws ClassNotFoundException {

		try {
			File test = new File("Maze.sav");
			if (!test.exists()) {
				return null;
			}
			FileInputStream stuff = new FileInputStream("Maze.sav");
			in = new ObjectInputStream(stuff);
			this.courses = (ArrayList<Course>) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return courses;

	}

}