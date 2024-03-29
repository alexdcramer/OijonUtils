package net.oijon.utils.parser.data;

import java.util.ArrayList;
import java.util.Arrays;

//last edit: 5/23/23 -N3

/**
 * Like an IPA table, but readable in Java
 * @author alex
 *
 */
public class PhonoTable {

	private String name;
	private ArrayList<String> columnNames;
	private ArrayList<PhonoCategory> rows;
	private int soundsPerCell;
	
	/**
	 * Creates a PhonoTable
	 * @param name The name of the PhonoTable (Consonants, Vowels, etc.)
	 * @param columnNames The names of the columns
	 * @param rows An ArrayList of all the rows to be added
	 * @param soundsPerCell How many sounds should be in a cell
	 */
	public PhonoTable(String name, ArrayList<String> columnNames, ArrayList<PhonoCategory> rows, int soundsPerCell) {
		this.name = name;
		this.columnNames = columnNames;
		this.rows = rows;
		this.soundsPerCell = soundsPerCell;
	}
	
	/**
	 * Copy constructor
	 * @param pt The PhonoTable to be copied
	 */
	public PhonoTable(PhonoTable pt) {
		this.name = pt.name;
		this.columnNames = new ArrayList<String>(pt.columnNames);
		this.rows = new ArrayList<PhonoCategory>(pt.rows);
		this.soundsPerCell = pt.soundsPerCell;
	}
	
	/**
	 * Converts a PhonoTable to a string
	 */
	public String toString() {
		String returnString = "===PhonoTable Start===\ntableName:" + name + "\ncolumnNames:";
		for (int i = 0; i < columnNames.size(); i++) {
			returnString += columnNames.get(i) + ",";
		}
		returnString = returnString.substring(0, returnString.length() - 1); // removes last comma
		returnString += "\nsoundsPerCell:" + soundsPerCell;
		returnString += "\nrowNames:";
		for (int i = 0; i < rows.size(); i++) {
			returnString +=rows.get(i).getName() + ",";
		}
		returnString = returnString.substring(0, returnString.length() - 1); // removes last comma
		returnString += "\n";
		for (int i = 0; i < rows.size(); i++) {
			returnString += ":";
			for (int j = 0; j < rows.get(i).size(); j++) {
				returnString += rows.get(i).getSound(j);
			}
			returnString += "\n";
		}
		returnString += "===PhonoTable End===";
		return returnString;
	}
	
	/**
	 * Gets the name of the PhonoTable
	 * @return the name of the PhonoTable
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the amount of rows in a PhonoTable
	 * @return The amount of rows in a PhonoTable
	 */
	public int size() {
		return rows.size();
	}
	
	/**
	 * Gets the row in a PhonoTable at index i
	 * @param i The index number
	 * @return The row at index i
	 */
	public PhonoCategory getRow(int i) {
		return rows.get(i);
	}
	
	/**
	 * Gets a list of all the column names
	 * @return The column names
	 */
	public ArrayList<String> getColumnNames() {
		return columnNames;
	}
	
	/**
	 * Gets the amount of data per cell
	 * @return The amount of sounds per cell
	 */
	public int dataPerCell() {
		return soundsPerCell;
	}
	
	/**
	 * Returns true
	 * @deprecated as of v1.1.2, as this returns true always and verifying a PhonoTable does not make all that much sense. However, some code somewhere might still rely on it.
	 */
	@Deprecated
	public boolean verify() {
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PhonoTable) {
			PhonoTable p = (PhonoTable) obj;
			if (p.name.equals(name) & p.columnNames.equals(columnNames) & p.rows.equals(rows) &
					p.soundsPerCell == soundsPerCell) {
				return true;
			}
			
		}
		return false;
	}
}
