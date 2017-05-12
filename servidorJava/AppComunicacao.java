///////////////////////////////////////////////////////////////////////////////////////
// Name: AppComunicacao.java
// Authors: Rian Martins
// Description: Create view of communication app
// Date: 05/05/2017
///////////////////////////////////////////////////////////////////////////////////////

import java.io.IOException;

//import javax.swing.JApplet;
import javax.swing.JFrame;

public class AppComunicacao extends JFrame
{
	private static final long serialVersionUID = 1L;

	
	public static void main(String[] args) throws IOException
	{		
		// create a Login object and add it to the applet
		JFrame appFrame = new JFrame("Sistema de Comunicação de Dados");
		
		AppComunicacaoPanel AppPanel = new AppComunicacaoPanel();
		appFrame.getContentPane().add(AppPanel);

		//set applet size to 500 X 500
		appFrame.setSize(800, 500); // set size of window
		appFrame.setVisible(true);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set default close operation
		appFrame.setLocationRelativeTo(null);
	}

}
