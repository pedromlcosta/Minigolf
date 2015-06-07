package com.lpoo.MiniGolf.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.lpoo.MiniGolf.logic.Course;
import com.lpoo.MiniGolf.logic.MiniGolf;

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

		// CREATE SAVE DIRECTORY
		File save_dir = new File("saved_courses");
		// if the directory does not exist, create it
		if (!save_dir.exists()) {
			try {
				save_dir.mkdir();
			} catch (SecurityException se) {
				// No permissions to create folder
			}
		}

		// SAVE TO THE DIRECTORY
		for (int i = 0; i < MiniGolf.getAllCourses().size(); i++) {
			try {
				// Serializing data object to a file
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("saved_courses\\Course" + i));
				out.writeObject(MiniGolf.getAllCourses().get(i));
				System.out.println("wrote object");
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// WORKS!! DO NOT REMOVE!
	public void saveAllCourses(ArrayList<Course> courses) {
		FileHandle file = Gdx.files.local("AllCourses.sav");
		OutputStream output = null;
		try {
			file.writeBytes(serialize(courses), false);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (Exception e) {

				}
		}

	}

	public ArrayList<Course> loadAllCourses() throws ClassNotFoundException {
		ArrayList<Course> tempCourses = null;
		FileHandle file = Gdx.files.local("AllCourses.sav");
		try {
			if (file.exists()) {
				System.out.println("Haaaai");
				tempCourses = (ArrayList<Course>) deserialize(file.readBytes());
			}else{
				System.out.println(Gdx.files.getLocalStoragePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempCourses;

	}

	// before version
	// out = new ObjectOutputStream(new FileOutputStream("AllCourses.sav"));
	// out.writeObject(courses);
	// out.close();

	public ArrayList<Course> loadAllIndividualCourses() throws ClassNotFoundException {
		ArrayList<Course> tempCourses = new ArrayList<Course>();

		File folder = new File("saved_courses");

		File[] listOfFiles = folder.listFiles();
		if (listOfFiles != null) {
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String fileName = listOfFiles[i].getName();
					try {
						ObjectInputStream in = new ObjectInputStream(new FileInputStream("saved_courses\\" + fileName));
						Course c = (Course) in.readObject();
						System.out.println("read obj");
						tempCourses.add(c);
						in.close();
					} catch (Exception e) {
						System.out.println("Exception: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}

		return tempCourses;

	}

	// WORKS!! DO NOT REMOVE!
	// public ArrayList<Course> loadAllCourses() throws ClassNotFoundException {
	//
	// try {
	// File test = new File("AllCourses.sav");
	// if (!test.exists()) {
	// return null;
	// }
	// FileInputStream stuff = new FileInputStream("AllCourses.sav");
	// in = new ObjectInputStream(stuff);
	// in.defaultReadObject();
	// this.courses = (ArrayList<Course>) in.readObject();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return courses;
	//
	// }

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		
		o.writeObject(obj);
		return b.toByteArray();
	}

	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}

}