package net.oijon.utils.parser.data;

/**
 * Stores a tag in a .language file
 * @author alex
 *
 */

//last edit: 4/29/23 -N3

public class Tag {
	
	static Tag NULL = new Tag("", "");
	private String name = "";
	private String data = "";

	/**
	 * Creates a tag
	 * @param name The name of the tag (First part before the :)
	 * @param data The data the tag contains (Second part after the :)
	 */
	public Tag(String name, String data) {
		this.name = name;
		this.data = data;
	}
	
	/**
	 * Creates a tag
	 * @param tag The tag in string format (name:data)
	 */
	public Tag(String tag) {
		String[] split = tag.split(":");
		if (split.length == 2) {
			this.name = split[0];
			this.data = split[1];
		} else if (split[0].charAt(split[0].length() - 1) == ':') {
			this.name = split[0].substring(0, split[0].length() - 1);
		} else if (split.length == 1) {
			this.name = split[0];
		}
	}
	
	/**
	 * Copy constructor
	 * @param tag The tag to be copied
	 */
	public Tag(Tag tag) {
		this.name = tag.name;
		this.data = tag.data;
	}
	
	/**
	 * Gets the name of the tag
	 * @return The name of the tag
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the tag.
	 * @param name The new name
	 */
	protected void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the data of a tag
	 * @return The data of a tag
	 */
	public String value() {
		return data;
	}
	
	/**
	 * Sets the data of a tag
	 * @param data The new data for the tag
	 */
	public void set(String data) {
		this.data = data;
	}
	
	/**
	 * Converts a tag to a string
	 */
	public String toString() {
		String returnString = this.name + ":" + this.data;
		return returnString;
	}
	
	/**
	 * Checks if two tags share the same name.
	 * @param tag The tag to be checked against
	 * @return true if the names are the same, false otherwise
	 */
	public boolean isSameTag(Tag tag) {
		if (this.name.equals(tag.getName())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if two tags are equal
	 * @param tag The tag to check
	 * @return true if equal, false otherwise
	 * @deprecated as of v1.2.0, as this should have been handled via equals() instead.
	 */
	@Deprecated
	public boolean isEqual(Tag tag) {
		return this.equals(tag);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tag) {
			Tag tag = (Tag) obj;
			if (this.name.equals(tag.getName())) {
				if (this.data.equals(tag.value())) {
					return true;
				}
			}
		}
		return false;
	}
	
}
