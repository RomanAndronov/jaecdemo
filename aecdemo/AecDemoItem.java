package aecdemo;

/*
   By Roman Andronov
 */

class AecDemoItem
{
	AecDemoItem()
	{
		erase();
	}

	void
	copyFrom( AecDemoItem src )
	{
		type = src.type;
		operand = src.operand;
		sym = src.sym;
		prec = src.prec;
		assoc = src.assoc;
		noo = src.noo;
	}

	void
	erase()
	{
		type = sym = '\0';
	}

	char		type;
	int		operand;
	char		sym;
	int		prec;
	char		assoc;
	int		noo;
}
