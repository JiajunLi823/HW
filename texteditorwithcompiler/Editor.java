package ljj;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.text.*;
 
 
public class Editor extends JFrame {
	private String strPath;      
	public JTextPane textPane = new JTextPane();
	public JTextArea textArea;
	public JTextArea textAreaResult;
	public JFileChooser filechooser = new JFileChooser();
	private java.util.List<String> regexSplitLine;  
	private List<Integer> regexSplitLineInt;   
	private int cursorLineNum = 0;  
 
	public Editor()
	{
		super("TextEditor");
		regexSplitLine = new ArrayList<>();
		regexSplitLineInt = new ArrayList<>();
		Action[] actions=
			{
					new NewAction(),
					new OpenAction(),
					new SaveAction(),
					new CutAction(),
					new CopyAction(),
					new PasteAction(),
					new ExitAction()

			};
		setJMenuBar(createJMenuBar(actions));
		Container container=getContentPane();
		textArea = new JTextArea(); textAreaResult = new JTextArea();
		container.add(textArea, BorderLayout.CENTER);
		container.add(textAreaResult, BorderLayout.SOUTH);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		container.add(scrollPane);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		regexSplitLine = new ArrayList<>();
		regexSplitLineInt = new ArrayList<>();
		textArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_F4:{
						if(!regexSplitLineInt.isEmpty()){
							showNextErrorLine();
						}
						break;
					}
				}
			}
		});

		setSize(800,600);
		setVisible(true);
	}
 
	private JMenuBar createJMenuBar(Action[] actions)
	{
		JMenuBar menubar=new JMenuBar();
		JMenu menuFile=new JMenu("File");
		JMenu menuEdit=new JMenu("Edit");
		menuFile.add(new JMenuItem(actions[0]));
		menuFile.add(new JMenuItem(actions[1]));
		menuFile.add(new JMenuItem(actions[2]));
		menuFile.add(new JMenuItem(actions[6]));
		menuEdit.add(new JMenuItem(actions[3]));
		menuEdit.add(new JMenuItem(actions[4]));
		menuEdit.add(new JMenuItem(actions[5]));
		JMenuItem jmit = new JMenuItem("Compile");
		jmit.addActionListener(new ActionListenerCompile());
		menuEdit.add(jmit);
		menubar.add(menuFile);
		menubar.add(menuEdit);
		return menubar;
	}
 
	class NewAction extends AbstractAction
	{
		public NewAction()
		{
			super("New");
		}
		public void actionPerformed(ActionEvent e)
		{
			textArea.setDocument(new DefaultStyledDocument());
		}
	}
 
	class OpenAction extends AbstractAction
	{
		public OpenAction()
		{
			super("Open");
		}
		public void actionPerformed(ActionEvent e)
		{
			int i=filechooser.showOpenDialog(Editor.this);
			if(i==JFileChooser.APPROVE_OPTION)
			{
				File f=filechooser.getSelectedFile();
				strPath = f.getAbsolutePath();
				try
				{
					BufferedReader br = new BufferedReader(new FileReader(strPath));
					String line;
					StringBuilder sbContent = new StringBuilder();
					while ((line = br.readLine())!= null){
						sbContent.append(line+"\n");
					}
					textArea.setText(sbContent.toString());
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
 
	class SaveAction extends AbstractAction
	{
		public SaveAction()
		{
			super("Save");
		}
		public void actionPerformed(ActionEvent e)
		{
			int i=filechooser.showSaveDialog(Editor.this);
			if(i==JFileChooser.APPROVE_OPTION)
			{
				File f = filechooser.getSelectedFile();
				try
				{
					FileOutputStream out=new FileOutputStream(f);
					out.write(textArea.getText().getBytes());
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
 
	class ExitAction extends AbstractAction
	{
		public ExitAction()
		{
			super("Exit");
		}
		public void actionPerformed(ActionEvent e)
		{
			dispose();
		}
	}
 
	class CutAction extends AbstractAction
	{
		public CutAction()
		{
			super("Cut");
		}
		public void actionPerformed(ActionEvent e)
		{
			textPane.cut();
		}
	}
	
	class CopyAction extends AbstractAction
	{
		public CopyAction()
		{
			super("Copy");
		}
		public void actionPerformed(ActionEvent e)
		{
			textPane.copy();
		}
	}
 
	class PasteAction extends AbstractAction
	{
		public PasteAction()
		{
			super("Pasate");
		}
		public void actionPerformed(ActionEvent e)
		{
			textPane.paste();
		}
	}

	class ActionListenerCompile implements ActionListener {
		public void actionPerformed(ActionEvent e){
			try {
				if (strPath != null && strPath != "") {

					FileOutputStream out=new FileOutputStream(new File(strPath));
					out.write(textArea.getText().getBytes());
					Process proc1 = Runtime.getRuntime().exec("javac "+ strPath);
					BufferedReader br = new BufferedReader(new InputStreamReader(proc1.getErrorStream()));
					StringBuilder sb = new StringBuilder(); String line = "";
					while ((line = br.readLine()) != null){
						sb.append(line + "\n");
					}
					textAreaResult.setText(sb.toString());
					regexSplit(sb.toString());
					System.out.println(sb.toString());
				}
			}
			catch (Exception e4) {
				e4.printStackTrace();
			}
		}
	}

	public void showNextErrorLine(){
		try{
			int lineNum = regexSplitLineInt.get(cursorLineNum) - 1;
			int selectionStart = textArea.getLineStartOffset(lineNum);
			int selectionEnd = textArea.getLineEndOffset(lineNum);
			textArea.requestFocus();
			textArea.setSelectionStart(selectionStart);
			textArea.setSelectionEnd(selectionEnd);
			cursorLineNum++;
			if(cursorLineNum >= regexSplitLineInt.size())
				cursorLineNum = 0;
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public void regexSplit(String line){
		String regex = "(([a-zA-Z])\\w+.java:[0-9]+)";
		regexSplitLine.clear();
		Pattern ptn = Pattern.compile(regex);
		Matcher matcher = ptn.matcher(line);
		while(matcher.find()){
			regexSplitLine.add(matcher.group());
		}
		if(!regexSplitLine.isEmpty()){
			regexSplitLineInt.clear();
			for(String s : regexSplitLine){
				String sNum = s.substring(s.lastIndexOf(':') + 1, s.length());
				regexSplitLineInt.add(Integer.valueOf(sNum));
				System.out.println(sNum);
			}
		}
	}

	public static void main(String[] args)
	{
		new Editor();
	}
}
