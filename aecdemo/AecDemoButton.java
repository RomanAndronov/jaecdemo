package aecdemo;

/*
   By Roman Andronov
 */

import javax.swing.JButton;

class AecDemoButton
	extends JButton
{
	AecDemoButton( AecDemoItem src )
	{
		super();

		item = new AecDemoItem();
		item.copyFrom( src );

		setText( item.type == 'a' ?
			"" + item.operand :
			"" + item.sym );
	}

	AecDemoItem		item = null;
}
