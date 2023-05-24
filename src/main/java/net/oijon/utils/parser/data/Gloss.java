package net.oijon.utils.parser.data;

//last edit: 5/24/23 -N3

/**
 * Attaches a meaning to a gloss abbreviation, allowing users to create their
 * own, or see what a certain abbreviation means.
 * 
 * @author alex
 *
 */
public class Gloss {

	private String abbreviation;
	private String meaning;
	
	/**
	 * Creates a Gloss based off an abbreviation and a plaintext meaning
	 * @param abbreviation The abbreviation of the gloss
	 * @param meaning What the gloss means
	 */
	public Gloss(String abbreviation, String meaning) {
		this.abbreviation = abbreviation;
		this.meaning = meaning;
	}
	
	/**
	 * Copy constructor
	 * @param gloss The gloss to copy
	 */
	public Gloss(Gloss gloss) {
		this.abbreviation = gloss.abbreviation;
		this.meaning = gloss.meaning;
	}
	
	/**
	 * Gets the abbreviation of the gloss
	 * @return The abbreviation of the gloss
	 */
	public String getAbbreviation() {
		return abbreviation;
	}
	
	/**
	 * Gets the meaning of the gloss
	 * @return The meaning of the gloss
	 */
	public String getMeaning() {
		return meaning;
	}
	
	/**
	 * Sets the abbreviation of the gloss
	 * @param abbreviation The new abbreviation for the gloss
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	/**
	 * Sets the meaning of the gloss
	 * @param meaning The new meaning of the gloss
	 */
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	
	@Override
	public String toString() {
		String returnString = "===Gloss Start===\n";
		returnString += "abb:" + abbreviation + "\n";
		returnString += "meaning:" + meaning + "\n";
		returnString += "===Gloss End===";
		return returnString;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Gloss) {
			Gloss g = (Gloss) o;
			if (g.getAbbreviation().equals(abbreviation) & g.getMeaning().equals(meaning)) {
				return true;
			}
		}
		return false;
	}

}
