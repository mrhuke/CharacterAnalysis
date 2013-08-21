package org.pipa;

import java.util.*;
import java.io.*;

public class Dictionary extends Text {
	Set<String> uniqueWords = new HashSet<String>();
	Set<Character> uniqueCharacters = new HashSet<Character>();
	
	Dictionary(String fileName, String format) throws IOException
	{
		super(fileName, format);
		uniqueWords = super.uniqueWords();
		uniqueCharacters = super.uniqueCharacters();
	}
	
	public Boolean hasWord(String word)
	{
		return uniqueWords.contains(word);
	}
	
	public Boolean hasCharacter(Character ch)
	{
		return uniqueCharacters.contains(ch);
	}
}
