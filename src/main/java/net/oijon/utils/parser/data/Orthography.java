package net.oijon.utils.parser.data;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Orthography {

	private Phonology ph = new Phonology();
	private ArrayList<String[]> orthoList = new ArrayList<String[]>();
	
	// TODO: allow ortho rules to have exceptions
	
	/**
	 * Creates an empty orthography
	 */
	public Orthography() {
		
	}
	
	public Orthography(Phonology ph) {
		this.ph = ph;
	}
	
	public void add(String phonemes, String ortho) {
		// TODO: check if phonemes are actually in phonology
		String[] valueArray = {phonemes, ortho};
		orthoList.add(valueArray);
		sortOrthoList();
	}
	
	public Phonology getPhono() {
		return ph;
	}
	
	public void setPhono(Phonology p) {
		this.ph = p;
	}
	
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
	
	public String orthoGuess(String input) {
		String returnString = input;
		for (int i = 0; i < orthoList.size(); i++) {
			String phonemes = orthoList.get(i)[0];
			String ortho = orthoList.get(i)[1];
			returnString = returnString.replaceAll(phonemes, ortho);
		}
		return returnString;
	}
	
	public String phonoGuess(String input) {
		String returnString = new String(input);
		for (int i = 0; i < orthoList.size(); i++) {
			String phonemes = orthoList.get(i)[0];
			String ortho = orthoList.get(i)[1];
			returnString = returnString.replaceAll(ortho, phonemes);
		}
		return returnString;
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
