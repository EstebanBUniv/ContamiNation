package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.GridLayout;

import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelMulti extends JPanel implements ActionListener
{
	private Controleur ctrl;
	
	private FrameJeu   frameMere;

	private JTextField txtServeurClient;
	private JButton    btnLocal;
	private JButton    btnReseauClient;
	private JButton    btnReseauServeur;
	
	public PanelMulti(FrameJeu frameMere, Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.frameMere = frameMere;

		this.setLayout(new GridLayout(4,1,10,10));
		
		/*-------------------------*/
		/* création des composants */
		/*-------------------------*/

		this.txtServeurClient = new JTextField(10);
		
		this.btnLocal         = new JButton("Jouer sur un PC");
		this.btnReseauClient  = new JButton("Etre Client");
		this.btnReseauServeur = new JButton("Etre serveur");

		/*-------------------------------*/
		/* positionnement des composants */
		/*-------------------------------*/
		
		this.add(this.btnLocal);
		this.add(this.btnReseauServeur);
		this.add(this.btnReseauClient);
		this.add(this.txtServeurClient);
		
		/* ------------------------------ */
		/* Activation des composants      */
		/* ------------------------------ */
        
		this.btnLocal .addActionListener(this);
		this.btnReseauServeur.addActionListener(this);
		this.btnReseauClient.addActionListener(this);
	}


	public void actionPerformed(ActionEvent e)
	{
		
		if(e.getSource() == this.btnLocal)
		{
			this.frameMere.changerPanel(new PanelNiveau(this.frameMere, this.ctrl, true));
		}
		if (e.getSource() == this.btnReseauClient)
			if (this.txtServeurClient.getText().matches("[0-9]+"))
				this.ctrl.lancerClient(Integer.parseInt(this.txtServeurClient.getText()));
		
		if (e.getSource() == this.btnReseauServeur)
			if (this.txtServeurClient.getText().matches("[0-9]+"))
				this.ctrl.lancerServeur(Integer.parseInt(this.txtServeurClient.getText()));
	}
}

