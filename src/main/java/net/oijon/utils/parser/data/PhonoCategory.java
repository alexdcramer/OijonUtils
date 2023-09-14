package net.oijon.utils.parser.data;

import java.util.ArrayList;

//last edit: 5/23/23 -N3

/**
 * Creates the equivalent of a row on the IPA chart.
 * @author alex
 *
 */
public class PhonoCategory {

	/**
	 * * marks possible sounds with no symbol
	 * # marks impossible sounds
	 */
	
	private String name;
	private ArrayList<String> sounds; 
	
	/**
	 * Creates phono category for already created list
	 * @param name the name of the category
	 * @param sounds a pre-existing ArrayList of each sound
	 */
	public PhonoCategory(String name, ArrayList<String> sounds) {
		this.name = name;
		this.sounds = sounds;
	}
	
	/**
	 * Creates phono category for as-of-yet created list
	 * @param name the name of the category
	 */
	public PhonoCategory(String name) {
		this.name = name;
		this.sounds = new ArrayList<String>();
	}
	
	/**
	 * Copy constructor
	 * @param pc The PhonoCategory to be copied
	 */
	public PhonoCategory(PhonoCategory pc) {
		this.name = pc.name;
		this.sounds = new ArrayList<String>(pc.sounds);		
	}
	
	/**
	 * Gets list of all sounds in category
	 * @return all sounds in category
	 */
	public ArrayList<String> getSounds() {
		return sounds;
	}
	
	/**
	 * Gets category name
	 * @return category name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets sound at index i
	 * @param i index
	 * @return sound
	 */
	public String getSound(int i) {
		return sounds.get(i);
	}
	
	/**
	 * Deletes sound 
	 * @param i index of sound to be deleted
	 */
	public void removeSound(int i) {
		sounds.remove(i);
	}
	
	/**
	 * Adds sound to end of list
	 * @param sound the sound to be added
	 */
	public void addSound(String sound) {
		sounds.add(sound);
	}
	
	/**
	 * Returns the amount of sounds in a phono category
	 * @return The amount of sounds
	 */
	public int size() {
		return sounds.size();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PhonoCategory) {
			PhonoCategory p = (PhonoCategory) obj;
			if (p.name.equals(name) & p.sounds.equals(sounds)) {
				return true;
			}
		}
		return false;
	}
}
