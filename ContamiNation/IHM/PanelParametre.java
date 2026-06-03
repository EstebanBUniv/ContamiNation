package ContamiNation.IHM;

import ContamiNation.Controleur;
import ContamiNation.IHM.FrameParametre;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Flow;

import javax.swing.*;

public class PanelParametre extends JPanel implements ActionListener
{
	private FrameParametre frameMere;

	private JButton btnValider;
	private JButton btnAnnuler;

	private JTextField txtLigne;
	private JTextField txtColonne;
	private JTextField txtNbVirus;

	private JPanel panelBouton;
	private JPanel panelTxt;

	public PanelParametre(FrameParametre frameMere)
	{
		this.frameMere = frameMere;

		this.setLayout(new BorderLayout());

		//Création

		this.btnAnnuler = new JButton("Annuler");
		this.btnValider = new JButton("Valider");

		this.txtColonne = new JTextField(30);
		this.txtLigne   = new JTextField(30);
		this.txtNbVirus = new JTextField(30);

		this.panelBouton = new JPanel();
		this.panelBouton.setLayout(new FlowLayout());
		this.panelTxt    = new JPanel();
		this.panelTxt.setLayout(new GridLayout(6, 1));

		//Placement

		this.panelBouton.add(this.btnValider);
		this.panelBouton.add(this.btnAnnuler);

		this.panelTxt.add( new JLabel("Nombre de lignes :"));
		this.panelTxt.add( this.txtLigne );
		this.panelTxt.add( new JLabel("Nombre de colonnes :"));
		this.panelTxt.add( this.txtColonne );
		this.panelTxt.add( new JLabel("Nombre de virus :"));
		this.panelTxt.add( this.txtNbVirus );

		this.add(this.panelTxt);
		this.add(this.panelBouton, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e)
	{
		return;
	}
}