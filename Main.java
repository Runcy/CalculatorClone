/**
   Windows 8 Programmer Calculator Clone
   
   Purpose: Clone of the Windows 8 Programmer Calculator.

    @author Franco Lynn
    @version 1.00 7/03/2017
*/

import javax.swing.JFrame;

public class Main {

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
