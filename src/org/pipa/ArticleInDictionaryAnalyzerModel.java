package org.pipa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;
import java.io.*;
import java.awt.event.*;

import org.pipa.*;

public class ArticleInDictionaryAnalyzerModel {
	//... Constants
    private static final String INITIAL_VALUE = "0";
    
    //... Member variable defining state of calculator.
    private BigInteger m_total;  // The total current value state.
    Article article = null;
    ArrayList<Dictionary> dicts = new ArrayList<Dictionary>();
    ArrayList<Dictionary> selected_dicts = new ArrayList<Dictionary>();
    
    //============================================================== constructor
    /** Constructor */
    ArticleInDictionaryAnalyzerModel() {
        reset();
    }
    
    //==================================================================== reset
    /** Reset to initial value. */
    public void reset() {
        m_total = new BigInteger(INITIAL_VALUE);
    }
    
    //=============================================================== multiplyBy
    /** Multiply current total by a number.
    *@param operand Number (as string) to multiply total by.
    */
    public void multiplyBy(String operand) {
        m_total = m_total.multiply(new BigInteger(operand));
    }
    
    //================================================================= setValue
    /** Set the total value. 
    *@param value New value that should be used for the calculator total. 
    */
    public void setValue(String value) {
        m_total = new BigInteger(value);
    }
    
    //================================================================= getValue
    /** Return current calculator total. */
    public String getValue() {
        return m_total.toString();
    }
    
    public void setArticle(Article a)
	{
		article = a;
	}
	
	public void setDictionaries(ArrayList<Dictionary> ds)
	{
		dicts = ds;
	}
	
    // read articles
    public void readArticle(String[] articleContent) throws IOException
    {
    	article = new Article(articleContent, "UTF-8");
    }
    
    // read dictionaries
    public int readDictionaries(String dictionaryListName) throws IOException
    {
 		BufferedReader br  = new BufferedReader(new FileReader(dictionaryListName));
 		String line = br.readLine();
         while (line != null) {
         	 dicts.add(new Dictionary(line, "UTF-8"));
         	 //System.out.println(dicts.get(dicts.size()-1));
             line = br.readLine();
         }
         br.close();
         resetSelectedDictionaries(dicts.size() -1);
         return dicts.size();
    }
    
    // get dictionaries
    public ArrayList<Dictionary> getDictionaries()
    {
    	return dicts;
    }
    // reset dictionaries as desired , including dicts <= index
    public ArrayList<Dictionary> resetSelectedDictionaries(int selectedIndex)
    {
       selected_dicts.clear();
       for(int i = 0; i <= selectedIndex; i++) {
           selected_dicts.add(dicts.get(i));
       }
       return selected_dicts;
    }
    // get article
    public Article getArticle()
    {
    	return article;
    }
    
    /**
	 * Find unique words in the article which can be found in any dictionary. 
	 * @return unique words found
	 */
	public Set<Character> uniqueCharactersOfArticleInDictionaries()
	{
		Set<Character> uniqueCharacters = new HashSet<Character>();
 		for (Character ch : article.uniqueCharacters())
		{
			for ( Dictionary dict : selected_dicts ){
				if (dict.hasCharacter(ch)){
					uniqueCharacters.add(ch);
					continue;
				}
			}
		}
 		return uniqueCharacters;
	}
	
	/**
	 * Find out number of unique characters in the article.
	 * @return Number of unique characters
	 */
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
}
