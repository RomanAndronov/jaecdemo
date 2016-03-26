package aecdemo;

/*
   By Roman Andronov
 */

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

/*
   This will execute AecDemo as a stand-alone
   Java program.

   To execute it as a Java applet consult the
   AecDemoApplet class in this package
*/

public
class AecDemoFrame
	extends JFrame
{
	public
	AecDemoFrame()
	{
		super();

		setTitle( "AEC Demonstration" );
		setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
	}

	public static void
	main( String[] args )
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			public void
			run()
			{
				boolean			dbg = false;
				String			dbgstr = null;
				AecDemoFrame		adfrm = new AecDemoFrame();

				dbgstr = System.getProperty( "dbg", null );
				if ( dbgstr != null )
				{
					dbg = true;
				}

				adfrm.adpnl = new AecDemoPanel( adfrm, dbg );
				adfrm.pack();
				adfrm.setLocationRelativeTo( null );
				adfrm.setVisible( true );
			}
		});

	}

	private AecDemoPanel		adpnl = null;
}
