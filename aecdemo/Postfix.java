package aecdemo;

/*
   By Roman Andronov
 */

class Postfix
	implements AeNotation
{
	Postfix( AecDemoPanel adpnl)
	{
		pnlAD = adpnl;
	}

	public char
	openingOOEM()
	{
		return '(';
	}

	public char
	closingOOEM()
	{
		return ')';
	}

	public void
	resetConvert()
	{
		currInQItem = 0;
		currOutQItem = 0;
		outQSz = 0;

		if ( pnlAD.dbg )
		{
			System.out.println(
				"Postfix.java:resetConvert(): " +
				"currInQItem = " + currInQItem +
				", currOutQItem = " + currOutQItem +
				", outQSz = " + outQSz );
		}
	}

	public void
	resetCompute()
	{
		pnlAD.outQ[ outQSz - 1 ].select( false );
		currOutQItem = 0;

		if ( pnlAD.dbg )
		{
			System.out.println(
				"Postfix.java:resetCompute(): " +
				"currOutQItem = " + currOutQItem +
				", outQSz = " + outQSz );
		}
	}

	/*
	   Input queue is read left to right
	   starting from the left-most item
	 */
	public AecDemoItem
	getInputItem()
	{
		AecDemoItem		item = null;

		if ( currInQItem > 0 )
		{
			pnlAD.inQ[ currInQItem - 1 ].select( false );
		}

		if ( currInQItem < pnlAD.inQSz )
		{
			pnlAD.inQ[ currInQItem ].select( true );
			item = pnlAD.inQ[ currInQItem ].item;
			currInQItem++;
		}

		if ( pnlAD.dbg )
		{
			System.out.println(
				"Postfix.java:getInputItem(): " +
				"currInQItem = " + currInQItem );
		}

		return  item;
	}

	/*
	   Postfix queue is accessed from head to tail
	 */
	public void
	outQAdd( AecDemoItem item )
	{
		if ( currOutQItem > 0 )
		{
			pnlAD.outQ[ currOutQItem - 1 ].select( false );
		}

		if ( currOutQItem < pnlAD.outQ.length )
		{
			pnlAD.outQ[ currOutQItem ].addItem( item );
			pnlAD.outQ[ currOutQItem ].select( true );
			currOutQItem++;
			outQSz++;
		}

		if ( pnlAD.dbg )
		{
			System.out.println(
				"Postfix.java:outQAdd(): " +
				"currOutQItem = " + currOutQItem +
				", outQSz = " + outQSz );
		}
	}

	public AecDemoItem
	getOutputItem()
	{
		AecDemoItem		item = null;

		if ( currOutQItem > 0 )
		{
			pnlAD.outQ[ currOutQItem - 1 ].select( false );
		}

		if ( currOutQItem < outQSz )
		{
			pnlAD.outQ[ currOutQItem ].select( true );
			item = pnlAD.outQ[ currOutQItem ].item;
			currOutQItem++;
		}

		if ( pnlAD.dbg )
		{
			System.out.println(
				"Postfix.java:getOutputItem(): " +
				"currOutQItem = " + currOutQItem );
		}

		return item;
	}


	private AecDemoPanel		pnlAD;

	private int			currInQItem;

	private int			currOutQItem;
	private int			outQSz;
}
