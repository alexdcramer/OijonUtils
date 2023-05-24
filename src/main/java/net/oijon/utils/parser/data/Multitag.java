package net.oijon.utils.parser.data;

import java.util.ArrayList;

//last edit: 5/23/23 -N3

/**
 * A tag that can have child tags
 * @author alex
 *
 */
public class Multitag {

	private String name;
	private ArrayList<Tag> subtags = new ArrayList<Tag>();
	private ArrayList<Multitag> subMultitags = new ArrayList<Multitag>();
	
	/**
	 * Creates a multitag based on its name and children
	 * @param name The name of the multitag
	 * @param subtags Tag objects that are a part of this multitag
	 * @param subMultitags Multitag objects that are a part of this multitag
	 */
	public Multitag(String name, ArrayList<Tag> subtags, ArrayList<Multitag> subMultitags) {
		this.name = name;
	}
	/**
	 * Creates a multitag based on its children
	 * @param subtags Tag objects that are a part of this multitag
	 * @param subMultitags Multitag objects that are a part of this multitag
	 */
	public Multitag(ArrayList<Tag> subtags, ArrayList<Multitag> subMultitags) {
		this.subtags = subtags;
		this.subMultitags = subMultitags;
	}
	/**
	 * Creates a multitag based on name
	 * @param name The name of the multitag
	 */
	public Multitag(String name) {
		this.name = name;
	}
	/**
	 * Copy constructor
	 * @param m The multitag to copy
	 */
	public Multitag(Multitag m) {
		this.name = m.name;
		this.subtags = m.subtags;
		this.subMultitags = m.subMultitags;
	}
	
	/**
	 * Gets the name of the multitag
	 * @return the name of the multitag
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name of the multitag
	 * @param name the new name of the multitag
	 */
	protected void setName(String name) {
		this.name = name;
	}
	/**
	 * Gets a list of all direct child Tag objects
	 * @return A list of all direct child Tag objects
	 */
	public ArrayList<Tag> getSubtags() {
		return subtags;
	}
	/**
	 * Gets a list of all direct child Multitag objects
	 * @return A list of all direct child Multitag objects
	 */
	public ArrayList<Multitag> getSubMultitags() {
		return subMultitags;
	}
	/**
	 * Adds a tag as a child
	 * @param tag The child tag to add
	 */
	public void addTag(Tag tag) {
		subtags.add(tag);
	}
	/**
	 * Adds a multitag as a child
	 * @param tag The child multitag to add
	 */
	public void addMultitag(Multitag tag) {
		subMultitags.add(tag);
	}
	/**
	 * Checks if two multitags are the same type
	 * @param tag The multitag to check
	 * @return true if same type of tag, false otherwise
	 */
	public boolean isSameTag(Multitag tag) {
		if (this.name.equals(tag.getName())) {
			return true;
		} else {
			return false;
		}
	}
	@Deprecated
	/**
	 * use equals() instead.
	 * @param tag
	 * @return
	 * @deprecated as of 1.2.1, use equals() instead.
	 */
	public boolean isEqual(Multitag tag) {		
		return this.equals(tag);
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Multitag) {
			Multitag tag = (Multitag) obj;
			if (this.name.equals(tag.getName())) {
				if (this.subtags.equals(tag.getSubtags())) {
					if (this.subMultitags.equals(tag.getSubMultitags())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public String toString() {
		String returnString = this.getStart() + "\n";
		for (int i = 0; i < subtags.size(); i++) {
			returnString += subtags.get(i).toString() + "\n";
		}
		for (int i = 0; i < subMultitags.size(); i++) {
			returnString += subMultitags.get(i).toString() + "\n";
		}
		returnString += this.getEnd();
		return returnString;
	}
	/**
	 * Gets the start of a tag's string
	 * @return
	 */
	public String getStart() {
		return "===" + this.name + " Start===";
	}
	/**
	 * Gets the end of a tag's string
	 * @return
	 */
	public String getEnd() {
		return "===" + this.name + " End===";
	}
	/**
	 * Gets a descendent multitag based on name. Child tag does not need to be direct.
	 * @param name The name to search for
	 * @return The multitag searched for
	 * @throws Exception Thrown if no multitag was found
	 */
	public Multitag getMultitag(String name) throws Exception {
		for (int i = 0; i < subMultitags.size(); i++) {
			if (subMultitags.get(i).getName().equals(name)) {
				return subMultitags.get(i);
			}
		}
		for (int i = 0; i < subMultitags.size(); i++) {
			try {
				Multitag tag = subMultitags.get(i).getMultitag(name);
				return tag;
			} catch (Exception e) {
				continue;
			}
		}
		throw new Exception("Child multitag " + name + " not found in tag " + this.name + ".");
	}
	
	/**
	 * Gets a direct child tag based off name.
	 * @param name The name of the tag to search for
	 * @return The tag searched for
	 * @throws Exception thrown if there is no tag with that name
	 */
	public Tag getDirectChild(String name) throws Exception {
		for (int i = 0; i < subtags.size(); i++) {
			if (subtags.get(i).getName().equals(name)) {
				return subtags.get(i);
			}
		}
		throw new Exception("Child tag " + name + " not found in tag " + this.name + ".");
	}
	/**
	 * Gets data attached to a multitag but not exactly attached to any subtag
	 * @return All unattached data
	 */
	public ArrayList<Tag> getUnattachedData() {
		ArrayList<Tag> returnList = new ArrayList<Tag>();
		for (int i = 0; i < subtags.size(); i++) {
			if (subtags.get(i).getName().equals("")) {
				returnList.add(subtags.get(i));
			}
		}
		return returnList;
		
	}
	
	/**
	 * Gets a descendent tag based off name. Does not need to be a direct child
	 * @param name The name of the tag
	 * @return The tag searched for
	 * @throws Exception thrown if a tag by the given name cannot be found
	 */
	public Tag getTag(String name) throws Exception {
		for (int i = 0; i < subtags.size(); i++) {
			if (subtags.get(i).getName().equals(name)) {
				return subtags.get(i);
			}
		}
		for (int i = 0; i < subMultitags.size(); i++) {
			try {
				Tag tag = subMultitags.get(i).getTag(name);
				return tag;
			} catch (Exception e) {
				continue;
			}
		}
		throw new Exception("Child tag " + name + " not found.");
	}
	
	
}
