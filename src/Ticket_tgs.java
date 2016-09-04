
import java.io.*;
import java.security.Key;

public class Ticket_tgs implements Serializable{
	
	byte[] IDc, ADc,IDtgs;
	long TS2,Lifetime1;
	Key key;
	public Ticket_tgs(Key key,byte[] IDc,byte[] ADc,byte[] IDtgs,long TS2,long Lifetime1){
		this.key=key;
		this.IDc = IDc;
		this.ADc = ADc;
		this.IDtgs = IDtgs;
		this.TS2 = TS2;
		this.Lifetime1 = Lifetime1;
	}
	
	public byte[] getIDc()
	{
		return this.IDc;
	}
	
	public byte[] getADc()
	{
		return this.ADc;
	}
	
	public byte[] getIDtgs()
	{
		return this.IDtgs;
	}
	
	public long getTS2()
	{
		return this.TS2;
	}
	
	public long getLifetime1()
	{
		return this.Lifetime1;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}
}
