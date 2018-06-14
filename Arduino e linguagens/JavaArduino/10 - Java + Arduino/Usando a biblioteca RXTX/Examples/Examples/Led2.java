import port.Port;

public class Led2
{
	public static void main(String args[])
	{
		Port port = new Port(0x378);
		port.out(1);
	}

}