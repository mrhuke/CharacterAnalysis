package org.pipa;

import java.io.*;
import java.util.*;

public abstract class Text implements Element {
	protected ArrayList<String> words = new ArrayList<String>();
	protected ArrayList<Character> chars = new ArrayList<Character>();
	
	public Text() {}
	
	public Text(String fileName, String format) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName), format));
		StringTokenizer st;
        String line = br.readLine();
        while (line != null) {
        	//st = new StringTokenizer(line,",.!?\"“”，。？",false);
        	st = new StringTokenizer(line);
            while(st.hasMoreTokens()){
            	String word = st.nextToken();
            	words.add(word);
            	for (char ch : word.toCharArray() ) // further break into characters
            		chars.add(ch);
            }
            line = br.readLine();
        }
        br.close();
	}
	
	public Text(String[] s, String format) throws IOException
	{
		StringTokenizer st;
		for (String line : s){
        	st = new StringTokenizer(line);
            while(st.hasMoreTokens()){
            	String word = st.nextToken();
            	words.add(word);
            	for (char ch : word.toCharArray() ) // further break into characters
            		chars.add(ch);
            }
        }
	}
	
	public List<Character> chars()
	{
		return chars;
	}
	
	public Set<Character> uniqueCharacters()
	{
		HashSet<Character> uniqueChars = new HashSet<Character>();
		for (char c : chars)
			uniqueChars.add(c);
		return uniqueChars;
	}

	public List<String> words()
	{
		return words;
	}
	
	public Set<String> uniqueWords()
	{
		List<String> allWords = words();
		Set<String> res = new HashSet<String>();
		for (String word : allWords){
			if (!res.contains(word)){
				res.add(word);
			}
		}
		return res;
	}
	
	public String toString()
	{
		String res = new String();
		for (Character word : chars)
			res += word + ",";
		return res;
	}
	
	public static <T> void save(ArrayList<T> chars, String fileName) throws FileNotFoundException
	{
		PrintWriter pw = new PrintWriter(new File(fileName));
		for (T word : chars ){
			pw.println(word);
		}
		pw.close();
	}
	
	public static void save(String str, String fileName) throws FileNotFoundException
	{
		
		PrintWriter pw = new PrintWriter(new File(fileName));
		pw.println(str);
		pw.close();
	
	}
}
