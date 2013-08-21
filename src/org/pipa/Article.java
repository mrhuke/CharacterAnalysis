package org.pipa;

import java.io.*;

public class Article extends Text {

	public Article(String articleName, String format) throws IOException
	{
		super(articleName, format);
	}
	
	public Article(String[] article, String format) throws IOException
	{ 
		super(article, format);
	}
}
