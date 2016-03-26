package aecdemo;

/*
   By Roman Andronov
 */

interface AeNotation
{
	public char		openingOOEM();
	public char		closingOOEM();

	public void		resetConvert();
	public void		resetCompute();

	public AecDemoItem	getInputItem();
	public AecDemoItem	getOutputItem();
	public void		outQAdd( AecDemoItem item );
}
