package net.oijon.utils.parser.data;

import java.util.ArrayList;

public class GlossList extends ArrayList<Gloss> {

	private static final long serialVersionUID = 2940848265098898582L;
	private String name;
	private ArrayList<Gloss> glosses;
	
	/**
	 * Creates an empty GlossList
	 */
	public GlossList(String name) {
		super();
		this.name = name;
	}
	
	/**
	 * Copy constructor
	 * @param gl
	 */
	public GlossList(GlossList gl) {
		super(gl);
		this.name = gl.name;
	}
	
	/**
	 * Gets the name of the GlossList
	 * @return The name of the GlossList
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the GlossList
	 * @param name The name of the GlossList
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		String returnString = "===GlossList Start===\n";
		returnString += "name:" + name + "\n";
		returnString += "===Glosses Start===\n";
		for (int i = 0; i < glosses.size(); i++) {
			returnString += glosses.get(i).toString() + "\n";
		}
		returnString += "===Glosses End===\n";
		returnString += "===GlossList End===";
		return returnString;
	}
	
}
