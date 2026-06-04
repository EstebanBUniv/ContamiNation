package ContamiNation_Creer.IHM;

import javax.swing.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.Metier.Sommet;

public class PanelBase extends JPanel implements ActionListener
{
	private Controleur  ctrl;
	private JButton[][] tabBtn;
	private int         cptVirus;

	public PanelBase(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.cptVirus = 0;
		this.setLayout(new GridLayout(this.ctrl.getLig(),this.ctrl.getCol() ));

		this.tabBtn = new JButton[this.ctrl.getLig()][this.ctrl.getCol()];

		for (int lig = 0; lig < this.ctrl.getLig(); lig++)
		{
			for (int col = 0; col < this.ctrl.getCol(); col++)
			{
				this.tabBtn[lig][col] = ctrl.getButton(lig, col);
				this.add(this.tabBtn[lig][col]);
				this.tabBtn[lig][col].addActionListener(this);
			}
		}

	}

	public void actionPerformed(ActionEvent e)
	{

			for (int lig = 0; lig < this.ctrl.getLig(); lig++)
			{
				for (int col = 0; col < this.ctrl.getCol(); col++)
				{
					if (e.getSource() == this.tabBtn[lig][col])
					{
						Sommet s = ctrl.getCase(lig, col).getSommet();

						if (s != null && this.cptVirus < this.ctrl.getNbVirus())
						{
							s.setBase(true);
							ctrl.getButton(lig, col).setBackground(Color.WHITE);
							this.cptVirus++;
							
							//if (cptVirus <= this.ctrl.getNbVirus() )
								//Fermer la frame
						}
					}
				}
			}
	}
}