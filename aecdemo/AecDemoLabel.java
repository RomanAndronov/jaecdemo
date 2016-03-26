package aecdemo;

/*
   By Roman Andronov
 */

import java.awt.Color;
import javax.swing.JLabel;

class AecDemoLabel
	extends JLabel
{
	AecDemoLabel()
	{
		super();
		item = new AecDemoItem();

		clrBg = getBackground();
		clrFg = getForeground();
	}

	void
	addItem( AecDemoItem src )
	{
		item.copyFrom( src );

		setText( item.type == 'a' ?
			"" + item.operand :
			"" + item.sym );
	}

	void
	rmItem()
	{
		item.erase();
		setText( "" );
	}

	void
	select( boolean s )
	{
		if ( s )
		{
			setBackground( clrFg );
			setForeground( clrBg );
			setOpaque( true );
		}
		else
		{
			setBackground( clrBg );
			setForeground( clrFg );
			setOpaque( false );
		}
	}


	AecDemoItem		item = null;
	Color			clrBg = null;
	Color			clrFg = null;
}
