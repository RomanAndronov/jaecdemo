package aecdemo;

/*
   By Roman Andronov
 */

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.RootPaneContainer;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

class AecDemoUi
{
	AecDemoUi( AecDemoPanel pnlad )
	{
		pnlAD = pnlad;
	}

	void
	init( RootPaneContainer rpc )
	{
		GridBagConstraints	gbc = new GridBagConstraints();

		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = INSETS;

		rpc.getContentPane().setLayout( new GridBagLayout() );

		pnlAD.setLayout( new GridBagLayout() );
		pnlAD.setBorder( BorderFactory.createLineBorder( CLRGRAY ) );
		rpc.getContentPane().add( pnlAD, gbc );

		pnlAD.pnlInfixExpr = addPnl( "Infix Expression:", 0 );
		pnlAD.inQ = initItems( pnlAD.pnlInfixExpr );
		initInfixPnl();

		pnlAD.pnlOutQ = addPnl( "Output Queue:", 1 );
		pnlAD.outQ = initItems( pnlAD.pnlOutQ );

		pnlAD.pnlStack = addPnl( "Stack:", 2 );
		pnlAD.stack = initItems( pnlAD.pnlStack );
		initStackPnl();

		addInputPnl();
		addConvertPnl();
		addComputePnl();

		pnlAD.ontp = 0;
		pnlAD.ons = new AeNotation[] { new Prefix( pnlAD ),
						new Postfix( pnlAD ) };

		pnlSetEnabled( pnlAD.pnlConvert, false );
		pnlSetEnabled( pnlAD.pnlCompute, false );
	}

	static void
	pnlSetEnabled( JPanel pnl, boolean e )
	{
		for ( Component component : pnl.getComponents() )
		{
			component.setEnabled( e );
		}
		pnl.setEnabled( e );
	}

	static void
	rmAll( AecDemoLabel[] arr )
	{
		for ( int i = 0; i < arr.length; i++ )
		{
			arr[ i ].rmItem();
			arr[ i ].select( false );
		}
	}

	void
	notationSetEnabled( boolean e )
	{
		pnlAD.rbPrefix.setEnabled( e );
		pnlAD.rbPostfix.setEnabled( e );
	}

	void
	clrStackCntrls()
	{
		pnlAD.lblLeft.setText( "" );
		pnlAD.lblOp.setText( "" );
		pnlAD.lblOp.select( false );
		pnlAD.lblRight.setText( "" );
		pnlAD.lblOpResult.setText( "" );
	}

	private void
	mkLbl( JLabel lbl, Dimension d )
	{
		lbl.setMinimumSize( d );
		lbl.setPreferredSize( d );
		lbl.setMaximumSize( d );
		lbl.setHorizontalAlignment( SwingConstants.CENTER );
		lbl.setBorder( BorderFactory.createLineBorder( CLRGRAY ) );
	}

	private JPanel
	addPnl( String title, int row )
	{
		JPanel			jpnl = null;
		GridBagConstraints	gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 3;
		gbc.insets = INSETS;
		gbc.weightx = gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;

		jpnl = new JPanel();
		jpnl.setLayout( new GridBagLayout() );
		jpnl.setBorder( BorderFactory.createTitledBorder( title ) );
		pnlAD.add( jpnl, gbc );

		return jpnl;
	}

	private AecDemoLabel[]
	initItems( JPanel cntnr )
	{
		AecDemoLabel[]		arr = null;
		GridBagConstraints	gbc = new GridBagConstraints();

		gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;

		arr = new AecDemoLabel[ ITEMCNT ];
		for ( int i = 0; i < arr.length; i++ )
		{
			arr[ i ] = new AecDemoLabel();
			mkLbl( arr[ i ], ITEMSZ );
			gbc.gridx = i;
			if ( i == 0 )
			{
				gbc.insets = INSETSTLB;
			}
			else if ( i == ( arr.length - 1 ) )
			{
				gbc.insets = INSETSTBR;
			}
			else
			{
				gbc.insets = INSETSTB;
			}
			cntnr.add( arr[ i ], gbc );
		}

		return arr;
	}

	private void
	initInfixPnl()
	{
		GridBagConstraints	gbc = new GridBagConstraints();

		gbc.gridy = 1;

		gbc.insets = INSETSTB;
		gbc.gridx = 10;
		pnlAD.lblEq = new JLabel( "=" );
		pnlAD.lblEq.setHorizontalAlignment( SwingConstants.CENTER );
		pnlAD.pnlInfixExpr.add( pnlAD.lblEq, gbc );

		gbc.gridx = 11;
		gbc.gridwidth = 6;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.anchor = GridBagConstraints.NORTH;
		pnlAD.lblResult = new JLabel();
		mkLbl( pnlAD.lblResult, ITEMSZLONG );
		pnlAD.pnlInfixExpr.add( pnlAD.lblResult, gbc );
	}

	private void
	initStackPnl()
	{
		GridBagConstraints	gbc = new GridBagConstraints();

		gbc.gridy = 1;
		gbc.insets = INSETSTB;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.anchor = GridBagConstraints.NORTH;

		gbc.gridx = 5;
		gbc.gridwidth = 6;
		pnlAD.lblLeft = new JLabel();
		mkLbl( pnlAD.lblLeft, ITEMSZLONG );
		pnlAD.pnlStack.add( pnlAD.lblLeft, gbc );

		gbc.gridx = 11;
		gbc.gridwidth = 1;
		pnlAD.lblOp = new AecDemoLabel();
		mkLbl( pnlAD.lblOp, ITEMSZ );
		pnlAD.pnlStack.add( pnlAD.lblOp, gbc );

		gbc.gridx = 12;
		gbc.gridwidth = 6;
		pnlAD.lblRight = new JLabel();
		mkLbl( pnlAD.lblRight, ITEMSZLONG );
		pnlAD.pnlStack.add( pnlAD.lblRight, gbc );

		gbc.gridx = 18;
		gbc.gridwidth = 1;
		pnlAD.lblOpEq = new JLabel( "=" );
		pnlAD.lblOpEq.setHorizontalAlignment( SwingConstants.CENTER );
		pnlAD.pnlStack.add( pnlAD.lblOpEq, gbc );

		gbc.gridx = 19;
		gbc.gridwidth = 6;
		pnlAD.lblOpResult = new JLabel();
		mkLbl( pnlAD.lblOpResult, ITEMSZLONG );
		pnlAD.pnlStack.add( pnlAD.lblOpResult, gbc );
	}

	private void
	addInputPnl()
	{
		AecDemoItem		src = new AecDemoItem();
		GridBagConstraints	gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.insets = INSETS;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.NORTH;

		pnlAD.pnlInput = new JPanel();
		pnlAD.pnlInput.setLayout( new GridBagLayout() );
		pnlAD.pnlInput.setBorder( BorderFactory.createTitledBorder( "Input:" ) );
		pnlAD.add( pnlAD.pnlInput, gbc );

		/*
		   Digits, first row
		 */
		src.type = 'a';

		gbc.gridy = 0;
		gbc.insets = INSETSTL;
		src.operand = 0;
		pnlAD.btn0 = new AecDemoButton( src );
		pnlAD.btn0.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btn0, gbc );

		gbc.gridx = 1;
		gbc.insets = INSETST;
		src.operand = 1;
		pnlAD.btn1 = new AecDemoButton( src );
		pnlAD.btn1.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btn1, gbc );

		gbc.gridx = 2;
		src.operand = 2;
		pnlAD.btn2 = new AecDemoButton( src );
		pnlAD.btn2.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btn2, gbc );

		gbc.gridx = 3;
		src.operand = 3;
		pnlAD.btn3 = new AecDemoButton( src );
		pnlAD.btn3.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btn3, gbc );

		gbc.gridx = 4;
		gbc.insets = INSETSTR;
		src.operand = 4;
		pnlAD.btn4 = new AecDemoButton( src );
		pnlAD.btn4.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btn4, gbc );

		/*
		   Digits, second row
		 */
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.insets = INSETSLB;
		src.operand = 5;
		pnlAD.btn5 = new AecDemoButton( src );
		pnlAD.btn5.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btn5, gbc );

		gbc.gridx = 1;
		gbc.insets = INSETSB;
		src.operand = 6;
		pnlAD.btn6 = new AecDemoButton( src );
		pnlAD.btn6.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btn6, gbc );

		gbc.gridx = 2;
		src.operand = 7;
		pnlAD.btn7 = new AecDemoButton( src );
		pnlAD.btn7.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btn7, gbc );

		gbc.gridx = 3;
		src.operand = 8;
		pnlAD.btn8 = new AecDemoButton( src );
		pnlAD.btn8.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btn8, gbc );

		gbc.gridx = 4;
		gbc.insets = INSETSBR;
		src.operand = 9;
		pnlAD.btn9 = new AecDemoButton( src );
		pnlAD.btn9.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btn9, gbc );

		/*
		   Operators, first row
		 */
		src.type = 'o';
		src.noo = 2;
		src.assoc = 'l';
		src.prec = 98;

		gbc.gridy = 0;

		gbc.gridx = 5;
		gbc.insets = INSETSTL;
		src.sym = '+';
		pnlAD.btnAdd = new AecDemoButton( src );
		pnlAD.btnAdd.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btnAdd, gbc );

		gbc.gridx = 6;
		gbc.insets = INSETST;
		src.sym = '-';
		pnlAD.btnSub = new AecDemoButton( src );
		pnlAD.btnSub.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btnSub, gbc );

		src.prec = 99;

		gbc.gridx = 7;
		src.sym = '*';
		pnlAD.btnMul = new AecDemoButton( src );
		pnlAD.btnMul.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btnMul, gbc );

		gbc.gridx = 8;
		src.sym = '/';
		pnlAD.btnDiv = new AecDemoButton( src );
		pnlAD.btnDiv.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btnDiv, gbc );

		gbc.gridx = 9;
		gbc.insets = INSETSTR;
		src.sym = '%';
		pnlAD.btnMod = new AecDemoButton( src );
		pnlAD.btnMod.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btnMod, gbc );

		/*
		   Operators, second row
		 */
		src.type = 'p';

		gbc.gridy = 1;
		gbc.gridx = 5;
		gbc.insets = INSETSLB;
		src.sym = '(';
		pnlAD.btnParenL = new AecDemoButton( src );
		pnlAD.btnParenL.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btnParenL, gbc );

		gbc.gridx = 6;
		gbc.insets = INSETSB;
		src.sym = ')';
		pnlAD.btnParenR = new AecDemoButton( src );
		pnlAD.btnParenR.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.btnParenR, gbc );

		gbc.gridx = 7;
		pnlAD.jbBkSp = new JButton( "b" );
		pnlAD.jbBkSp.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.jbBkSp, gbc );

		gbc.gridx = 8;
		pnlAD.jbClr = new JButton( "C" );
		pnlAD.jbClr.addActionListener( pnlAD );
		pnlAD.pnlInput.add( pnlAD.jbClr, gbc );
	}

	private void
	addConvertPnl()
	{
		GridBagConstraints	gbc = new GridBagConstraints();

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.insets = INSETS;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.NORTH;

		pnlAD.pnlConvert = new JPanel();
		pnlAD.pnlConvert.setLayout( new GridBagLayout() );
		pnlAD.pnlConvert.setBorder( BorderFactory.createTitledBorder( "Convert To:" ) );
		pnlAD.add( pnlAD.pnlConvert, gbc );

		pnlAD.bgConvert = new ButtonGroup();
		pnlAD.rbPrefix = new JRadioButton( "prefix" );
		pnlAD.rbPrefix.setSelected( true );
		pnlAD.rbPostfix = new JRadioButton( "postfix" );
		pnlAD.bgConvert.add( pnlAD.rbPrefix );
		pnlAD.bgConvert.add( pnlAD.rbPostfix );

		gbc.gridx = gbc.gridy = 0;
		pnlAD.pnlConvert.add( pnlAD.rbPrefix, gbc );

		gbc.gridx = 1;
		pnlAD.pnlConvert.add( pnlAD.rbPostfix, gbc );

		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		pnlAD.jbConvert = new JButton( "Next" );
		pnlAD.jbConvert.addActionListener( pnlAD );
		pnlAD.pnlConvert.add( pnlAD.jbConvert, gbc );
	}

	private void
	addComputePnl()
	{
		GridBagConstraints	gbc = new GridBagConstraints();

		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.insets = INSETS;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.NORTH;

		pnlAD.pnlCompute = new JPanel();
		pnlAD.pnlCompute.setLayout( new GridBagLayout() );
		pnlAD.pnlCompute.setBorder( BorderFactory.createTitledBorder( "Compute:" ) );
		pnlAD.add( pnlAD.pnlCompute, gbc );

		gbc.gridx = gbc.gridy = 0;
		pnlAD.jbCompute = new JButton( "Next" );
		pnlAD.jbCompute.addActionListener( pnlAD );
		pnlAD.pnlCompute.add( pnlAD.jbCompute, gbc );
	}


	AecDemoPanel				pnlAD;

	static final Insets			INSETS = new Insets( 3, 3, 3, 3 );
	static final Insets			INSETST = new Insets( 3, 0, 0, 0 ); // Top
	static final Insets			INSETSTB = new Insets( 3, 0, 3, 0 ); // Top, Bottom
	static final Insets			INSETSTL = new Insets( 3, 3, 0, 0 ); // Top, Left
	static final Insets			INSETSTR = new Insets( 3, 0, 0, 3 ); // Top, Right
	static final Insets			INSETSTLB = new Insets( 3, 3, 3, 0 ); // Top, Left, Bottom
	static final Insets			INSETSTBR = new Insets( 3, 0, 3, 3 ); // Top, Bottom, Right
	static final Insets			INSETSB = new Insets( 0, 0, 3, 0 ); // Bottom
	static final Insets			INSETSLB = new Insets( 0, 3, 3, 0 ); // Left, Bottom
	static final Insets			INSETSBR = new Insets( 0, 0, 3, 3 ); // Bottom, Right

	static final int			ITEMCNT = 29;
	static final int			ITEMHEIGHT = 30;
	static final int			ITEMWIDTH = ITEMHEIGHT;

	static final Color			CLRGRAY = Color.GRAY;
	static final Color			CLRLGRAY = Color.LIGHT_GRAY;

	static final Dimension			ITEMSZ = new Dimension( ITEMWIDTH, ITEMHEIGHT );
	static final Dimension			ITEMSZLONG = new Dimension( ITEMWIDTH * 6, ITEMHEIGHT );
}
