

import java.io.*;
public class Authenticator_s implements Serializable{
	byte[] IDc,ADc;
	long TS5;
	
	public Authenticator_s(byte[] IDc,byte[] ADc,long TS5)
	{
		this.IDc = IDc;
		this.ADc = ADc;
		this.TS5 = TS5;
	}
	
	public long getTS5()
	{
		return this.TS5;
	}
	public byte[] getIDc()
	{
		return this.IDc;
	}
	public byte[] getADc()
	{
		return this.ADc;
	}
}

