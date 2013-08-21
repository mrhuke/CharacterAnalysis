package org.pipa;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.html.*;
import javax.swing.border.*;

import java.awt.event.*;
import java.util.List;
import java.util.Set;

class ArticleInDictionaryAnalyzerView extends JFrame {
	//... Constants
	private static final String INITIAL_VALUE = "1";
	private static final int TA_WIDTH = 30;
	private static String[] listNames = { "一星生字表", "二星生字表", "三星生字表", "四星生字表", "五星生字表", "六星生字表" };
	private static Color[] listColors = { Color.BLACK, Color.BLUE, Color.GREEN,Color.YELLOW, Color.WHITE };
	
	//... Components
	private JButton    m_inputBtn 		= new JButton("Input Dictionary List...");
	private JTextArea  m_dictTa 		= new JTextArea(15,TA_WIDTH);
	private JTextArea  m_articleTa      = new JTextArea(15,TA_WIDTH);
	private JTextPane  m_articleTp 		= new JTextPane();
	private JButton    m_runBtn 		= new JButton("Run");
	private JButton    m_saveBtn    	= new JButton("Save...");
	private DefaultListModel listModel  = new DefaultListModel();
	private JList list = new JList(listModel);
	
	private ArticleInDictionaryAnalyzerModel m_model;
	
	//======================================================= constructor
	/** Constructor */
	ArticleInDictionaryAnalyzerView(ArticleInDictionaryAnalyzerModel model) {
	  //... Set up the logic
	  m_model = model;
	  m_model.setValue(INITIAL_VALUE);
	  
	  //... Initialize components
	  //m_articleTf.setText(m_model.getValue());
	  m_dictTa.setCaretPosition(m_dictTa.getDocument().getLength());
	  m_dictTa.setEditable(false);
	  EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
	  m_articleTp.setBorder(eb);
      //m_articleTp.setMargin(new Insets(5, 5, 5, 5));
	  m_articleTp.setSize(20, (int)(TA_WIDTH/2));
	  m_articleTp.setContentType("text/html");
	  list.setSelectedIndex(0);
	  list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	  listModel.addElement("请输入生字表");
	  
	  
	  //... Layout the components.  
	  JPanel content1 = new JPanel();
	  content1.setLayout(new FlowLayout());
	  content1.add(new JScrollPane(list));
	  content1.add(new JScrollPane(m_dictTa));
	  
	  JPanel content2 = new JPanel();
	  content2.setLayout(new FlowLayout());
	  content2.add(new JScrollPane(m_articleTa));
	  
	  JPanel content3 = new JPanel();
	  content3.setLayout(new BorderLayout());
	  content3.add(new JScrollPane(m_articleTp),BorderLayout.CENTER);
	  
	  JPanel content4 = new JPanel();
	  content4.add(m_inputBtn);
	  content4.add(m_runBtn);
	  content4.add(m_saveBtn);
	  //content2.add(new JLabel("Total"));
	  
	  Container mainPane = getContentPane();
	  mainPane.setLayout(new GridLayout(2,2));
	  mainPane.add(content1);
	  mainPane.add(content2);
	  mainPane.add(content3);
	  mainPane.add(content4);
	  //mainPane.add(content1,BorderLayout.NORTH);
	  //mainPane.add(content2,BorderLayout.CENTER);
	  //mainPane.add(content3, BorderLayout.SOUTH);
	  
	  //... finalize layout
	  //this.setContentPane(content);
	  this.pack();
	  //this.setSize(800,700);
	  
	  this.setTitle("文章生字分析程序");
	  // The window closing event should probably be passed to the 
	  // Controller in a real program, but this is a short example.
	  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	void reset() {
	  m_articleTa.setText(INITIAL_VALUE);
	}
	
	String getUserInput() {
	  return m_dictTa.getText();
	}
	
	void setTotal(String newTotal) {
	  m_articleTa.setText(newTotal);
	}
	
	void showError(String errMessage) {
	  JOptionPane.showMessageDialog(this, errMessage);
	}
	
	String getArticle()
	{
		return m_articleTa.getText();
	}
	
	JTextPane getColoredArticle()
	{
		return m_articleTp;
	}
	
	int getDictIndex()
	{
		return list.getSelectedIndex();
	}
	
	void clearDictList()
	{
		listModel.clear();
	}
	
	void setDictList(String[] listNames, int n)
	{
		for (int i = 0; i < n; ++i)
			listModel.addElement(listNames[i]);
	}
	
	void showDictionary(int index)
	{
		String content = m_model.getDictionaries().get(index).toString();
		this.m_dictTa.setText(formatString(content,TA_WIDTH));
	}
	
	String formatString(String s, int maxCharPerLine)
	{
		String res = new String();
		int be = 0;
		while (be < s.length()){
			res += s.substring(be, Math.min(be+maxCharPerLine,s.length())) + "\n";
			be += maxCharPerLine;
		}
		return res;
	}
	
	String[] getDictNames()
	{
		return listNames;
	}
	
	static void appendToPane(JTextPane pane, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = pane.getDocument().getLength();
        pane.setCaretPosition(len);
        pane.setCharacterAttributes(aset, false);
        pane.replaceSelection(msg);
    }
	
	public void highlightCharactersNotInSet(List<Character> chars, Set<Character> uniqueChars)
	{
		int count = 0;
		for (Character ch : chars){
			// contained in the set or punctuation
			if (uniqueChars.contains(ch) || (!Character.isDigit(ch) && !Character.isLetter(ch))){
				appendToPane(m_articleTp, ch.toString(), Color.BLACK);
			}
			else{
				appendToPane(m_articleTp, ch.toString(), Color.RED);
			}
			if (++count % 20 == 0) appendToPane(m_articleTp, "\n", Color.BLACK);
		}
	}
		
	// register listeners
	void addSelectionListener(ListSelectionListener sel)
	{
		list.addListSelectionListener(sel);
	}
	
	void addSaveListener(ActionListener sav)
	{
		m_saveBtn.addActionListener(sav);
	}
	
	void addInputListener(ActionListener inp) {
		m_inputBtn.addActionListener(inp);
	}
	
	void addRunListener(ActionListener mal) {
		m_runBtn.addActionListener(mal);
	}
}
