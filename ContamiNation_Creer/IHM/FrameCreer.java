package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import java.awt.BorderLayout;
import javax.swing.*;
import java.io.File;
import java.awt.event.*;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class FrameCreer extends JFrame implements ActionListener
{
	private JPanel       panel;
	private JPanel       panelZone;


	private JButton     btnplusZone;
	private JLabel      numZoneActuelle;
	private JButton     btnmoinsZone;

	private int         cptZone;

	private Controleur   ctrl;

	public FrameCreer(Controleur ctrl) 
	{
		this.setTitle   ("ContamiNation");
		this.setSize    (900,600);
		this.setLocationRelativeTo(null);
		
		this.ctrl            = ctrl;
		this.panel           = new PanelSauvegarde(this, this.ctrl);
		this.panelZone       = new JPanel(new BorderLayout());
		this.btnplusZone        = new JButton("+");
		this.numZoneActuelle = new JLabel("1");
		this.btnmoinsZone       = new JButton("-");
		this.cptZone = 1;
		
		this.btnplusZone .addActionListener(this);
		this.btnmoinsZone.addActionListener(this);

		this.panelZone.add( this.btnplusZone, BorderLayout.NORTH    );
		this.panelZone.add(this.numZoneActuelle, BorderLayout.CENTER);
		this.panelZone.add(this.btnmoinsZone   , BorderLayout.SOUTH );

		this.setLayout(new BorderLayout());

		this.add(this.panel, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public JPanel getPanel()
	{
		return this.panel;
	}

	public Controleur getCtrl()
	{
		return this.ctrl;
  }
	public void changerGrille(int lig, int col)
	{
		this.ctrl.changerPanel(lig, col);
		this.setSize(800,600);
	}
	
	public void charger(File fichier)
	{
		this.ctrl.charger(fichier);
		this.changerPanel(panel);
	}

	public void creerPlateau()
	{
		this.changerPanel(new PanelParametre(this));
	}

	public void valider (int lig, int col, int nbVirus, String nomPlateau)
	{
		this.ctrl.creerPlateau(lig, col, nbVirus, nomPlateau);
		this.setSize    (300,300);
	}



	public void ajouterZone (int lig, int col)
	{
		this.ctrl.ajouterZone(lig, col, this.cptZone);
	}

	public void initBtn (String valeur, int lig, int col)
	{
		if (this.panel instanceof PanelGrille)	
			((PanelGrille)(this.panel)).initBtn(valeur, lig, col);
	}

	public void fermer()
	{
		this.ctrl.ouvrirSommet();
		this.dispose();
	}

	public void changerPanel(JPanel panel)
	{
		this.remove(this.panel);
		this.panel = panel;
		this.add(this.panel);
		this.revalidate();
		this.repaint();
	}

	public void setEstNouveau(boolean val)
	{
		if ( this.panel instanceof PanelGrille )
		{
			((PanelGrille)(this.panel)).setEstNouveau(val);
		}
			
	}

	public void creerVirus(String nom)
	{
		this.ctrl.creerVirus(nom);
	}
	
	public void ajouterPanel()
	{
		this.add(this.panelZone, BorderLayout.WEST  );
		this.revalidate();
	}
	
	public void retirerPanel()
	{
		this.remove(this.panelZone);
		this.revalidate();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnplusZone && this.cptZone < Integer.MAX_VALUE)
		{
			this.cptZone++;
			this.numZoneActuelle.setText(this.cptZone + "");
			this.panelZone.repaint();
		}

		if (e.getSource() == this.btnmoinsZone && this.cptZone > 1)
		{
			this.cptZone--;
			this.numZoneActuelle.setText(this.cptZone + "");
			this.panelZone.repaint();
		}		

	}
}