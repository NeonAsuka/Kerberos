
import java.io.*;
public class Authenticator_tgs implements Serializable{
	byte[] IDc,ADc;
	long TS3;
	
	public Authenticator_tgs(byte[] IDc,byte[] ADc, long TS3)
	{
		this.IDc = IDc;
		this.ADc = ADc;
		this.TS3 = TS3;
	}
	
	public long getTS3()
	{
		return this.TS3;
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
