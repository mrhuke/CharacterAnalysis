package org.pipa;

import org.pipa.ArticleInDictionaryAnalyzerModel;
import org.pipa.ArticleInDictionaryAnalyzerView;

import java.awt.event.*; 
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.*;

import java.io.*;
import java.util.*;

import org.pipa.ArticleInDictionaryAnalyzerController.ClearListener;
import org.pipa.ArticleInDictionaryAnalyzerController.MultiplyListener;

public class ArticleInDictionaryAnalyzerController{
	//... The Controller needs to interact with both the Model and View.
    private ArticleInDictionaryAnalyzerModel m_model;
    private ArticleInDictionaryAnalyzerView  m_view;
    
    //========================================================== constructor
    /** Constructor */
    ArticleInDictionaryAnalyzerController(ArticleInDictionaryAnalyzerModel model, ArticleInDictionaryAnalyzerView view) {
        m_model = model;
        m_view  = view;
        
        //... Add listeners to the view.
        //view.addRunListener(new MultiplyListener());
        //view.addSaveListener(new ClearListener());
        view.addInputListener(new InputDictionaryListListener());
        view.addSelectionListener(new SelectListener());
        view.addRunListener(new RunListener());
        view.addSaveListener(new SaveListener());
    }
    
    class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	JFileChooser fc = new JFileChooser();
        	try{
        		fc.setCurrentDirectory(new File(new File(".").getCanonicalPath()));
        	}catch (Exception ex){
        		System.out.println("Get current directory failed!");
        	}
        	fc.setSelectedFile(new File("result"));
        	int returnVal = fc.showSaveDialog(null);
        	if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
            	try{
            		HTMLDocument htmlDoc  = (HTMLDocument) m_view.getColoredArticle().getDocument();
	            	FileWriter fw = new FileWriter(file.toString()+".html");
	            	HTMLWriter htmlWriter = new HTMLWriter(fw, htmlDoc);
	            	htmlWriter.write();
	            	fw.flush();
	            	fw.close();
            	}catch (Exception ex)
            	{
            		System.out.println("Saving file error!");
            	}
            }
        }
    }
    
    class RunListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	m_view.getColoredArticle().setText("");
            try {
            	String[] articleContent = new String[1];
            	articleContent[0] = m_view.getArticle();
            	m_model.readArticle(articleContent);
            	//System.out.println(articleContent[0]);
            	//analyze m_model
        		Set<Character> uniqueCharsInArt = m_model.uniqueCharactersOfArticleInDictionaries();
        		//String output = ArticleInDictionaryAnalyzer.highlightCharactersNotInSet(m_model.getArticle().chars(), uniqueCharsInArt, "red");
        		//System.out.println(output);
            	// show result in artilceTF in viewm_view
        		double percent = (double)uniqueCharsInArt.size()/m_model.numberUniqueCharactersInArticle()*100;
        		String head = String.format("%.2f%% words are in dictionaries. Characters not in dictionaries are in red.\n", percent);
        		ArticleInDictionaryAnalyzerView.appendToPane(m_view.getColoredArticle(), head, Color.BLACK);
                m_view.highlightCharactersNotInSet(m_model.getArticle().chars(), uniqueCharsInArt);
                
            } catch (Exception ex) {
                System.out.println("Reading article failed!");
            }
        }
    }
    
    class InputDictionaryListListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	JFileChooser fc = new JFileChooser();
        	try{
        		fc.setCurrentDirectory(new File(new File(".").getCanonicalPath()));
        	}catch (Exception ex){
        		System.out.println("Get current directory failed!");
        	}
        	int returnVal = fc.showOpenDialog(null);
        	int nDict = 0;
        	
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	//File curPath = fc.getCurrentDirectory();
                File file = fc.getSelectedFile();
                System.out.println(file);
                try{
                	nDict = m_model.readDictionaries(file.toString());
                }catch(Exception ex)
                {
                	System.out.println("Reading dictionaries failed.\n");
                }
                m_view.clearDictList();
                m_view.setDictList(m_view.getDictNames(), nDict);
                //This is where a real application would open the file.
                //log.append("Opening: " + file.getName() + "." + newline);
            } else {
                //log.append("Open command cancelled by user." + newline);
            }
        }
    }
    
	//////////////////////////////////////////inner class ListSelectionListener
	/** When a dictionary in a list is selected.
	*  1. Get the dictionary index.
	*  2. Tell View to display this dictionary.
	*/
    class SelectListener implements ListSelectionListener{
    	public void valueChanged(ListSelectionEvent e) {
    		if (e.getValueIsAdjusting())
    			return;
    		m_view.showDictionary(m_view.getDictIndex());
    		// should change the dictlists for checking at the same time.
    		// e.g. if you select two-star, it needs include one and two star only (i.e. <= this index) 
    		m_model.resetSelectedDictionaries(m_view.getDictIndex());
  		  }
    }
    
    
    ////////////////////////////////////////// inner class MultiplyListener
    /** When a mulitplication is requested.
     *  1. Get the user input number from the View.
     *  2. Call the model to mulitply by this number.
     *  3. Get the result from the Model.
     *  4. Tell the View to display the result.
     * If there was an error, tell the View to display it.
     */
    class MultiplyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userInput = "";
            try {
                userInput = m_view.getUserInput();
                m_model.multiplyBy(userInput);
                m_view.setTotal(m_model.getValue());
                
            } catch (NumberFormatException nfex) {
                m_view.showError("Bad input: '" + userInput + "'");
            }
        }
    }//end inner class MultiplyListener
    
    
    //////////////////////////////////////////// inner class ClearListener
    /**  1. Reset model.
     *   2. Reset View.
     */    
    class ClearListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            m_model.reset();
            m_view.reset();
        }
    }// end inner class ClearListener
}
