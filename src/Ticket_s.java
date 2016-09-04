
import java.io.*;
import java.security.Key;
public class Ticket_s implements Serializable{
	byte[] IDc, ADc,IDs;
	long TS4,Lifetime2;
	Key key;
	
	public Ticket_s(Key key,byte[] IDc, byte[] ADc, byte[] IDs, long TS4, long Lifetime2)
	{	
		this.key= key;
		this.IDc = IDc;
		this.ADc = ADc;
		this.IDs = IDs;
		this.TS4 = TS4;
		this.Lifetime2 = Lifetime2;
	}
	
	public byte[] getIDc()
	{
		return this.IDc;
	}
	
	public byte[] getADc()
	{
		return this.ADc;
	}
	
	public byte[] getIDs()
	{
		return this.IDs;
	}
	
	public long getTS4()
	{
		return this.TS4;
	}
	
	public long getLifetime2()
	{
		return this.Lifetime2;
	}
	
}
