package port;

public class Port
{
	private int port;

	public Port(int port)
	{
		//System.setProperty("java.library.path", "Port.dll");
		System.loadLibrary("Port");
		this.port = port;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public static void loadLibraryFromFolder(String folder)
	{
		System.loadLibrary(folder+"/Port");
	}

	public static void loadLibrary()
	{
		System.loadLibrary("Port");
	}

	public void out(int n)
	{
		out(this.port, n);
	}

	public int in()
	{
		return in(this.port);
	}

	public static native void out(int port, int n);
	public static native int in(int port);
}