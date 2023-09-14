package net.oijon.utils.info.ezfile;

import java.util.Scanner;

import net.oijon.utils.parser.data.Gloss;
import net.oijon.utils.parser.data.GlossList;

public class EZGloss {
	
	private static Scanner sc = new Scanner(System.in);
	private static GlossList glossList;
	
	private static String getName() {
		System.out.println("Welcome to EZGloss!");
		System.out.println("Enter name of gloss system: ");
		String name = "";
		if (sc.hasNextLine()) {
			name = sc.nextLine();
		} else {
			name = "N/A";
		}
		return name;
	}
	
	private static boolean getGloss() {
		boolean isRunning = true;
		
		System.out.print("\nAbbreviation: ");
		String abbr = sc.nextLine();
		if (abbr.equals("Done!")) {
			System.out.println("OK, " + glossList.size() + " glosses added.");
			isRunning = false;
			return isRunning;
		}
		System.out.print("\nMeaning: ");
		String m = sc.nextLine();
		if (m.equals("Done!")) {
			System.out.println("OK, " + glossList.size() + " glosses added.");
			isRunning = false;
			return isRunning;
		}
		
		Gloss g = new Gloss(abbr, m);
		glossList.add(g);
		
		System.out.println("Added gloss! Enter \"Done!\" (case sensitive) to exit");
		
		return isRunning;
	}
	
	public static void main(String[] args) {
		
		String name = getName();
		
		glossList = new GlossList(name);
		
		System.out.println("Name \"" + name + "\" selected. Start entering glosses now.");
		System.out.println("Enter \"Done!\" (case sensitive) to exit");
		
		boolean isRunning = true;
		
		while (isRunning) {
			isRunning = getGloss();
		}
		sc.close();
		
		System.out.println("Here is your gloss list!");
		System.out.println(glossList.toString());
		
	}
	
}
