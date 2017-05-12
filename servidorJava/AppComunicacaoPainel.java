///////////////////////////////////////////////////////////////////////////////////////
// Name: AppComunicacaoPanel.java
// Authors: Rian Martins
// Description: Create view of communication app
// Date: 05/05/2017
///////////////////////////////////////////////////////////////////////////////////////

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AppComunicacaoPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	private JPanel appPanel; // main panel
	private JPanel selecaoCurvaPanel;
	private JPanel plotcurvaPanel;
	private JPanel esqPanel;
	private JPanel plotcurvaPanelNorte;
	private JPanel plotcurvaPanelSul;
	
	private JLabel graficoLabel; // Label do grafico
	private JLabel selecaoCurvaLabel; // Label de seleção de curva
	private JLabel statusLabel; // mostra status da conexão
	private JLabel curvaLabel; // label que indica a curva em ação
	private JLabel pwmLabel; // label que indica valor do pwm
	
	private PlotCurvaSecagem curvaSecagemPlotPanel; // painel de plot do grafico de secagem
	
	private JRadioButton curva1; // opção de curva 1
	private JRadioButton curva2; // opção de curva 2
	private ButtonGroup selecaoCurvaRadioButtons; // Grupo de opções para selecao de curva
	private int valorCurvaSelecionada;

	private JButton ligaBotao; // Botao de ligar
	
	private RecebendoDados dadosFromCliente;
	
	public AppComunicacaoPanel() throws IOException {
		
		graficoLabel = new JLabel("Curva de Secagem:                                                                                                                          ");
		selecaoCurvaLabel = new JLabel("Selecione a curva de secagem:");
		statusLabel = new JLabel("Status:");
		curvaLabel = new JLabel("Curva Selecionada: ");
		pwmLabel = new JLabel("PWM: ");
		
		dadosFromCliente = new RecebendoDados();	
		curvaSecagemPlotPanel = new PlotCurvaSecagem();
		
		statusLabel.setText("Status: " + dadosFromCliente.getStatus());
		
		plotcurvaPanelNorte = new JPanel();
		plotcurvaPanelNorte.setLayout(new BoxLayout(plotcurvaPanelNorte, BoxLayout.Y_AXIS));
		plotcurvaPanelNorte.add(graficoLabel);
		plotcurvaPanelNorte.add(Box.createRigidArea(new Dimension(0,15)));
		
		plotcurvaPanelSul = new JPanel();
		plotcurvaPanelSul.setLayout(new BoxLayout(plotcurvaPanelSul, BoxLayout.Y_AXIS));
		plotcurvaPanelSul.add(Box.createRigidArea(new Dimension(0,10)));
		plotcurvaPanelSul.add(statusLabel);
			
		plotcurvaPanel = new JPanel();
		plotcurvaPanel.setLayout(new BorderLayout());
		plotcurvaPanel.add(plotcurvaPanelNorte, BorderLayout.NORTH);
		plotcurvaPanel.add(curvaSecagemPlotPanel, BorderLayout.CENTER);
		plotcurvaPanel.add(plotcurvaPanelSul, BorderLayout.SOUTH);
		
		//Radio Buttons
		curva1 = new JRadioButton("1. Curva Padrão"); //male option for sex radio button
		curva2 = new JRadioButton("2. Curva Constante"); //female option for sex radio button
		selecaoCurvaRadioButtons = new ButtonGroup(); //creates group of radio buttons and add them to the group
		selecaoCurvaRadioButtons.add(curva1);
		selecaoCurvaRadioButtons.add(curva2);
		valorCurvaSelecionada = 0;
		//adiciona listener
		curva1.addActionListener(new CurvaSelectionListener());
		curva2.addActionListener(new CurvaSelectionListener());
		
		selecaoCurvaPanel = new JPanel();
		selecaoCurvaPanel.setLayout(new BoxLayout(selecaoCurvaPanel, BoxLayout.Y_AXIS));
		selecaoCurvaPanel.add(selecaoCurvaLabel);
		selecaoCurvaPanel.add(curva1);
		selecaoCurvaPanel.add(curva2);
		
		ligaBotao = new JButton("Ligar");
		ligaBotao.addActionListener(new ButtonListener());
		
		esqPanel = new JPanel();
		esqPanel.setLayout(new BoxLayout(esqPanel, BoxLayout.Y_AXIS));
		esqPanel.add(selecaoCurvaPanel);
		esqPanel.add(Box.createRigidArea(new Dimension(0,10)));
		esqPanel.add(ligaBotao);
		esqPanel.add(Box.createRigidArea(new Dimension(0,50)));
		esqPanel.add(curvaLabel);
		esqPanel.add(Box.createRigidArea(new Dimension(0,10)));
		esqPanel.add(pwmLabel);
		esqPanel.add(Box.createRigidArea(new Dimension(0,150)));
		
		appPanel = new JPanel();
		appPanel.setLayout(new BorderLayout());
		
		appPanel.add(esqPanel, BorderLayout.WEST);
		appPanel.add(Box.createRigidArea(new Dimension(50,0)));
		appPanel.add(plotcurvaPanel, BorderLayout.EAST);
		
		add(appPanel);
	}
	
	// Listener que verifica click do botao
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			statusLabel.setText("Status: " + dadosFromCliente.getStatus());
			dadosFromCliente.setCurvaSelecionada(valorCurvaSelecionada);
			if(valorCurvaSelecionada == 0)
				statusLabel.setText("Status: Favor selecionar a curva");
			else{
				curvaLabel.setText("Curva Selecionada: " + valorCurvaSelecionada);
				try {
					dadosFromCliente.startRetrievingData();
				} catch (IOException e) {
					e.printStackTrace();
				}
				statusLabel.setText("Status: " + dadosFromCliente.getStatus());
				pwmLabel.setText("PWM: " + dadosFromCliente.getPWM());
			}
		}
	}
	
	//Listener que verifica selecao da curva
	private class CurvaSelectionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Object select = event.getSource();

			if(select == curva1)
				valorCurvaSelecionada = 1;
			else if (select == curva2)
				valorCurvaSelecionada = 2;
		}
	}


}
