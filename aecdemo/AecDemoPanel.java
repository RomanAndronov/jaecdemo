package aecdemo;

/*
   By Roman Andronov
 */

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.RootPaneContainer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

class AecDemoPanel
	extends JPanel
	implements ActionListener
{
	AecDemoPanel( RootPaneContainer rpc, boolean dbg )
	{
		super();
		this.dbg = dbg;
		init( rpc );
	}

	public void
	actionPerformed( ActionEvent ae )
	{
		JButton	btn = ( JButton )ae.getSource();

		if ( btn instanceof AecDemoButton )
		{
			AecDemoButton		adb = ( AecDemoButton )btn;

			btnAct( adb );
		}
		else if ( btn == jbBkSp )
		{
			backSpace();
		}
		else if ( btn == jbClr )
		{
			clearState();
		}
		else if ( btn == jbConvert )
		{
			convert();
		}
		else if ( btn == jbCompute )
		{
			compute();
		}
	}

	private void
	init( RootPaneContainer rpc )
	{
		ui = new AecDemoUi( this );
		ui.init( rpc );
		state = STATE_INIT;
	}

	private void
	btnAct( AecDemoButton btn )
	{
		if ( inQSz >= inQ.length )
		{
			return;
		}

		inQ[ inQSz ].addItem( btn.item );

		if ( inQSz == 0 )
		{
			AecDemoUi.pnlSetEnabled( pnlConvert, true );
		}

		inQSz++;

		if ( dbg )
		{
			System.out.println(
				"AecDemoPanel.java:btnAct(): " +
				"inQSz = " + inQSz );
		}
	}

	private void
	backSpace()
	{
		if ( inQSz <= 0 )
		{
			return;
		}

		inQ[ inQSz - 1 ].rmItem();

		inQSz--;

		if ( dbg )
		{
			System.out.println(
				"AecDemoPanel.java:backSpace(): " +
				"inQSz = " + inQSz );
		}

		if ( inQSz == 0 )
		{
			AecDemoUi.pnlSetEnabled( pnlConvert, false );
		}
	}

	private void
	clearState()
	{
		if ( inQSz > 0 )
		{
			AecDemoUi.rmAll( inQ );
			inQSz = 0;
			AecDemoUi.pnlSetEnabled( pnlConvert, false );
		}

		reset();

		state = STATE_INIT;
	}

	private void
	convert()
	{
		if ( state == STATE_INIT )
		{
			doInit();
		}

		if ( state == STATE_READ_INPUT )
		{
			doReadInput();
		}

		if ( state == STATE_ADD_CLOSING_OOEM )
		{
			doAddClosingOOEM();
		}

		if ( state == STATE_ADD_OPERATOR )
		{
			doAddOperator();
		}

		if ( state == STATE_EMPTY_STACK )
		{
			doEmptyStack();
		}
	}

	private void
	compute()
	{
		if ( state == STATE_COMPUTE )
		{
			doCompute();
		}

		if ( state == STATE_EXE_OPERATOR )
		{
			doExeOperator();
		}
	}

	private void
	reset()
	{
		AecDemoUi.rmAll( outQ );
		if ( stackSz > 0 )
		{
			AecDemoUi.rmAll( stack );
			stackSz = 0;
		}
		lblResult.setText( "" );
		ui.clrStackCntrls();
	}

	private void
	doInit()
	{
		reset();

		AecDemoUi.pnlSetEnabled( pnlInput, false );
		ui.notationSetEnabled( false );
		ontp = rbPrefix.isSelected() == true ? PREFIX : POSTFIX;
		ons[ ontp ].resetConvert();

		state = STATE_READ_INPUT;
	}

	private void
	doReadInput()
	{
		inputItem = ons[ ontp ].getInputItem();
		if ( inputItem != null )
		{
			if ( inputItem.type == 'a' )
			{
				ons[ ontp ].outQAdd( inputItem );
			}
			else if ( inputItem.sym == ons[ ontp ].openingOOEM() )
			{
				push( inputItem );
			}
			else if ( inputItem.sym == ons[ ontp ].closingOOEM() )
			{
				balancedOOEMs = false;
				state = STATE_ADD_CLOSING_OOEM;
			}
			else if ( inputItem.type == 'o' )
			{
				state = STATE_ADD_OPERATOR;
			}
		}
		else
		{
			state = STATE_EMPTY_STACK;
		}
	}

	private void
	doAddClosingOOEM()
	{
		AecDemoItem		op;

		if ( stackSz > 0 )
		{
			op = pop();
			if ( op.sym == ons[ ontp ].openingOOEM() )
			{
				balancedOOEMs = true;
				state = STATE_READ_INPUT;
			}
			else
			{
				ons[ ontp ].outQAdd( op );
			}
		}
		else
		{
			error( ERR_UNBALANCED_OOEMS );
		}
	}

	private void
	doAddOperator()
	{
		AecDemoItem		op;

		if ( stackSz > 0 )
		{
			op = stack[ stackSz - 1 ].item;
			if ( op.type == 'o' )
			{
				if ( inputItem.prec < op.prec )
				{
					op = pop();
					ons[ ontp ].outQAdd( op );
					return;
				}
				else if ( inputItem.prec == op.prec )
				{
					if ( inputItem.assoc != op.assoc )
					{
						error( ERR_ASSOC );
						return;
					}
					else if ( inputItem.assoc == 'l' )
					{
						if ( ontp == POSTFIX )
						{
							op = pop();
							ons[ ontp ].outQAdd( op );
							return;
						}
					}
					else // inputItem.assoc == 'r'
					{
						if ( ontp == PREFIX )
						{
							op = pop();
							ons[ ontp ].outQAdd( op );
							return;
						}
					}
				}
			}
		}

		push( inputItem );
		state = STATE_READ_INPUT;
	}

	private void
	doEmptyStack()
	{
		AecDemoItem		op;

		if ( stackSz > 0 )
		{
			op = pop();
			if ( op.type == 'p' )
			{
				error( ERR_INVALID_AE );
			}
			else
			{
				ons[ ontp ].outQAdd( op );
			}
		}
		else
		{
			state = STATE_COMPUTE;
			ons[ ontp ].resetCompute();
			pnlConvert.setEnabled( false );
			jbConvert.setEnabled( false );
			AecDemoUi.pnlSetEnabled( pnlCompute, true );
			jbCompute.requestFocusInWindow();
		}
	}

	private void
	doCompute()
	{
		outputItem = ons[ ontp ].getOutputItem();
		if ( outputItem != null )
		{
			if ( outputItem.type == 'a' )
			{
				push( outputItem );
			}
			else
			{
				noo = false;
				state = STATE_EXE_OPERATOR;
			}
		}
		else
		{
			if ( stackSz != 1 )
			{
				error( ERR_INVALID_AE );
			}
			else
			{
				AecDemoItem	r;

				r = pop();
				lblResult.setText( "" + r.operand );
				AecDemoUi.pnlSetEnabled( pnlCompute, false );
				AecDemoUi.pnlSetEnabled( pnlConvert, true );
				AecDemoUi.pnlSetEnabled( pnlInput, true );

				state = STATE_INIT;
			}
		}
	}

	private void
	doExeOperator()
	{
		AecDemoItem		arg;

		if ( !noo )
		{
			noo = true;
			if ( stackSz < outputItem.noo )
			{
				error( ERR_NOO );
			}
		}

		if ( lblOp.getText().length() == 0 )
		{
			lblOp.setText( "" + outputItem.sym );
			lblOp.select( true );
		}
		else if ( lblLeft.getText().length() == 0 ||
			lblRight.getText().length() == 0 )
		{
			if ( ontp == PREFIX )
			{
				if ( lblLeft.getText().length() == 0 )
				{
					arg = pop();
					_left.copyFrom( arg );
					lblLeft.setText( "" + _left.operand );
				}
				else if ( lblRight.getText().length() == 0 )
				{
					arg = pop();
					_right.copyFrom( arg );
					lblRight.setText( "" + _right.operand );
				}
			}
			else if ( ontp == POSTFIX )
			{
				if ( lblRight.getText().length() == 0 )
				{
					arg = pop();
					_right.copyFrom( arg );
					lblRight.setText( "" + _right.operand );
				}
				else if ( lblLeft.getText().length() == 0 )
				{
					arg = pop();
					_left.copyFrom( arg );
					lblLeft.setText( "" + _left.operand );
				}
			}
		}
		else if ( lblOpResult.getText().length() == 0 )
		{
			boolean		err = false;
			char		op = outputItem.sym;

			if ( op == '+' )
			{
				result = _left.operand + _right.operand;
			}
			else if ( op == '-' )
			{
				result = _left.operand - _right.operand;
			}
			else if ( op == '*' )
			{
				result = _left.operand * _right.operand;
			}
			else if ( op == '/' )
			{
				if ( _right.operand == 0 )
				{
					err = true;
					error( ERR_DIVISION_BY_ZERO );
				}
				else
				{
					result = _left.operand / _right.operand;
				}
			}
			else if ( op == '%' )
			{
				if ( _right.operand == 0 )
				{
					err = true;
					error( ERR_DIVISION_BY_ZERO );
				}
				else
				{
					result = _left.operand % _right.operand;
				}
			}

			if ( !err )
			{
				lblOpResult.setText( "" + result );
			}
		}
		else
		{
			_item.type = 'a';
			_item.operand = result;
			push( _item );
			ui.clrStackCntrls();
			state = STATE_COMPUTE;
		}
	}

	private boolean
	push( AecDemoItem item )
	{
		boolean		rv = false;

		if ( stackSz > 0 )
		{
			stack[ stackSz - 1 ].select( false );
		}

		if ( stackSz < stack.length )
		{
			rv = true;
			stack[ stackSz ].addItem( item );
			stack[ stackSz ].select( true );
			stackSz++;
		}

		if ( dbg )
		{
			System.out.println(
				"AecDemoPanel.java:push(): " +
				"stackSz = " + stackSz +
				", rv = " + rv );
		}

		return rv;
	}

	private AecDemoItem
	pop()
	{
		AecDemoItem		item = null;

		if ( stackSz <= 0 )
		{
			return null;
		}

		stack[ stackSz - 1 ].select( false );
		item = stack[ stackSz - 1 ].item;
		_item.copyFrom( item );
		stack[ stackSz - 1 ].rmItem();
		stackSz--;

		if ( stackSz > 0 )
		{
			stack[ stackSz - 1 ].select( true );
		}

		if ( dbg )
		{
			System.out.println(
				"AecDemoPanel.java:pop(): " +
				"stackSz = " + stackSz );
		}

		return _item;
	}

	private void
	error( String es )
	{
		lblResult.setText( es );
		AecDemoUi.pnlSetEnabled( pnlCompute, false );
		AecDemoUi.pnlSetEnabled( pnlConvert, true );
		AecDemoUi.pnlSetEnabled( pnlInput, true );

		state = STATE_INIT;
	}


	/*
	   Algorithmic state
	 */
	int				inQSz = 0;
	int				stackSz = 0;
	int				result = 0;
	boolean				noo = false;
	boolean				balancedOOEMs = false;
	AecDemoItem			inputItem = null;
	AecDemoItem			outputItem = null;
	AecDemoItem			_item = new AecDemoItem();
	AecDemoItem			_left = new AecDemoItem();
	AecDemoItem			_right = new AecDemoItem();

	boolean				dbg = false;

	int				ontp = PREFIX;
	AeNotation[]			ons = null;

	private static final int	PREFIX = 0;
	private static final int	POSTFIX = 1;

	private static final int	STATE_INIT = 0;
	private static final int	STATE_READ_INPUT = 1;
	private static final int	STATE_ADD_CLOSING_OOEM = 2;
	private static final int	STATE_ADD_OPERATOR = 3;
	private static final int	STATE_EMPTY_STACK = 4;
	private static final int	STATE_COMPUTE = 5;
	private static final int	STATE_EXE_OPERATOR = 6;

	private int			state = STATE_INIT;

	private static final String	ERR_UNBALANCED_OOEMS = "unbalanced OOEMs";
	private static final String	ERR_INVALID_AE = "invalid input expression";
	private static final String	ERR_ASSOC = "associativity conflict";
	private static final String	ERR_NOO = "not enough operands";
	private static final String	ERR_DIVISION_BY_ZERO = "division by zero";


	/*
	   Gui elements
	 */
	AecDemoUi			ui = null;

	AecDemoLabel[]			inQ = null;
	JPanel				pnlInfixExpr = null;
	JLabel				lblEq = null;
	JLabel				lblResult = null;

	AecDemoLabel[]			outQ = null;
	JPanel				pnlOutQ = null;

	AecDemoLabel[]			stack = null;
	JPanel				pnlStack = null;
	JLabel				lblLeft = null;
	AecDemoLabel			lblOp = null;
	JLabel				lblRight = null;
	JLabel				lblOpEq = null;
	JLabel				lblOpResult = null;

	JPanel				pnlInput = null;
	AecDemoButton			btn0 = null;
	AecDemoButton			btn1 = null;
	AecDemoButton			btn2 = null;
	AecDemoButton			btn3 = null;
	AecDemoButton			btn4 = null;
	AecDemoButton			btn5 = null;
	AecDemoButton			btn6 = null;
	AecDemoButton			btn7 = null;
	AecDemoButton			btn8 = null;
	AecDemoButton			btn9 = null;
	AecDemoButton			btnAdd = null;
	AecDemoButton			btnSub = null;
	AecDemoButton			btnMul = null;
	AecDemoButton			btnDiv = null;
	AecDemoButton			btnMod = null;
	AecDemoButton			btnParenR = null;
	AecDemoButton			btnParenL = null;

	JButton				jbBkSp = null;
	JButton				jbClr = null;

	JPanel				pnlConvert = null;
	ButtonGroup			bgConvert = null;
	JRadioButton			rbPrefix = null;
	JRadioButton			rbPostfix = null;
	JButton				jbConvert = null;

	JPanel				pnlCompute = null;
	JButton				jbCompute = null;
}
