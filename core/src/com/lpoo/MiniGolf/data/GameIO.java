package com.lpoo.MiniGolf.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.lpoo.MiniGolf.logic.Course;
import com.lpoo.MiniGolf.logic.MiniGolf;

/**
 * The Class GameIO, which handles saves and loads.
 */
public class GameIO {

	/** The object output stream, to write the serialized version to a file. */
	ObjectOutputStream out;

	/** The object input stream, to read the deserialized version from a file. */
	ObjectInputStream in;

	/** The individual course, to be handled by the loads and saves. */
	Course course = new Course();

	/** The array with all the courses, to be handled by the loads and saves. */
	ArrayList<Course> courses = new ArrayList<Course>();

	/**
	 * Instantiates a new GameIO class.
	 */
	public GameIO() {
		out = null;
		in = null;
		this.course = null;
		this.courses = null;
	}

	/**
	 * Instantiates a new GameIO class.
	 *
	 * @param courses
	 *            the courses to be handled.
	 */
	public GameIO(ArrayList<Course> courses) {
		out = null;
		in = null;
		this.courses = courses;
	}

	/**
	 * Gets the object output stream.
	 *
	 * @return the object output stream.
	 */
	public ObjectOutputStream getOut() {
		return out;
	}

	/**
	 * Sets the object output stream.
	 *
	 * @param out
	 *            the new object output stream.
	 */
	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	/**
	 * Gets the object input stream.
	 *
	 * @return the object input stream.
	 */
	public ObjectInputStream getIn() {
		return in;
	}

	/**
	 * Sets the object input stream.
	 *
	 * @param in
	 *            the new object input stream.
	 */
	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	/**
	 * Gets the course to be handled.
	 *
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}

	/**
	 * Sets the course to be handled.
	 *
	 * @param course
	 *            the new course
	 */
	public void setCourse(Course course) {
		this.course = course;
	}

	/**
	 * Gets the courses to be handled.
	 *
	 * @return the courses
	 */
	public ArrayList<Course> getCourses() {
		return courses;
	}

	/**
	 * Sets the courses to be handled.
	 *
	 * @param courses
	 *            the new courses
	 */
	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}

	/**
	 * Save an individual course to a file, inside a folder in the local
	 * directory. If the folder doesn't exist, it is created.
	 *
	 * @param course
	 *            the course to be saved to the file
	 */
	public void saveIndividualCourse(Course course) {

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

		try {
			// Serializing data object to a file
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("saved_courses\\" + course.getNome()));
			out.writeObject(course);
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Save all individual courses, one by one, inside a folder in the local
	 * directory. If the folder doesn't exist, it is created.
	 *
	 * @param courses
	 *            the courses to be saved to files
	 */
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

	/**
	 * Load all individual courses, one by one, from inside the save directory.
	 *
	 * @return the array list with all the courses loaded
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
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


}