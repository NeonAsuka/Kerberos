
import java.io.*;
public class TGS_C implements Serializable{
	byte[] IDs;
	long TS4;
	Ticket_s ts;
	long Lifetime2;
	//TGS->C: Epub-c[IDs || TS4 || Tickets]
	public TGS_C(byte[] IDs, long TS4,Ticket_s ts, long Lifetime2)
	{
		this.IDs = IDs;
		this.ts = ts;
		this.TS4 = TS4;
		this.Lifetime2 = Lifetime2;
	}
}