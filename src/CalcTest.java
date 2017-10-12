//Author: 	Francisco Lynn
//Date: 	7/16/17
//Description:  This program is a clone of the Windows 9 Programmer Calculator

import javax.swing.JFrame;

public class CalcTest {

	public static void main(String[] args) {

		Calculator calculator = new Calculator();
		//set window size and color
		calculator.setSize(550,385);
		//disable screen stretching
		calculator.setResizable(false);
		//set visibility
		calculator.setVisible(true);
		//set title text
		calculator.setTitle("Calculator");
		//have program exit when the close window button is pressed
		calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
