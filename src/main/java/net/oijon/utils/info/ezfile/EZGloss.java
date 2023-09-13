package net.oijon.utils.info.ezfile;

import java.util.Scanner;

import net.oijon.utils.parser.data.Gloss;
import net.oijon.utils.parser.data.GlossList;

public class EZGloss {
	
	private static Scanner sc = new Scanner(System.in);
	
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
	
	public static void main(String[] args) {
		
		String name = getName();
		
		GlossList glossList = new GlossList(name);
		
		System.out.println("Name \"" + name + "\" selected. Start entering glosses now.");
		System.out.println("Enter \"Done!\" (case sensitive) to exit");
		
		boolean isRunning = true;
		
		while (isRunning) {
			System.out.print("\nAbbreviation: ");
			String abbr = sc.nextLine();
			if (abbr.equals("Done!")) {
				System.out.println("OK, " + glossList.size() + " glosses added.");
				isRunning = false;
				break;
			}
			System.out.print("\nMeaning: ");
			String m = sc.nextLine();
			if (m.equals("Done!")) {
				System.out.println("OK, " + glossList.size() + " glosses added.");
				isRunning = false;
				break;
			}
			
			Gloss g = new Gloss(abbr, m);
			glossList.add(g);
			
			System.out.println("Added gloss! Enter \"Done!\" (case sensitive) to exit");
		}
		sc.close();
		
		System.out.println("Here is your gloss list!");
		System.out.println(glossList.toString());
		
	}
	
}
