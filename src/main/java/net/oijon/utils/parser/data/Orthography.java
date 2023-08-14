package net.oijon.utils.parser.data;

import java.util.ArrayList;

//last edit: 5/24/23 -N3

/**
 * The writing system of a language. Connects phonemes to graphemes, allowing
 * a user to convert between phonetic transcription and standard writing system.
 * @author alex
 *
 */
public class Orthography {

	private Phonology ph = new Phonology();
	private ArrayList<String[]> orthoList = new ArrayList<String[]>();
	
	// TODO: allow ortho rules to have exceptions
	
	/**
	 * Creates an empty orthography
	 */
	public Orthography() {
		
	}
	
	/**
	 * Creates an orthography with a set phonology
	 * @param ph The phonology to be used
	 */
	public Orthography(Phonology ph) {
		this.ph = ph;
	}
	
	/**
	 * Copy constructor
	 * @param o The orthography to copy
	 */
	public Orthography(Orthography o) {
		this.ph = new Phonology(o.ph);
		this.orthoList = new ArrayList<String[]>(o.orthoList);
	}
	
	public void add(String phonemes, String ortho) {
		// TODO: check if phonemes are actually in phonology
		String[] valueArray = {phonemes, ortho};
		orthoList.add(valueArray);
		sortOrthoList();
	}
	
	/**
	 * Gets the phonology used
	 * @return The phonology in question
	 */
	public Phonology getPhono() {
		return ph;
	}
	
	/**
	 * Sets a new phonology for the orthography to use
	 * @param p The new phonology to use
	 */
	public void setPhono(Phonology p) {
		this.ph = p;
	}
	
	/**
	 * Sorts the list
	 */
	private void sortOrthoList() {
		for (int i = 1; i < orthoList.size(); i++) {
			if (orthoList.get(i)[1].length() > orthoList.get(i - 1)[1].length()) {
				String[] tempval1 = orthoList.get(i);
				String[] tempval2 = orthoList.get(i - 1);
				orthoList.set(i, tempval2);
				orthoList.set(i - 1, tempval1);
				sortOrthoList();
			}
		}
	}
	
	/**
	 * Guesses what the graphemes will be given phonemes
	 * @param input The phonemes to use
	 * @return A guess on graphemes
	 */
	public String orthoGuess(String input) {
		String returnString = input;
		for (int i = 0; i < orthoList.size(); i++) {
			String phonemes = orthoList.get(i)[0];
			String ortho = orthoList.get(i)[1];
			returnString = returnString.replaceAll(phonemes, ortho);
		}
		return returnString;
	}
	
	/**
	 * Guesses what the phonemes will be given graphemes
	 * @param input The graphemes to use
	 * @return A guess on phonemes
	 */
	public String phonoGuess(String input) {
		String returnString = new String(input);
		for (int i = 0; i < orthoList.size(); i++) {
			String phonemes = orthoList.get(i)[0];
			String ortho = orthoList.get(i)[1];
			returnString = returnString.replaceAll(ortho, phonemes);
		}
		return returnString;
	}
	
	/**
	 * Gets a pair, with 0 being the phonemes, and 1 being the graphemes
	 * @param i The index of the pair in the orthography
	 * @return The pair specified
	 */
	public String[] getPair(int i) {
		return orthoList.get(i);
	}
	
	/**
	 * Removes a pair from an orthography based on index
	 * @param i The index of the pair to be removed
	 */
	public void remove(int i) {
		orthoList.remove(i);
	}
	
	public String toString() {
		sortOrthoList();
		String returnString = "===Orthography Start===\n";
		for (int i = 0; i < orthoList.size(); i++) {
			returnString += orthoList.get(i)[0] + ":" + orthoList.get(i)[1] + "\n";
		}
		returnString += "===Orthography End===";
		
		
		return returnString;
	}
	
	/**
	 * Gets the size of the Orthography
	 * @return The amount of orthography pairs
	 */
	public int size() {
		return orthoList.size();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Orthography) {
			Orthography o = (Orthography) obj;
			for (int i = 0; i < orthoList.size(); i++) {
				for (int j = 0; j < orthoList.get(i).length; j++) {
					if (!orthoList.get(i)[j].equals(o.orthoList.get(i)[j])) {
						System.out.println("Expected " + orthoList.get(i)[j] + ", got " +
								o.orthoList.get(i)[j]);
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
}
