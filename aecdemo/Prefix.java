package aecdemo;

/*
   By Roman Andronov
 */

class Prefix
	implements AeNotation
{
	Prefix( AecDemoPanel adpnl )
	{
		pnlAD = adpnl;
	}

	public char
	openingOOEM()
	{
		return ')';
	}

	public char
	closingOOEM()
	{
		return '(';
	}

	public void
	resetConvert()
	{
		currInQItem = pnlAD.inQSz - 1;
		currOutQItem = pnlAD.outQ.length - 1;
		outQSz = 0;
		if ( pnlAD.dbg )
		{
			System.out.println(
				"Prefix.java:resetConvert(): " +
				"currInQItem = " + currInQItem +
				", currOutQItem = " + currOutQItem +
				", outQSz = " + outQSz );
		}
	}

	public void
	resetCompute()
	{
		pnlAD.outQ[ pnlAD.outQ.length - outQSz ].select( false );
		currOutQItem = pnlAD.outQ.length - 1;

		if ( pnlAD.dbg )
		{
			System.out.println(
				"Prefix.java:resetCompute(): " +
				"currOutQItem = " + currOutQItem +
				", outQSz = " + outQSz );
		}
	}

	/*
	   Input queue is read right to left
	   starting from the right-most item
	 */
	public AecDemoItem
	getInputItem()
	{
		AecDemoItem		item = null;

		if ( currInQItem < ( pnlAD.inQSz - 1 ) )
		{
			pnlAD.inQ[ currInQItem + 1 ].select( false );
		}

		if ( currInQItem >= 0 )
		{
			pnlAD.inQ[ currInQItem ].select( true );
			item = pnlAD.inQ[ currInQItem ].item;
			currInQItem--;
		}

		if ( pnlAD.dbg )
		{
			System.out.println(
				"Prefix.java:getInputItem(): " +
				"currInQItem = " + currInQItem );
		}

		return  item;
	}

	/*
	   Prefix queue is populated right to left
	   starting from its right-most extremity
	 */
	public void
	outQAdd( AecDemoItem item )
	{
		if ( currOutQItem < ( pnlAD.outQ.length - 1 ) )
		{
			pnlAD.outQ[ currOutQItem + 1 ].select( false );
		}

		if ( currOutQItem >= 0 )
		{
			pnlAD.outQ[ currOutQItem ].addItem( item );
			pnlAD.outQ[ currOutQItem ].select( true );
			currOutQItem--;
			outQSz++;
		}

		if ( pnlAD.dbg )
		{
			System.out.println(
				"Prefix.java:outQAdd(): " +
				"currOutQItem = " + currOutQItem +
				", outQSz = " + outQSz );
		}
	}

	public AecDemoItem
	getOutputItem()
	{
		AecDemoItem		item = null;

		if ( currOutQItem < ( pnlAD.outQ.length - 1 ) )
		{
			pnlAD.outQ[ currOutQItem + 1 ].select( false );
		}

		if ( currOutQItem > ( pnlAD.outQ.length - 1 - outQSz ) )
		{
			pnlAD.outQ[ currOutQItem ].select( true );
			item = pnlAD.outQ[ currOutQItem ].item;
			currOutQItem--;
		}

		if ( pnlAD.dbg )
		{
			System.out.println(
				"Prefix.java:getOutputItem(): " +
				"currOutQItem = " + currOutQItem );
		}

		return item;
	}


	private AecDemoPanel		pnlAD;

	private int			currInQItem;

	private int			outQSz;
	private int			currOutQItem;
}
