package org.pipa;

import org.pipa.ArticleInDictionaryAnalyzerController;
import org.pipa.ArticleInDictionaryAnalyzerModel;
import org.pipa.ArticleInDictionaryAnalyzerView;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ArticleInDictionaryAnalyzerDemo{

	public static void main(String[] args) {
        
		ArticleInDictionaryAnalyzerModel      model      = new ArticleInDictionaryAnalyzerModel();
		ArticleInDictionaryAnalyzerView       view       = new ArticleInDictionaryAnalyzerView(model);
		ArticleInDictionaryAnalyzerController controller = new ArticleInDictionaryAnalyzerController(model, view);
        
        view.setVisible(true);
    }
}