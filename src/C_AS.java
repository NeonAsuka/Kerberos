
import java.io.*;
public class C_AS implements Serializable{
	String IDc;
	long TS1;
	public C_AS(String IDc, long TS1)
	{
		this.IDc = IDc;
		this.TS1 = TS1;
	}
	
	public String getIDc(){
		return IDc;
	}
	
	
}
