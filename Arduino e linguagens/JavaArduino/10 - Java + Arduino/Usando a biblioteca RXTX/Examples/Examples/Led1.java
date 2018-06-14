import static port.Port.*;

public class Led1
{
	public static void main(String args[])
	{
		loadLibrary();
		out(0x378, 1);
	}

}