package net.oijon.utils.parser.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import net.oijon.utils.logger.Log;
import net.oijon.utils.parser.Parser;

//last edit: 8/14/23 -N3

/**
 * A way to transcribe all sounds allowed in a vocal tract. IPA is specified here as that
 * is a standard for human sounds, however PhonoSystems can be created for non-human
 * sounds as well.
 * @author alex
 *
 */
public class PhonoSystem {

	private String name;
	private ArrayList<PhonoTable> tables = new ArrayList<PhonoTable>();
	private ArrayList<String> diacriticList = new ArrayList<String>();
	
	static Log log = Parser.getLog();
	
	/**
	 * Creates an IPA preset. Useful when we just want the default PhonoSystem.
	 */
	
	public static final PhonoSystem IPA = new PhonoSystem("IPA", new ArrayList<PhonoTable>(
			Arrays.asList(PhonoTable.IPAConsonants, PhonoTable.IPAVowels, PhonoTable.IPAOther)), PhonoCategory.IPADiacritics);
	/**
	 * Creates a PhonoSystem object with a pre-defined ArrayList
	 * @param name The name of the phono system
	 * @param categories The pre-defined ArrayList
	 * @param columnNames The names of each column on the chart
	 */
	public PhonoSystem(String name, ArrayList<PhonoTable> tables) {
		this.name = name;
		this.tables = tables;
	}
	public PhonoSystem(String name, ArrayList<PhonoTable> tables, ArrayList<String> diacriticList) {
		this.name = name;
		this.tables = tables;
		this.diacriticList = diacriticList;
	}
	/**
	 * Creates a PhonoSystem object with a blank category list. This list will need something added to it to work!
	 * @param name The name of the phono system
	 */
	public PhonoSystem(String name) {
		this.name = name;
	}
	
	/**
	 * Copy constructor
	 * @param ps The PhonoSystem to be copied
	 */
	public PhonoSystem(PhonoSystem ps) {
		this.name = ps.name;
		this.tables = new ArrayList<PhonoTable>(ps.tables);
		this.diacriticList = new ArrayList<String>(ps.diacriticList);
	}
	
	/**
	 * Loads a PhonoSystem object from a file
	 * 
	 */
	
	public PhonoSystem(File file) {
		try {
			Parser parser = new Parser(file);
			// this is silly
			PhonoSystem parsedSys = parser.parsePhonoSys();
			this.diacriticList = parsedSys.getDiacritics();
			this.name = parsedSys.getName();
			this.tables = parsedSys.getTables();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.print("\n");
			for (int i = 0; i < 30; i++) {
				System.err.print("+=");
			}
			System.err.print("\n");
			System.err.println("Exception encountered! " + e.toString());
			System.err.println("Defaulting to IPA...");
			this.name = PhonoSystem.IPA.getName();
			this.tables = PhonoSystem.IPA.getTables();
		}
	}
	
	/**
	 * Gets the name of the phono system
	 * @return The name of the phono system
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets an ArrayList of all of the categories added
	 * @return ArrayList of several PhonoCategory instances
	 */
	public ArrayList<PhonoTable> getTables() {
		return tables;
	}
	
	/**
	 * Adds a table to a phono system
	 * @param table The table to be added
	 */
	public void addTable(PhonoTable table) {
		tables.add(table);
	}
	
	/**
	 * Removes table based off index.
	 * @param i index
	 */
	public void removeTable(int i) {
		tables.remove(i);
	}
	
	/**
	 * Removes table based off name. As this is slower than removing via index, removing via index is preferred.
	 * @param name Name of category to be removed
	 */
	public void removeTable(String name) {
		for (int i = 0; i < tables.size(); i++) {
			if (tables.get(i).getName().equals(name)) {
				tables.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Allows use of an XY coordinate system to get sounds
	 * @param i Index of table
	 * @param x Index of category
	 * @param y Index of sound
	 * @return The sound at both indexes
	 */
	public String getSound(int i, int x, int y) {
		return tables.get(i).getRow(x).getSound(y);
	}
	
	public void addDiacritic(String value) {
		diacriticList.add(value);
	}
	public void setDiacritics(ArrayList<String> newList) {
		diacriticList = newList;
	}
	public ArrayList<String> getDiacritics() {
		return diacriticList;
	}
	
	private static Tag parseDiacritics(Multitag tablelist) {
		Tag diacriticList;
		try {
			diacriticList = tablelist.getDirectChild("diacriticList");
		} catch (Exception e) {
			log.warn(e.toString());
			diacriticList = new Tag("diacriticList", "");
		}
		return diacriticList;
	}
	
	public static PhonoSystem parse(Multitag docTag) throws Exception {
		try {
			Multitag tablelist = docTag.getMultitag("Tablelist");
			Tag diacriticList = parseDiacritics(tablelist);
			PhonoSystem phonoSystem = new PhonoSystem(tablelist.getDirectChild("tablelistName").value());
			ArrayList<String> diacritics = new ArrayList<String>(Arrays.asList(diacriticList.value().split(",")));
			phonoSystem.setDiacritics(diacritics);
			for (int i = 0; i < tablelist.getSubMultitags().size(); i++) {
				if (tablelist.getSubMultitags().get(i).getName().equals("PhonoTable")) {
					Multitag phonoTableTag = tablelist.getSubMultitags().get(i);
					ArrayList<Tag> tableData = phonoTableTag.getUnattachedData();
					
					String name = phonoTableTag.getDirectChild("tableName").value();
					ArrayList<String> columns = new ArrayList<String>(Arrays.asList(phonoTableTag.getDirectChild("columnNames").value().split(",")));
					ArrayList<String> rowNamesList = new ArrayList<String>(Arrays.asList(phonoTableTag.getDirectChild("rowNames").value().split(",")));
					int perCell = 0;
					try {
						perCell = Integer.parseInt(phonoTableTag.getDirectChild("soundsPerCell").value());
					} catch (NumberFormatException nfe) {
						log.err("soundsPerCell must be integer in " + phonoTableTag.getDirectChild("tableName").value());
						log.err(nfe.toString());
						throw nfe;
					}
					
					ArrayList<PhonoCategory> cats = new ArrayList<PhonoCategory>();
					for (int j = 0; j < rowNamesList.size(); j++) {
						PhonoCategory cat = new PhonoCategory(rowNamesList.get(j));
						// TODO: allow multiple character sounds?
						try {
							String catData = tableData.get(j).value();
							for (int k = 0; k < catData.length(); k++) {
								cat.addSound(Character.toString(catData.charAt(k)));
							}
							cats.add(cat);
						} catch (IndexOutOfBoundsException e) {
							log.warn("No data found in table " + name);
						}
					}
					
					PhonoTable phonoTable = new PhonoTable(name, columns, cats, perCell);
					phonoSystem.addTable(phonoTable);
				}
			}
			return phonoSystem;
		} catch (Exception e) {
			log.err(e.toString());
			throw e;
		}
	}
	
	/**
	 * Converts a PhonoSystem object to a string
	 */
	public String toString() {
		String output = "===Tablelist Start===\n";
		output += "tablelistName:" + name + "\n";
		output += "diacriticList:";
		for (int i = 0; i < diacriticList.size(); i++) {
			output += diacriticList.get(i) + ",";
		}
		if (output.charAt(output.length() - 1) == ',') {
			output = output.substring(0, output.length() - 1);
		}
		output += "\n";
		for (int i = 0; i < tables.size(); i++) {
			output += tables.get(i).toString() + "\n";
		}
		output += "===Tablelist End===";
		return output;
	}
	/**
	 * Checks if a given value exists in a phono system.
	 * @param value The value to be checked
	 * @return Returns true if value is found in phono system, false if not
	 */
	public boolean isIn(String value) {
		for (int i = 0; i < diacriticList.size(); i++) {
			value = value.replace(Character.toString(diacriticList.get(i).charAt(0)), "");
		}
		if (value.length() > 1) {
			value = Character.toString(value.charAt(0));
		}
		for (int i = 0; i < tables.size(); i++) {
			for (int j = 0; j < tables.get(i).size(); j++) {
				for (int k = 0; k < tables.get(i).getRow(j).size(); k++) {
					if (tables.get(i).getRow(j).getSound(k).equals(value)) {
						return true;
					}
				}
			}
		}
		System.out.println(value + " not in sys");
		return false;
	}
	
	/**
	 * Creates a file of the PhonoSystem.
	 */
	public void toFile() {
		String output = "===PHOSYS Start===\n";
		output += toString();
		output += "\n===PHOSYS End===";
		
		File mainDir = new File(System.getProperty("user.home") + "/Susquehanna/phonoSystems");
		mainDir.mkdirs();
		File systemFile = new File(System.getProperty("user.home") + "/Susquehanna/phonoSystems/" + getName() + ".phosys");
		PrintWriter out;
		try {
			out = new PrintWriter(systemFile);
			out.println(output);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PhonoSystem) {
			PhonoSystem p = (PhonoSystem) obj;			
			if (p.name.equals(name) & p.tables.equals(tables) &
					p.diacriticList.equals(diacriticList)) {
				return true;
			}
			
		}
		return false;
	}
}
