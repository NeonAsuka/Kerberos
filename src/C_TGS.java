
import java.io.*;
public class C_TGS implements Serializable{
	
	
	byte[] IDs;
	Ticket_tgs Tickettgs;
	Authenticator_tgs at;
	
	//IDs || Tickettgs || Authenticatortgs
	public C_TGS(byte[] IDs,Ticket_tgs tt,Authenticator_tgs at)
	{
		this.IDs = IDs;
		this.Tickettgs = tt;
		this.at = at;
	}
	
	public byte[] getIDs(){
		return IDs;
	}
	
	public Ticket_tgs getTicket_tgs(){
		return Tickettgs;
	}
	
	public Authenticator_tgs getAuthenticator_tgs(){
		return at;
	}
	
}