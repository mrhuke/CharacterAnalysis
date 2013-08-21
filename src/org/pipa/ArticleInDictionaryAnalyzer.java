package org.pipa;

import java.io.*;
import java.util.*;

public class ArticleInDictionaryAnalyzer{
	
	Article article = null;
	ArrayList<Dictionary> dicts = new ArrayList<Dictionary>();
	
	public ArticleInDictionaryAnalyzer(String articleName, String dictionaryListName) throws IOException
	{
		// read article
		article = new Article(articleName, "UTF-8");
		//System.out.println(article);
		
		// read dictionaries
		BufferedReader br  = new BufferedReader(new FileReader(dictionaryListName));
		String line = br.readLine();
        while (line != null) {
        	dicts.add(new Dictionary(line, "UTF-8"));
        	//System.out.println(dicts.get(dicts.size()-1));
            line = br.readLine();
        }
        br.close();
	}
	
	public void setArticle(Article a)
	{
		article = a;
	}
	
	public void setDictionaries(ArrayList<Dictionary> ds)
	{
		dicts = ds;
	}
	
    public int numberWordsInArticle()
    {
    	return article.words().size();
    }
    
    public int numberUniqueCharactersInArticle()
    {
    	Set<Character> chars = article.uniqueCharacters();
    	int count = 0;
    	for (char ch : chars){
    		if (Character.isLetter(ch) || Character.isDigit(ch)){
    			count ++;
    		}
    	}
    	return count;
    }
    
	/**
	 * Calculate unique words in the article can be found in any dictionary. 
	 * @return unique words found
	 */
	public Set<Character> uniqueCharactersOfArticleInDictionaries()
	{
		Set<Character> uniqueCharacters = new HashSet<Character>();
 		for (Character ch : article.uniqueCharacters())
		{
			for ( Dictionary dict : dicts ){
				if (dict.hasCharacter(ch)){
					uniqueCharacters.add(ch);
					continue;
				}
			}
		}
 		return uniqueCharacters;
	}

	/**
	 * Highlight characters in a list that are not in a set. The missing characters are in parenthesis.
	 * @param chars A list of characters
	 * @param uniqueChars A set of unique characters
	 * @return A String consisting of the list of characters and missing characters are parenthesized.
	 */
	public static String highlightCharactersNotInSet(List<Character> chars, Set<Character> uniqueChars)
	{
		String output = new String();
		
		int state = 0;
		for (char ch : chars){
			if (uniqueChars.contains(ch)){
				if (state == 0){
					output += ch;
					state = 1;		
				}else if (state == 1){
					output += ch;
				}
				else{
					output += "）" + ch;
					state = 1;
				}
			}
			else{
				if (state == 0){
					output += "（" + ch;
					state = -1;		
				}else if (state == 1){
					output += "（" + ch;
					state = -1;
				}
				else{
					output += ch;
				}
			}
		}
		if (state == -1) output += ")";
		return output;
	}
	
	/**
	 * Highlight characters in a list that are not in a set. The missing characters are in color and the output is in HTML format.
	 * @param chars A list of characters
	 * @param uniqueChars A set of unique characters
	 * @return A String consisting of the list of characters and missing characters are parenthesized.
	 */
	public static String highlightCharactersNotInSet(List<Character> chars, Set<Character> uniqueChars, String color)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>");
		sb.append("<html lang=\"en\">");
		sb.append("<head><title>Title</title></head>");
		sb.append("<body><p>");
		
		int count = 0;
		for (char ch : chars){
			// contained in the set or punctuation
			if (uniqueChars.contains(ch) || (!Character.isDigit(ch) && !Character.isLetter(ch))){
				sb.append(ch);
			}
			else{
				sb.append("<font color=\""+color+"\">");
				sb.append(ch);
				sb.append("</font>");
			}
			if (++count % 20 == 0) sb.append("\n");
		}
		
		sb.append("</p></body>");
		sb.append("</html>");
		
		return sb.toString();
	}
	
	public Article article()
	{
		return article;
	}
	
	public ArrayList<Dictionary> dictionaries()
	{
		return dicts;
	}
	/*
	public static void main(String[] args) throws IOException
	{
		String articleName = "data/article.txt";
		String dictionaryListName = "data/dictlist.txt";
		
		// construct an analyzer using articles and dictionaries
		ArticleInDictionaryAnalyzer artInDictAnalyzer = new ArticleInDictionaryAnalyzer("./data/article.txt", "./data/dictlist.txt");
		
		// unique character set
		Set<Character> uniqueCharsInArt = artInDictAnalyzer.uniqueCharactersOfArticleInDictionaries();
		//for (String uniqueWord : uniqueWords)
		//	System.out.println(uniqueWord);
		
		String output = ArticleInDictionaryAnalyzer.highlightCharactersNotInSet(artInDictAnalyzer.article().chars(), uniqueCharsInArt, "red");
		
		System.out.println(uniqueCharsInArt);
		System.out.println((double)artInDictAnalyzer.numberUniqueCharactersInArticle());
		
		double percent = (double)uniqueCharsInArt.size()/artInDictAnalyzer.numberUniqueCharactersInArticle()*100;
		String head = String.format("%.2f%% words in %s are in dictionaries listed in %s. Characters not in dictionaries are in red.\n", percent, articleName, dictionaryListName);
		System.out.println(head + output);
		
		// output
		Text.save(head + output, "data/output.html");
	}*/
}
