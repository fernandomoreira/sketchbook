import port.Port;

public class Led3
{
	public static void main(String args[])
	{
		Port.loadLibrary();
		Port.out(0x378, 1);
	}

}