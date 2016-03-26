package aecdemo;

/*
   By Roman Andronov
*/

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

public
class AecDemoApplet
	extends JApplet
{
	public void
	init()
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			public void
			run()
			{
				createAppletGui();
			}
		});
	}

	private void
	createAppletGui()
	{
		if ( adpnl == null )
		{
			adpnl = new AecDemoPanel( this, false );
		}
	}

	private AecDemoPanel		adpnl = null;
}
