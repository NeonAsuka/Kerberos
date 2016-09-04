
import java.io.*;
public class C_S implements Serializable{
	Ticket_s ts;
	Authenticator_s as;
	//Tickets || Authenticators
	public C_S(Ticket_s ts,Authenticator_s as)
	{
		this.as = as;
		this.ts = ts;
	}
	
	public Ticket_s getTicket_s(){
		return ts;
	}
	
	public Authenticator_s getAuthenticator_s(){
		return as;
	}
}
