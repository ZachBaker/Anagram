import java.io.*;
import java.util.*;


public class Assig1 {
	
	DictInterface D;
	ArrayList<String> wordsFound;
	ArrayList<ArrayList<String>> masterList;
	static PrintWriter output;
	ArrayList<Character> charArray;
	
	public Assig1(String inputFile, String OutputFile, String dataType) throws FileNotFoundException{
		
		Scanner dictScan = new Scanner(new File("Dictionary.txt"));
		Scanner inScan = new Scanner(new File(inputFile));
		
		File outPutFile = new File(OutputFile);
		output = new PrintWriter(outPutFile);
		
		//Allows user to select the Dictionary object or the DLB
		if(dataType.equals("orig"))
			D = new MyDictionary();
		
		else if(dataType.equals("dlb"))
			D = new DLB();
				
		while(dictScan.hasNext())
			D.add(dictScan.nextLine());
				
		//Use the findAnagram method for each string in the file
		while(inScan.hasNext()){
						
			StringBuilder original = new StringBuilder(inScan.nextLine());
			
			charArray = new ArrayList<Character>();
			masterList = new ArrayList<ArrayList<String>>();
			
			findAnagram(original);
			
			System.out.println("Here are the results for " + original + ":");
			output.println("Here are the results for " + original + ":");
									
			for(int i = 0; i < masterList.size(); i++){
				wordsFound = masterList.get(i);
				
				if(wordsFound.isEmpty() == false){
					int words = i+1;
					System.out.println("Here are the " + words + " letter solutions:");
					output.println("Here are the " + words + " letter solutions:");
					
					java.util.Collections.sort(wordsFound);
					
					for(int j = 0; j<wordsFound.size();j++){
						System.out.println(wordsFound.get(j));
						output.println(wordsFound.get(j));
					}
				}
			}
			System.out.println();
			output.println();
		}
	}
	
	//Pass StringBuilder object of the original String into method. Adds each letter once to the beginning of a StringBuilder
	//and passes these with their respective suffixes into the overloaded method. 
	public void findAnagram(StringBuilder original){		
		
		StringBuilder b = new StringBuilder();

		//Deletes spaces original strings
		for(int i = 0;i<original.length(); i++){
			if(original.charAt(i) == ' ')
				original.deleteCharAt(i);
		}
				
		for(int i = 0; i<original.length(); i++){
			boolean charFinder = false;		
			char temper = original.charAt(i);

			for(int j = 0; j<charArray.size(); j++){
				char currChar = charArray.get(j);
				
				if(temper  == currChar)
					charFinder = true;
			}
			
			if(charFinder == false){
			b.append(original.charAt(i));
			int answer = D.searchPrefix(b);
			
				if(answer == 1 || answer == 3){
					StringBuilder suffix = new StringBuilder(original.deleteCharAt(i));
					findAnagram(b,suffix,0);
					b.deleteCharAt(b.length()-1);
					original.insert(i,temper);
					charArray.add(temper);
				}	
			}
				else{
				}
			}
		}
	
	//This is the main algorithm in the program. Its process is well documented throughout the method. 
	
	public void findAnagram(StringBuilder prefix, StringBuilder suffix, int pos){
		
		for(int i = 0; i <suffix.length();i++){
			//Modify prefix and suffix for this specific iteration, then use searchPrefix to determine the outcome.
			//Every possible outcome ends up restoring the prefix and suffix for the next iteration
			
			//System.out.println(prefix + " " + suffix);
			
			prefix.append(suffix.charAt(i));
			char currChar = suffix.charAt(i);
			suffix.deleteCharAt(i);
						
			int answer = D.searchPrefix(prefix,pos,prefix.length()-1);
						
			//If prefix is not a word or prefix, delete the character added in this iteration and restore suffix
			if(answer == 0){
				prefix.deleteCharAt(prefix.length()-1);
				suffix.insert(i, currChar);
			}
			
			//If prefix is a prefix, call method with new prefix and suffix, then restore prefix and suffix for next iteration
			else if(answer == 1 && suffix.length() != 0){
				findAnagram(prefix,suffix,pos);
				suffix.insert(i, currChar);
				prefix.deleteCharAt(prefix.length()-1);
			}
			
			//If prefix is a prefix but there are no more letters in suffix, there is no possible word, so restore prefix and suffix for 
			//next iteration
			else if(answer == 1 && suffix.length() == 0){
				prefix.deleteCharAt(prefix.length()-1);
				suffix.insert(i, currChar);
			}
			
			//If prefix is a word but not prefix and there is still a suffix to work with, there is no possible words so restore prefix and suffix.
			//However, if word is found there may be possible multiple word solutions left to find, so add a space to the prefix and pass in the suffix
			//to findAnagram, with a position of the prefix length so searchPrefix will only check the remaining suffix.
			else if(answer == 2 && suffix.length() != 0){	
				prefix.append(" ");
				findAnagram(prefix,suffix,prefix.length());
				prefix.deleteCharAt(prefix.length()-1);
				prefix.deleteCharAt(prefix.length()-1);
				suffix.insert(i, currChar);
			}
			
			//If prefix is a word and there is no suffix left, word was found so add it to the arraylist
			else if(answer == 2 && suffix.length() == 0){
				addWord(prefix);
				prefix.deleteCharAt(prefix.length()-1);
				suffix.insert(i, currChar);
			}
			
			//If prefix is a prefix and word and there is still a suffix, call the method with updated prefix and suffix
			//However, if word is found there may be possible multiple word solutions left to find, so add a space to the prefix and pass in the suffix
			//to findAnagram, with a position of the prefix length so searchPrefix will only check the remaining suffix.
			else if(answer == 3 && suffix.length() != 0){
				prefix.append(" ");
				findAnagram(prefix,suffix,prefix.length());
				prefix.deleteCharAt(prefix.length()-1);
				findAnagram(prefix,suffix,pos);
				suffix.insert(i, currChar);
				prefix.deleteCharAt(prefix.length()-1);
			}
			
			//If prefix is a prefix and word and there is no suffix left, then we found a word. 
			else if(answer == 3 && suffix.length() == 0){
				addWord(prefix);
				prefix.deleteCharAt(prefix.length()-1);
				suffix.insert(i, currChar);
			}
		}
	}
	
	//When word is found, calls addWord. addWord checks if word is already in its respective collection based on the number of words it contains,
	//and if it is not the word is added.
	
	public void addWord(StringBuilder newWord){
		int spaces = spaces(newWord);
		
		if(spaces>= masterList.size()){
			wordsFound = new ArrayList<String>();
			String tempString = new String(newWord);
			wordsFound.add(tempString);
			try{
			masterList.add(spaces, wordsFound);
			}
			catch(IndexOutOfBoundsException e){
				
				for(int i = 0; i<spaces; i++){
					if(masterList.size() <= spaces){
					ArrayList<String> tempList = new ArrayList<String>();
					masterList.add(i,tempList);
					}
				}
				masterList.add(spaces,wordsFound);
			}
		}
		
		else{
			wordsFound = masterList.get(spaces);
			boolean contains  = alreadyContains(newWord, wordsFound);
			
			if(contains == false){
			String tempString = new String(newWord);
			wordsFound.add(tempString);
		}
			else
				return;
		}
	}
	
	//Returns whether the word that was found is already in the list.
	
	public boolean alreadyContains(StringBuilder wordFound, ArrayList<String> list){
		
		String word = wordFound.toString();
		boolean found = false;
		
		for(int i = 0; i <list.size();i++){
			
			String temper = list.get(i);
			String tempString = temper.toString();
			
			if(tempString.equals(word))
				found = true;
		}
		return found;
	}
	
	//Simply searches through given String and returns the number of spaces in the String.
	public int spaces(StringBuilder b){
		int numberOfSpaces = 0;
		
		for(int i=0;i<b.length();i++){
			if(b.charAt(i) == ' ')
				numberOfSpaces++;
		}
		return numberOfSpaces;
	}
	
	public static void main (String [] args) throws FileNotFoundException{
		Assig1 runner = new Assig1(args[0], args[1], args[2]);
		output.close();
	}
}
