
import java.io.*;
public class AS_C implements Serializable{

	byte[] IDtgs;
	Ticket_tgs Tickettgs;
	long TS2,Lifetime1;
	public AS_C(byte[] IDtgs,long TS2,long Lifetime1,Ticket_tgs Tickettgs )
	{
		this.IDtgs = IDtgs;
		this.Tickettgs = Tickettgs;
		this.TS2 = TS2;
		this.Lifetime1 = Lifetime1;
	}
}
