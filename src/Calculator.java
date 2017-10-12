//Author: 	Francisco Lynn
//Date: 	7/16/17
//Description: 	Calculator Class

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.event.ActionEvent.*;
import java.awt.event.ActionListener.*;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Calculator extends JFrame implements ActionListener{

	private JPanel mainPanel, panel_Binary, panel_NumSystem, panel_Buttons, panel_ByteSize;
	private JMenuBar menuBar;
	private JMenu menuView, menuEdit, menuHelp;
	private JCheckBoxMenuItem menuItem_hideCalc;
	private JMenuItem menuItem_Copy, menuItem_Help;
	private JTextArea display;
	
	private ButtonGroup btnGroup_NumSystem, btnGroup_byteSize;
	
	private JRadioButton rdbtnQword, rdbtnDword, rdbtnWord, rdbtnByte,
						 rdbtnDec, rdbtnHex, rdbtnOct, rdbtnBin;
	
	private JButton btnA, btnB, btnC, btnD, btnE, btnF,
					btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
					btnQuot, btnMod, btnLeftArrow, btnCE, btnClear, btnNegative,
					btnDecimal, btnSqrt, btnPercent, btnRational,
					btnDivide, btnMult, btnSubtract, btnAdd, btnEquals;
	
	private JButton[] alphaNumerals = new JButton[15];
	
	private String prevNumSystem = "Dec",	//holds previous numSystem radio button selection for proper conversion
				   operator = "none",		//current math operator being used (eg: add, subtract...)
				   binFieldText;
	
	//placeholder for operands for math operations
	private int a = 0, b = 0, result = 0, displayValue = 0, binFieldVal = 0;
	
	private JTextArea binaryField;


	//constructor
	public Calculator() {
		
		//disable resizing
		setResizable(false);
		
		//Title the frame and set window to exit program upon clicking 'x'
		setTitle("Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 514, 377);
		//add calculator icon
		ImageIcon img = new ImageIcon("icon.JPG");
		this.setIconImage(img.getImage());
		
		//create a menu bar and attach to frame
		menuBar = new JMenuBar();
		menuBar.setBorder(new MatteBorder(1, 1, 0, 1, (Color) Color.DARK_GRAY));
		menuBar.setForeground(Color.DARK_GRAY);
		setJMenuBar(menuBar);
		
		//Create menu that contains button to view/hide calculator and add to frame
		menuView = new JMenu("View");
		menuBar.add(menuView);
		//Create menu item that can hide the calculator
		menuItem_hideCalc = new JCheckBoxMenuItem("Hide/Show Calculator");
		menuItem_hideCalc.addActionListener(this);
		menuView.add(menuItem_hideCalc);
		
		//create menu item that copies the value in display screen
		menuEdit = new JMenu("Edit");
		menuBar.add(menuEdit);
		menuItem_Copy = new JMenuItem(new DefaultEditorKit.CopyAction());
		menuItem_Copy.setText("Copy");
		menuItem_Copy.addActionListener(this);
		menuEdit.add(menuItem_Copy);
		
		//create menu that has item which will display calculator user manual
		menuHelp = new JMenu("Help");
		menuHelp.addActionListener(this);
		menuBar.add(menuHelp);
		menuItem_Help = new JMenuItem("Help");
		menuItem_Help.addActionListener(this);
		menuHelp.add(menuItem_Help);
		mainPanel = new JPanel();
		mainPanel.setForeground(Color.DARK_GRAY);
		mainPanel.setBorder(new LineBorder(Color.BLACK));
		setContentPane(mainPanel);
		
		//Create display screen and set attributes
		display = new JTextArea();
		display.setLineWrap(true);
		display.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		display.setFont(new Font("Consolas", Font.PLAIN, 24));
		display.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		display.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				//System.out.println("change triggered");
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				//updateBinaryField();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
		
			}

	    });
		
		//Create panel for binary string field
		panel_Binary = new JPanel();
		panel_Binary.setBorder(new LineBorder(Color.GRAY));
		
		//Create Panel for Number System radio buttons (hex, dec...)
		panel_NumSystem = new JPanel();
		panel_NumSystem.setBorder(new LineBorder(Color.GRAY));
		
		//Create Panel for buttons
		panel_Buttons = new JPanel();
		panel_Buttons.setBorder(null);
		
		//Create Button groups to house related buttons
		btnGroup_NumSystem = new ButtonGroup();
		btnGroup_byteSize = new ButtonGroup();
		
		//Create panel for byte size radio buttons (eg: qword, dword...)
		panel_ByteSize = new JPanel();
		panel_ByteSize.setBorder(new LineBorder(Color.GRAY));
		
		//Create radio buttons
		rdbtnQword = new JRadioButton("Qword");
		rdbtnQword.addActionListener(this);
		rdbtnQword.setSelected(true);
		btnGroup_byteSize.add(rdbtnQword);
		
		rdbtnDword = new JRadioButton("Dword");
		btnGroup_byteSize.add(rdbtnDword);
		rdbtnDword.setEnabled(false);
		
		rdbtnWord = new JRadioButton("Word");
		btnGroup_byteSize.add(rdbtnWord);
		rdbtnWord.setEnabled(false);
		
		rdbtnByte = new JRadioButton("Byte");
		btnGroup_byteSize.add(rdbtnByte);
		rdbtnByte.setEnabled(false);
		
		//Format button groups and add to frame
		GroupLayout gl_panel_ByteSize = new GroupLayout(panel_ByteSize);
		gl_panel_ByteSize.setHorizontalGroup(
			gl_panel_ByteSize.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_ByteSize.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_ByteSize.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnByte)
						.addComponent(rdbtnWord)
						.addComponent(rdbtnDword)
						.addComponent(rdbtnQword))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		gl_panel_ByteSize.setVerticalGroup(
			gl_panel_ByteSize.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_ByteSize.createSequentialGroup()
					.addContainerGap()
					.addComponent(rdbtnQword, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnDword, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnWord, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnByte)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_ByteSize.setLayout(gl_panel_ByteSize);
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_Binary, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
						.addComponent(display, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(panel_ByteSize, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panel_NumSystem, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(panel_Buttons, GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addGap(13)
					.addComponent(display, GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_Binary, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(panel_NumSystem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_ByteSize, 0, 88, Short.MAX_VALUE))
						.addComponent(panel_Buttons, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_Binary.setLayout(new BorderLayout(0, 0));
		
		//Create field containing binary "tickers" that update based on display value
		binaryField = new JTextArea();
		binaryField.setFont(new Font("Monospaced", Font.PLAIN, 18));
		binaryField.setLineWrap(true);
		panel_Binary.add(binaryField);
		GridBagLayout gbl_panel_Buttons = new GridBagLayout();
		gbl_panel_Buttons.columnWidths = new int[]{57, 53, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_Buttons.rowHeights = new int[]{23, 0, 0, 0, 0, 0, 0};
		gbl_panel_Buttons.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_Buttons.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_Buttons.setLayout(gbl_panel_Buttons);
		
		//Create Buttons and set attributes
		btnQuot = new JButton("Quot");
		btnQuot.setEnabled(false);
		btnQuot.setMargin(new Insets(2, 3, 2, 3));
		GridBagConstraints gbc_btnQuot = new GridBagConstraints();
		gbc_btnQuot.fill = GridBagConstraints.BOTH;
		gbc_btnQuot.insets = new Insets(0, 0, 5, 5);
		gbc_btnQuot.gridx = 0;
		gbc_btnQuot.gridy = 0;
		panel_Buttons.add(btnQuot, gbc_btnQuot);
		
		btnMod = new JButton("Mod");
		btnMod.addActionListener(this);
		btnMod.setMargin(new Insets(2, 5, 2, 5));
		GridBagConstraints gbc_btnMod = new GridBagConstraints();
		gbc_btnMod.fill = GridBagConstraints.BOTH;
		gbc_btnMod.insets = new Insets(0, 0, 5, 5);
		gbc_btnMod.gridx = 1;
		gbc_btnMod.gridy = 0;
		panel_Buttons.add(btnMod, gbc_btnMod);
		
		btnA = new JButton("A");
		btnA.addActionListener(this);
		btnA.setEnabled(false);
		GridBagConstraints gbc_btnA = new GridBagConstraints();
		gbc_btnA.fill = GridBagConstraints.BOTH;
		gbc_btnA.insets = new Insets(0, 0, 5, 5);
		gbc_btnA.gridx = 2;
		gbc_btnA.gridy = 0;
		panel_Buttons.add(btnA, gbc_btnA);
		
		//Create empty buttons
		JButton btnNull11 = new JButton(" ");
		btnNull11.setEnabled(false);
		GridBagConstraints gbc_btnNull11 = new GridBagConstraints();
		gbc_btnNull11.fill = GridBagConstraints.BOTH;
		gbc_btnNull11.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull11.gridx = 3;
		gbc_btnNull11.gridy = 0;
		panel_Buttons.add(btnNull11, gbc_btnNull11);
		
		JButton btnNull12 = new JButton(" ");
		btnNull12.setEnabled(false);
		GridBagConstraints gbc_btnNull12 = new GridBagConstraints();
		gbc_btnNull12.fill = GridBagConstraints.BOTH;
		gbc_btnNull12.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull12.gridx = 4;
		gbc_btnNull12.gridy = 0;
		panel_Buttons.add(btnNull12, gbc_btnNull12);
		
		JButton btnNull13 = new JButton(" ");
		btnNull13.setEnabled(false);
		GridBagConstraints gbc_btnNull13 = new GridBagConstraints();
		gbc_btnNull13.fill = GridBagConstraints.BOTH;
		gbc_btnNull13.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull13.gridx = 5;
		gbc_btnNull13.gridy = 0;
		panel_Buttons.add(btnNull13, gbc_btnNull13);
		
		JButton btnNull14 = new JButton(" ");
		btnNull14.setEnabled(false);
		GridBagConstraints gbc_btnNull14 = new GridBagConstraints();
		gbc_btnNull14.fill = GridBagConstraints.BOTH;
		gbc_btnNull14.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull14.gridx = 6;
		gbc_btnNull14.gridy = 0;
		panel_Buttons.add(btnNull14, gbc_btnNull14);
		
		JButton btnNull15 = new JButton(" ");
		btnNull15.setEnabled(false);
		GridBagConstraints gbc_btnNull15 = new GridBagConstraints();
		gbc_btnNull15.fill = GridBagConstraints.BOTH;
		gbc_btnNull15.insets = new Insets(0, 0, 5, 0);
		gbc_btnNull15.gridx = 7;
		gbc_btnNull15.gridy = 0;
		panel_Buttons.add(btnNull15, gbc_btnNull15);
		
		JButton btnNull1 = new JButton(" ");
		btnNull1.setEnabled(false);
		GridBagConstraints gbc_btnNull1 = new GridBagConstraints();
		gbc_btnNull1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNull1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull1.gridx = 0;
		gbc_btnNull1.gridy = 1;
		panel_Buttons.add(btnNull1, gbc_btnNull1);
		
		JButton btnNull2 = new JButton(" ");
		btnNull2.setEnabled(false);
		GridBagConstraints gbc_btnNull2 = new GridBagConstraints();
		gbc_btnNull2.fill = GridBagConstraints.BOTH;
		gbc_btnNull2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull2.gridx = 1;
		gbc_btnNull2.gridy = 1;
		panel_Buttons.add(btnNull2, gbc_btnNull2);
		
		btnB = new JButton("B");
		btnB.addActionListener(this);
		btnB.setEnabled(false);
		GridBagConstraints gbc_btnB = new GridBagConstraints();
		gbc_btnB.fill = GridBagConstraints.BOTH;
		gbc_btnB.insets = new Insets(0, 0, 5, 5);
		gbc_btnB.gridx = 2;
		gbc_btnB.gridy = 1;
		panel_Buttons.add(btnB, gbc_btnB);
		
		btnLeftArrow = new JButton("←");
		btnLeftArrow.addActionListener(this);
		GridBagConstraints gbc_btnLeftArrow = new GridBagConstraints();
		gbc_btnLeftArrow.fill = GridBagConstraints.BOTH;
		gbc_btnLeftArrow.insets = new Insets(0, 0, 5, 5);
		gbc_btnLeftArrow.gridx = 3;
		gbc_btnLeftArrow.gridy = 1;
		panel_Buttons.add(btnLeftArrow, gbc_btnLeftArrow);
		
		btnCE = new JButton("CE");
		btnCE.addActionListener(this);
		GridBagConstraints gbc_btnCE = new GridBagConstraints();
		gbc_btnCE.fill = GridBagConstraints.BOTH;
		gbc_btnCE.insets = new Insets(0, 0, 5, 5);
		gbc_btnCE.gridx = 4;
		gbc_btnCE.gridy = 1;
		panel_Buttons.add(btnCE, gbc_btnCE);
		
		btnClear = new JButton("C");
		btnClear.addActionListener(this);
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.fill = GridBagConstraints.BOTH;
		gbc_btnClear.insets = new Insets(0, 0, 5, 5);
		gbc_btnClear.gridx = 5;
		gbc_btnClear.gridy = 1;
		panel_Buttons.add(btnClear, gbc_btnClear);
		
		btnNegative = new JButton("±");
		btnNegative.addActionListener(this);
		GridBagConstraints gbc_btnNegative = new GridBagConstraints();
		gbc_btnNegative.fill = GridBagConstraints.BOTH;
		gbc_btnNegative.insets = new Insets(0, 0, 5, 5);
		gbc_btnNegative.gridx = 6;
		gbc_btnNegative.gridy = 1;
		panel_Buttons.add(btnNegative, gbc_btnNegative);
		
		btnSqrt = new JButton("√");
		btnSqrt.setEnabled(false);
		GridBagConstraints gbc_btnSqrt = new GridBagConstraints();
		gbc_btnSqrt.fill = GridBagConstraints.BOTH;
		gbc_btnSqrt.insets = new Insets(0, 0, 5, 0);
		gbc_btnSqrt.gridx = 7;
		gbc_btnSqrt.gridy = 1;
		panel_Buttons.add(btnSqrt, gbc_btnSqrt);
		
		JButton btnNull3 = new JButton(" ");
		btnNull3.setEnabled(false);
		GridBagConstraints gbc_btnNull3 = new GridBagConstraints();
		gbc_btnNull3.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNull3.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull3.gridx = 0;
		gbc_btnNull3.gridy = 2;
		panel_Buttons.add(btnNull3, gbc_btnNull3);
		
		JButton btnNull4 = new JButton(" ");
		btnNull4.setEnabled(false);
		GridBagConstraints gbc_btnNull4 = new GridBagConstraints();
		gbc_btnNull4.fill = GridBagConstraints.BOTH;
		gbc_btnNull4.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull4.gridx = 1;
		gbc_btnNull4.gridy = 2;
		panel_Buttons.add(btnNull4, gbc_btnNull4);
		
		btnC = new JButton("C");
		btnC.addActionListener(this);
		btnC.setEnabled(false);
		GridBagConstraints gbc_btnC = new GridBagConstraints();
		gbc_btnC.fill = GridBagConstraints.BOTH;
		gbc_btnC.insets = new Insets(0, 0, 5, 5);
		gbc_btnC.gridx = 2;
		gbc_btnC.gridy = 2;
		panel_Buttons.add(btnC, gbc_btnC);
		
		btn7 = new JButton("7");
		btn7.addActionListener(this);
		GridBagConstraints gbc_btn7 = new GridBagConstraints();
		gbc_btn7.fill = GridBagConstraints.BOTH;
		gbc_btn7.insets = new Insets(0, 0, 5, 5);
		gbc_btn7.gridx = 3;
		gbc_btn7.gridy = 2;
		panel_Buttons.add(btn7, gbc_btn7);
		
		btn8 = new JButton("8");
		btn8.addActionListener(this);
		GridBagConstraints gbc_btn8 = new GridBagConstraints();
		gbc_btn8.fill = GridBagConstraints.BOTH;
		gbc_btn8.insets = new Insets(0, 0, 5, 5);
		gbc_btn8.gridx = 4;
		gbc_btn8.gridy = 2;
		panel_Buttons.add(btn8, gbc_btn8);
		
		btn9 = new JButton("9");
		btn9.addActionListener(this);
		GridBagConstraints gbc_btn9 = new GridBagConstraints();
		gbc_btn9.fill = GridBagConstraints.BOTH;
		gbc_btn9.insets = new Insets(0, 0, 5, 5);
		gbc_btn9.gridx = 5;
		gbc_btn9.gridy = 2;
		panel_Buttons.add(btn9, gbc_btn9);
		
		btnDivide = new JButton("/");
		btnDivide.addActionListener(this);
		GridBagConstraints gbc_btnDivide = new GridBagConstraints();
		gbc_btnDivide.fill = GridBagConstraints.BOTH;
		gbc_btnDivide.insets = new Insets(0, 0, 5, 5);
		gbc_btnDivide.gridx = 6;
		gbc_btnDivide.gridy = 2;
		panel_Buttons.add(btnDivide, gbc_btnDivide);
		
		btnPercent = new JButton("%");
		btnPercent.setEnabled(false);
		GridBagConstraints gbc_btnPercent = new GridBagConstraints();
		gbc_btnPercent.fill = GridBagConstraints.BOTH;
		gbc_btnPercent.insets = new Insets(0, 0, 5, 0);
		gbc_btnPercent.gridx = 7;
		gbc_btnPercent.gridy = 2;
		panel_Buttons.add(btnPercent, gbc_btnPercent);
		
		JButton btnNull5 = new JButton(" ");
		btnNull5.setEnabled(false);
		GridBagConstraints gbc_btnNull5 = new GridBagConstraints();
		gbc_btnNull5.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNull5.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull5.gridx = 0;
		gbc_btnNull5.gridy = 3;
		panel_Buttons.add(btnNull5, gbc_btnNull5);
		
		JButton btnNull6 = new JButton(" ");
		btnNull6.setEnabled(false);
		GridBagConstraints gbc_btnNull6 = new GridBagConstraints();
		gbc_btnNull6.fill = GridBagConstraints.BOTH;
		gbc_btnNull6.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull6.gridx = 1;
		gbc_btnNull6.gridy = 3;
		panel_Buttons.add(btnNull6, gbc_btnNull6);
		
		btnD = new JButton("D");
		btnD.addActionListener(this);
		btnD.setEnabled(false);
		GridBagConstraints gbc_btnD = new GridBagConstraints();
		gbc_btnD.fill = GridBagConstraints.BOTH;
		gbc_btnD.insets = new Insets(0, 0, 5, 5);
		gbc_btnD.gridx = 2;
		gbc_btnD.gridy = 3;
		panel_Buttons.add(btnD, gbc_btnD);
		
		btn4 = new JButton("4");
		btn4.addActionListener(this);
		GridBagConstraints gbc_btn4 = new GridBagConstraints();
		gbc_btn4.fill = GridBagConstraints.BOTH;
		gbc_btn4.insets = new Insets(0, 0, 5, 5);
		gbc_btn4.gridx = 3;
		gbc_btn4.gridy = 3;
		panel_Buttons.add(btn4, gbc_btn4);
		
		btn5 = new JButton("5");
		btn5.addActionListener(this);
		GridBagConstraints gbc_btn5 = new GridBagConstraints();
		gbc_btn5.fill = GridBagConstraints.BOTH;
		gbc_btn5.insets = new Insets(0, 0, 5, 5);
		gbc_btn5.gridx = 4;
		gbc_btn5.gridy = 3;
		panel_Buttons.add(btn5, gbc_btn5);
		
		btn6 = new JButton("6");
		btn6.addActionListener(this);
		GridBagConstraints gbc_btn6 = new GridBagConstraints();
		gbc_btn6.fill = GridBagConstraints.BOTH;
		gbc_btn6.insets = new Insets(0, 0, 5, 5);
		gbc_btn6.gridx = 5;
		gbc_btn6.gridy = 3;
		panel_Buttons.add(btn6, gbc_btn6);
		
		btnMult = new JButton("*");
		btnMult.addActionListener(this);
		GridBagConstraints gbc_btnMult = new GridBagConstraints();
		gbc_btnMult.fill = GridBagConstraints.BOTH;
		gbc_btnMult.insets = new Insets(0, 0, 5, 5);
		gbc_btnMult.gridx = 6;
		gbc_btnMult.gridy = 3;
		panel_Buttons.add(btnMult, gbc_btnMult);
		
		btnRational = new JButton("1/x");
		btnRational.setEnabled(false);
		GridBagConstraints gbc_btnRational = new GridBagConstraints();
		gbc_btnRational.fill = GridBagConstraints.BOTH;
		gbc_btnRational.insets = new Insets(0, 0, 5, 0);
		gbc_btnRational.gridx = 7;
		gbc_btnRational.gridy = 3;
		panel_Buttons.add(btnRational, gbc_btnRational);
		
		JButton btnNull7 = new JButton(" ");
		btnNull7.setEnabled(false);
		GridBagConstraints gbc_btnNull7 = new GridBagConstraints();
		gbc_btnNull7.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNull7.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull7.gridx = 0;
		gbc_btnNull7.gridy = 4;
		panel_Buttons.add(btnNull7, gbc_btnNull7);
		
		JButton btnNull8 = new JButton(" ");
		btnNull8.setEnabled(false);
		GridBagConstraints gbc_btnNull8 = new GridBagConstraints();
		gbc_btnNull8.fill = GridBagConstraints.BOTH;
		gbc_btnNull8.insets = new Insets(0, 0, 5, 5);
		gbc_btnNull8.gridx = 1;
		gbc_btnNull8.gridy = 4;
		panel_Buttons.add(btnNull8, gbc_btnNull8);
		
		btnE = new JButton("E");
		btnE.addActionListener(this);
		btnE.setEnabled(false);
		GridBagConstraints gbc_btnE = new GridBagConstraints();
		gbc_btnE.fill = GridBagConstraints.BOTH;
		gbc_btnE.insets = new Insets(0, 0, 5, 5);
		gbc_btnE.gridx = 2;
		gbc_btnE.gridy = 4;
		panel_Buttons.add(btnE, gbc_btnE);
		
		btn1 = new JButton("1");
		btn1.addActionListener(this);
		GridBagConstraints gbc_btn1 = new GridBagConstraints();
		gbc_btn1.fill = GridBagConstraints.BOTH;
		gbc_btn1.insets = new Insets(0, 0, 5, 5);
		gbc_btn1.gridx = 3;
		gbc_btn1.gridy = 4;
		panel_Buttons.add(btn1, gbc_btn1);
		
		btn2 = new JButton("2");
		btn2.addActionListener(this);
		GridBagConstraints gbc_btn2 = new GridBagConstraints();
		gbc_btn2.fill = GridBagConstraints.BOTH;
		gbc_btn2.insets = new Insets(0, 0, 5, 5);
		gbc_btn2.gridx = 4;
		gbc_btn2.gridy = 4;
		panel_Buttons.add(btn2, gbc_btn2);
		
		btn3 = new JButton("3");
		btn3.addActionListener(this);
		GridBagConstraints gbc_btn3 = new GridBagConstraints();
		gbc_btn3.fill = GridBagConstraints.BOTH;
		gbc_btn3.insets = new Insets(0, 0, 5, 5);
		gbc_btn3.gridx = 5;
		gbc_btn3.gridy = 4;
		panel_Buttons.add(btn3, gbc_btn3);
		
		btnSubtract = new JButton("-");
		btnSubtract.addActionListener(this);
		GridBagConstraints gbc_btnSubtract = new GridBagConstraints();
		gbc_btnSubtract.fill = GridBagConstraints.BOTH;
		gbc_btnSubtract.insets = new Insets(0, 0, 5, 5);
		gbc_btnSubtract.gridx = 6;
		gbc_btnSubtract.gridy = 4;
		panel_Buttons.add(btnSubtract, gbc_btnSubtract);
		
		JButton btnNull9 = new JButton(" ");
		btnNull9.setEnabled(false);
		GridBagConstraints gbc_btnNull9 = new GridBagConstraints();
		gbc_btnNull9.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNull9.insets = new Insets(0, 0, 0, 5);
		gbc_btnNull9.gridx = 0;
		gbc_btnNull9.gridy = 5;
		panel_Buttons.add(btnNull9, gbc_btnNull9);
		
		JButton btnNull10 = new JButton(" ");
		btnNull10.setEnabled(false);
		GridBagConstraints gbc_btnNull10 = new GridBagConstraints();
		gbc_btnNull10.fill = GridBagConstraints.BOTH;
		gbc_btnNull10.insets = new Insets(0, 0, 0, 5);
		gbc_btnNull10.gridx = 1;
		gbc_btnNull10.gridy = 5;
		panel_Buttons.add(btnNull10, gbc_btnNull10);
		
		btnF = new JButton("F");
		btnF.addActionListener(this);
		btnF.setEnabled(false);
		GridBagConstraints gbc_btnF = new GridBagConstraints();
		gbc_btnF.fill = GridBagConstraints.BOTH;
		gbc_btnF.insets = new Insets(0, 0, 0, 5);
		gbc_btnF.gridx = 2;
		gbc_btnF.gridy = 5;
		panel_Buttons.add(btnF, gbc_btnF);
		
		btn0 = new JButton("0");
		btn0.addActionListener(this);
		GridBagConstraints gbc_btn0 = new GridBagConstraints();
		gbc_btn0.fill = GridBagConstraints.BOTH;
		gbc_btn0.gridwidth = 2;
		gbc_btn0.insets = new Insets(0, 0, 0, 5);
		gbc_btn0.gridx = 3;
		gbc_btn0.gridy = 5;
		panel_Buttons.add(btn0, gbc_btn0);
		
		btnDecimal = new JButton(".");
		btnDecimal.setEnabled(false);
		GridBagConstraints gbc_btnDecimal = new GridBagConstraints();
		gbc_btnDecimal.fill = GridBagConstraints.BOTH;
		gbc_btnDecimal.insets = new Insets(0, 0, 0, 5);
		gbc_btnDecimal.gridx = 5;
		gbc_btnDecimal.gridy = 5;
		panel_Buttons.add(btnDecimal, gbc_btnDecimal);
		
		btnAdd = new JButton("+");
		btnAdd.addActionListener(this);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.BOTH;
		gbc_btnAdd.insets = new Insets(0, 0, 0, 5);
		gbc_btnAdd.gridx = 6;
		gbc_btnAdd.gridy = 5;
		panel_Buttons.add(btnAdd, gbc_btnAdd);
		
		btnEquals = new JButton("=");
		btnEquals.addActionListener(this);
		GridBagConstraints gbc_buttonEquals = new GridBagConstraints();
		gbc_buttonEquals.fill = GridBagConstraints.BOTH;
		gbc_buttonEquals.gridheight = 2;
		gbc_buttonEquals.gridx = 7;
		gbc_buttonEquals.gridy = 4;
		panel_Buttons.add(btnEquals, gbc_buttonEquals);
		
		//Create Number System  Radio buttons, and start with decimal as default
		rdbtnDec = new JRadioButton("Dec");
		rdbtnDec.setSelected(true);
		rdbtnDec.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnDec.isSelected()){
					//convert display value to current number system
					display.setText(convertToDecString());
					//add number system to queue
					setPrevNumSystem("Dec");
					disableEnableDigits();
				}
			}
		});
		rdbtnDec.addActionListener(this);
		btnGroup_NumSystem.add(rdbtnDec);
		
		rdbtnHex = new JRadioButton("Hex");
		rdbtnHex.addItemListener(new ItemListener() {
			//makes necessary conversions when switching to Hexidecimal system
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnHex.isSelected()){
					//convert display value to current number system
					display.setText(convertToHexString());
					//add number system to queue
					setPrevNumSystem("Hex");
					disableEnableDigits();
				}
			}
		});
		rdbtnHex.addActionListener(this);
		btnGroup_NumSystem.add(rdbtnHex);
		
		rdbtnOct = new JRadioButton("Oct");
		rdbtnOct.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnOct.isSelected()){
					//convert display value to current number system
					display.setText(convertToOctString());
					//add number system to queue
					setPrevNumSystem("Oct");
					disableEnableDigits();
				}
			}
		});
		rdbtnOct.addActionListener(this);
		btnGroup_NumSystem.add(rdbtnOct);
		
		rdbtnBin = new JRadioButton("Bin");
		rdbtnBin.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnBin.isSelected()){
					//convert display value to current number system
					display.setText(convertToBinString());
					//add number system to queue
					setPrevNumSystem("Bin");
					disableEnableDigits();
				}
			}
		});
		rdbtnBin.addActionListener(this);
		btnGroup_NumSystem.add(rdbtnBin);
		
		//create array for alphanumerals
		alphaNumerals = new JButton[16];
		alphaNumerals[0] = btn0;
		alphaNumerals[1] = btn1;
		alphaNumerals[2] = btn2;
		alphaNumerals[3] = btn3;
		alphaNumerals[4] = btn4;
		alphaNumerals[5] = btn5;
		alphaNumerals[6] = btn6;
		alphaNumerals[7] = btn7;
		alphaNumerals[8] = btn8;
		alphaNumerals[9] = btn9;
		alphaNumerals[10] = btnA;
		alphaNumerals[11] = btnB;
		alphaNumerals[12] = btnC;
		alphaNumerals[13] = btnD;
		alphaNumerals[14] = btnE;
		alphaNumerals[15] = btnF;

		GroupLayout gl_panel_NumSystem = new GroupLayout(panel_NumSystem);
		gl_panel_NumSystem.setHorizontalGroup(
			gl_panel_NumSystem.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_NumSystem.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_NumSystem.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnBin)
						.addComponent(rdbtnOct)
						.addGroup(gl_panel_NumSystem.createParallelGroup(Alignment.TRAILING)
							.addComponent(rdbtnDec, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(rdbtnHex, Alignment.LEADING)))
					.addContainerGap(73, Short.MAX_VALUE))
		);
		gl_panel_NumSystem.setVerticalGroup(
			gl_panel_NumSystem.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_NumSystem.createSequentialGroup()
					.addComponent(rdbtnHex, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnDec, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnOct, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnBin, GroupLayout.PREFERRED_SIZE, 21, Short.MAX_VALUE))
		);
		panel_NumSystem.setLayout(gl_panel_NumSystem);
		mainPanel.setLayout(gl_mainPanel);
		display.setText("0");
	}

	//method that determines which action to take based on buttons pressed
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (display.getText().startsWith("ERROR")){
			display.setText("0");
		}

		//If user checks hide calculator button
		if(e.getSource() == menuItem_hideCalc){
			//hide main panel visibility if box is checked
			mainPanel.setVisible(!menuItem_hideCalc.getState());
		}
		//If source is the copy menu item
		else if(e.getSource() == menuItem_Copy){
			//get text from display screen
			StringSelection stringSelection = new StringSelection(display.getText());
			//put text on clipboard
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}		
		//If source is the help menu item
		else if(e.getSource()==menuItem_Help) {
			//create a popup dialog containing a label containing a picture of help manual
			JDialog myDialog = new JDialog();
			myDialog.setVisible(true);
			JLabel myLabel = new JLabel(new ImageIcon("help.png"));
			myDialog.getContentPane().add(myLabel);
			myDialog.pack();	
		}
		
		//If source is the delete (<-) button and there is text in the display screen
		else if (e.getSource() == btnLeftArrow && !display.getText().isEmpty()) {
			//delete last character on the display screen
			display.setText(display.getText().substring(0, display.getText().length()-1));
		}
		//If source is the Clear button ("C" or "CE")
		else if (e.getSource() == btnClear || e.getSource() == btnCE){
			//empty out display screen and reset place holders
			display.setText("0");
			a = 0;
			b = 0;
			result = 0;
			operator = "none";
		}

		//If source is a number or hex-digit button
		else if(e.getSource() == btn0){
			display.setText(display.getText().concat("0"));
		}
		else if(e.getSource() == btn1){
			display.setText(display.getText().concat("1"));
		}
		else if(e.getSource() == btn2){
			display.setText(display.getText().concat("2"));
		}
		else if(e.getSource() == btn3){
			display.setText(display.getText().concat("3"));
		}
		else if(e.getSource() == btn4){
			display.setText(display.getText().concat("4"));
		}
		else if(e.getSource() == btn5){
			display.setText(display.getText().concat("5"));
		}
		else if(e.getSource() == btn6){
			display.setText(display.getText().concat("6"));
		}
		else if(e.getSource() == btn7){
			display.setText(display.getText().concat("7"));
		}
		else if(e.getSource() == btn8){
			display.setText(display.getText().concat("8"));
		}
		else if(e.getSource() == btn9){
			display.setText(display.getText().concat("9"));
		}
		else if(e.getSource() == btnA){
			display.setText(display.getText().concat("A"));
		}
		else if(e.getSource() == btnB){
			display.setText(display.getText().concat("B"));
		}
		else if(e.getSource() == btnC){
			display.setText(display.getText().concat("C"));
		}
		else if(e.getSource() == btnD){
			display.setText(display.getText().concat("D"));
		}
		else if(e.getSource() == btnE){
			display.setText(display.getText().concat("E"));
		}
		else if(e.getSource() == btnF){
			display.setText(display.getText().concat("F"));
		}
		
		//Arithmetic features

		else if (e.getSource() == btnNegative){
			
			int displayVal = 666;
			
			if (rdbtnDec.isSelected()){
				//if value is 0, do nothing
				if (display.getText() == "0"){
					return;
				}
				//if val is negative
				else if (display.getText().indexOf('-') >= 0) {
					//remove '-' sign
					display.setText(display.getText().replace("-", ""));
				}
				else {
					//add a negative sign to the display value.
					//concatenation is reversed due to right-to-left orientation
					display.setText("-"+display.getText());
				}
			}
			else {	
				if (rdbtnHex.isSelected()){
					displayVal = (int)Long.parseLong(display.getText(), 16) * -1;
					display.setText(Integer.toHexString(displayVal).toUpperCase());
				}
				else if (rdbtnOct.isSelected()){
					displayVal = (int)Long.parseLong(display.getText(), 8) * -1;
					display.setText(Integer.toOctalString(displayVal));
				}
				else if (rdbtnBin.isSelected()){
					displayVal = (int)Long.parseLong(display.getText(), 2) * -1;
					display.setText(Integer.toBinaryString(displayVal));
				}
			}
		}
		
		//arithmetic features
		else if(e.getSource() == btnAdd)
        {
			//get a
			a = parseAndConvert();
			//reset screen
            display.setText("0");
            //update operator
			operator = "add";
        }
		else if(e.getSource() == btnSubtract)
        {
			a = parseAndConvert();
            display.setText("0");
			operator = "subtract";
        }
		else if(e.getSource() == btnMult)
        {
			a = parseAndConvert();
            display.setText("0");
			operator = "multiply";
        }
		else if(e.getSource() == btnDivide)
        {
			a = parseAndConvert();
            display.setText("0");
			operator = "divide";
        }
		else if(e.getSource() == btnMod)
        {
			a = parseAndConvert();
            display.setText("0");
			operator = "mod";
        }
		
		//if = button is pressed
		else if (e.getSource() == btnEquals && operator!= "none") {
			
			//this flag is true if illegal operation will be performed
			Boolean undefined = false;
			
			//get b
			b = parseAndConvert();
			
			//compute result based on operation type
			if (operator == "add") {
				result = a+b;
			}
			else if (operator == "subtract") {
				result = a-b;
			}
			else if (operator == "multiply") {
				result = a*b;
			}
			else if (operator == "divide") {
				//do not try to divide by 0
				if (b!=0){
					result = a/b;
				}
				else{
					//switch to true if divisor will be 0
					undefined = true;
				}
			}
			else if (operator == "mod") {
				if (b!=0){
					result = a%b;
				}
				else{
					undefined = true;
				}
			}
			//print ERROR if illegal operation occured
			if (undefined){
				display.setText("ERROR: Cannot divide or modulo by 0");
			}
			else {
				//print answer to screen
				display.setText(convertedResult());
			}
			//reset operator
			operator = "none";
		}
		leadingZero();
		updateBinaryField();
		fixNegative2();
	}
	
	//enables and disables components based on which number system is being used
	public void disableEnableDigits() {
	
		//disable all digits and letters
		for (int i = 0; i < alphaNumerals.length; i++) {
			alphaNumerals[i].setEnabled(false);
		}
		
		if (rdbtnHex.isSelected()){
			//enable all 15 digits from 0-F
			for (int i = 0; i < alphaNumerals.length; i++) {
				alphaNumerals[i].setEnabled(true);
			}
		}
		else if (rdbtnDec.isSelected()) {
			for (int i = 0; i <= 9; i++) {
				alphaNumerals[i].setEnabled(true);
			}
		}
		else if (rdbtnOct.isSelected()) {
			for (int i = 0; i <= 7; i++) {
				alphaNumerals[i].setEnabled(true);
			}
		}
		else if (rdbtnBin.isSelected()) {
			for (int i = 0; i <= 1; i++) {
				alphaNumerals[i].setEnabled(true);
			}
		}
	}
	
	//mutator
	public void setPrevNumSystem(String s) {
		prevNumSystem = s;
	}
	
	//returns hex string of number on screen, pass True as argument to return opposite sign value
	public String convertToHexString() {

		//if 666 returns, something went wrong
		int displayVal = 666;

		//determine which number system is currently displayed
		if (prevNumSystem == "Dec"){
			//negatives will appear at the end. This puts the sign in front
			if (display.getText().indexOf('-')>=0){
				display.setText(display.getText().replace("-", ""));
				displayVal = (int)Long.parseLong(display.getText()) * -1;
			}
			else{
			//convert display to a decimal value
			displayVal = (int)Long.parseLong(display.getText());
			}
		}
		else if (prevNumSystem == "Oct"){
			//convert display value from octal to decimal value
			displayVal = (int)Long.parseLong(display.getText(), 8);
		}
		else if (prevNumSystem == "Bin"){
			//convert from Binary to Dec
			displayVal = (int)Long.parseLong(display.getText(), 2);
		}
		//return hex string
		return Integer.toHexString(displayVal).toUpperCase();
	}
	
	//returns decimal string of number on screen
	public String convertToDecString() {
		
		//numerical value of screen text
		int displayVal = 666;
		
		//determine which number system is currently displayed
		if (prevNumSystem == "Hex"){
			//convert display to a decimal value
			//EXCEPTION FROM OVERFLOW
			displayVal = (int)Long.parseLong(display.getText(), 16);
		}
		else if (prevNumSystem == "Oct"){
			//convert display value from octal to decimal value
			displayVal = (int)Long.parseLong(display.getText(), 8);
		}
		else if (prevNumSystem == "Bin"){
			//convert from Binary to Dec
			displayVal = (int)Long.parseLong(display.getText(), 2);
		}
		else if (prevNumSystem == "Dec"){
			//negatives will appear at the end. This puts the sign in front
			if (display.getText().indexOf('-')>=0){
				display.setText(display.getText().replace("-", ""));
				displayVal = (int)Long.parseLong(display.getText()) * -1;
			}
			else{
			//convert display to a decimal value
			displayVal = (int)Long.parseLong(display.getText());
			}
		}
		//return dec string
		return Integer.toString(displayVal);
	}
	
	//returns Octal string of number on screen
	public String convertToOctString() {
		
		//numerical value of screen text
		int displayVal = 666;
		
		//determine which number system is currently displayed
		if (prevNumSystem == "Hex"){
			//convert display to a decimal value
			displayVal = (int)Long.parseLong(display.getText(), 16);
		}
		else if (prevNumSystem == "Dec"){
			//negatives will appear at the end. This puts the sign in front
			if (display.getText().indexOf('-')>=0){
				display.setText(display.getText().replace("-", ""));
				displayVal = (int)Long.parseLong(display.getText()) * -1;
			}
			else{
			//convert display to a decimal value
			displayVal = (int)Long.parseLong(display.getText());
			}
		}
		else if (prevNumSystem == "Bin"){
			//convert from Binary to Dec
			displayVal = (int)Long.parseLong(display.getText(), 2);
		}
		//return octal string
		return Integer.toOctalString(displayVal);
	}

	//returns Octal string of number on screen
	public String convertToBinString() {

		//numerical value of screen text
		int displayVal = 666;
		
		//determine which number system is currently displayed
		if (prevNumSystem == "Hex"){
			//convert display to a decimal value
			displayVal = (int)Long.parseLong(display.getText(), 16);
		}
		else if (prevNumSystem == "Dec"){
			//negatives will appear at the end. This puts the sign in front
			if (display.getText().indexOf('-')>=0){
				display.setText(display.getText().replace("-", ""));
				displayVal = (int)Long.parseLong(display.getText()) * -1;
			}
			else{
			//convert display to a decimal value
			displayVal = (int)Long.parseLong(display.getText());
			}
		}
		else if (prevNumSystem == "Oct"){
			//convert from Oct to Dec
			displayVal = (int)Long.parseLong(display.getText(), 8);
		}
		//return binary string
		return Integer.toBinaryString(displayVal);
	}
	//eliminates leading zeroes unless in binary mode
	public void leadingZero(){
		if (!rdbtnBin.isSelected()){
			if (display.getText().length() > 1 && display.getText().indexOf('0') == 0){
				display.setText(display.getText().substring(1));
			}
		}
	}
	//puts negative sign at the beginning of display instead of end due to text area orientation
	public void fixNegative(){
		if (display.getText().indexOf('-')>=0){
			display.setText(display.getText().replace("-", ""));
			display.setText("-"+display.getText());
		}
	}
	public void fixNegative2(){
		if (display.getText().indexOf('-')>=0){
			display.setText(display.getText().replace("-", ""));
			display.setText(display.getText()+"-");
		}
	}
	//returns a decimal value of display value of any number system
	public int parseAndConvert(){
		if (rdbtnHex.isSelected()){
			return (int)Long.parseLong(display.getText(), 16);
		}
		else if (rdbtnDec.isSelected()){
			fixNegative();
			return (int)Long.parseLong(display.getText());
		}
		else if (rdbtnOct.isSelected()){
			return (int)Long.parseLong(display.getText(), 8);
		}
		else {
			return (int)Long.parseLong(display.getText(), 2);
		}
	}
	
	//returns a string with a converted value based on number system selected
	public String convertedResult() {
		if (rdbtnHex.isSelected()){
			return Integer.toHexString(result).toUpperCase();
		}
		else if (rdbtnDec.isSelected()){
			if (result < 0) {
				result*=-1;
				return (result+"-");
			}
			else {
				return (result+"");
			}
		}
		else if (rdbtnOct.isSelected()){
			return Integer.toOctalString(result);
		}
		else {
			return Integer.toBinaryString(result);
		}
	}
	
	//updates binary field to reflect screen value
	public void updateBinaryField(){
		//dont call method if display isnt a number
		if (display.getText().startsWith("ERROR") || display.getText() == "-"){
			return;
		}
		
		binFieldVal = parseAndConvert();
		binFieldText = Integer.toBinaryString(binFieldVal);
		String padded = "00000000000000000000000000000000"
					  + "00000000000000000000000000000000"
				.substring(binFieldText.length()) + binFieldText;
		
		//format display
		String s1 = padded.substring(0, 4);
		String s2 = padded.substring(4, 8);
		String s3 = padded.substring(8, 12);
		String s4 = padded.substring(12, 16);
		String s5 = padded.substring(16, 20);
		String s6 = padded.substring(20, 24);
		String s7 = padded.substring(24, 28);
		String s8 = padded.substring(28, 32);
		String s9 = padded.substring(32, 36);
		String s10 = padded.substring(36, 40);
		String s11 = padded.substring(40, 44);
		String s12 = padded.substring(44, 48);
		String s13 = padded.substring(48, 52);
		String s14 = padded.substring(52, 56);
		String s15 = padded.substring(56, 60);
		String s16 = padded.substring(60, 64);
		String pad = " ";
		
		padded = "   "+s1+pad+s2+pad+s3+pad+s4+pad+s5+pad+s6+pad+
				 s7+pad+s8+"\n   "+s9+pad+s10+pad+s11+pad+s12+pad+
				 s13+pad+s14+pad+s15+pad+s16;
		//print to binary field
		binaryField.setText(padded);
	}
}

























