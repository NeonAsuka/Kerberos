import java.io.*;
import java.net.*;
import java.security.*;


public class TGSserver {
	private Socket clientSocket;
	private ServerSocket serverSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int port;
	private Utils utils =new Utils();
	public TGSserver(int port) {
		this.port = port;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("TGS Server is working");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void start() {
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				in = new ObjectInputStream(clientSocket.getInputStream());
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				Object inObject = in.readObject();	
				/**
				 * (3)C->TGS: IDs || Tickettgs || Authenticatortgs
				 * (4)TGS->C: Epub-c[IDs || TS4 || Tickets] 
				 * Tickettgs = Epub-tgs[Kpub-c || IDc || ADc || IDtgs || TS2 || Lifetime1] £¬
				 * Tickets = Epub-s[Kpub-c || IDc || ADc || IDs || TS4 || Lifetime2] £¬
				 * Authenticatortgs = Epri-c[IDc || ADc || TS3]
				 */
				if(inObject instanceof C_TGS){
					C_TGS c_tgs = (C_TGS)inObject;
					byte[] IDs_byte = c_tgs.getIDs();
					Ticket_tgs ticket_tgs = c_tgs.getTicket_tgs();
					if(!userValidate(ticket_tgs)){
						out.writeObject("false");
						System.out.println("ticket_tgs error");
					}else{
						System.out.println("TGS received message from client successfully");
						Authenticator_tgs authenticator = c_tgs.getAuthenticator_tgs();
						long TS2 = ticket_tgs.getTS2();
						long TS3 = authenticator.getTS3();
						long lifeTime = ticket_tgs.getLifetime1();
						if(!timeValidation(lifeTime, TS2, TS3)){
							out.writeObject("false");
							System.out.println("TGS time error");
							
						}else if(!IDc_ADcValidate(ticket_tgs, authenticator)){
							out.writeObject("false");
							System.out.println("TGS IDc,ADc error");
						
						}else if(!new String(IDs_byte).equals("A")){
							out.writeObject("false");
							System.out.println("TGS IDs error");
						}
						else{
							long TS4 = utils.getCurrentTime();
							//·â×°ticket_s							
							Ticket_s ticket_s = generateServerTicket(IDs_byte, authenticator, TS4);
							//·â×°TGS->C
							TGS_C tgs_c = generateTGS_C(IDs_byte, TS4, ticket_s);
							out.writeObject(tgs_c);
							System.out.println("TGS verfication pass");
						}
					}
				}else{
					out.writeObject("false");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	private boolean userValidate(Ticket_tgs ticket_tgs){
		byte[] IDtgs_Cipher_byte = ticket_tgs.getIDtgs();
		byte[] IDtgs_Plain_byte = utils.encrypt_decrypt_tgs(IDtgs_Cipher_byte, false);
		String IDtgs_String = null;
		try{
			IDtgs_String = new String(IDtgs_Plain_byte, Utils.UTF);
		}catch(Exception e){
			e.printStackTrace();
		}
		return (IDtgs_String.equals(Utils.ID_TGS));
	}
	
	public boolean timeValidation(long lifeTime, long startTime, long endTime){
		long validationLifeTime = startTime + lifeTime;
		return (validationLifeTime > endTime);
	}


	private boolean IDc_ADcValidate(Ticket_tgs ticket_tgs, Authenticator_tgs authenticator_tgs){
	try{
		RSACryptography rsa = new RSACryptography();
		Object keyObject = utils.getKey(Utils.KEY_PATH, "pb"+Utils.CLIENT_KEY );
		Key key = (Key)keyObject;
		byte[] IDc_Cipher_authenticator_byte = authenticator_tgs.getIDc();
		byte[] ADc_Cipher_authenticator_byte = authenticator_tgs.getADc();
		byte[] IDc_Plain_authenticator_byte = rsa.encrypt_decrypt(IDc_Cipher_authenticator_byte, key, false);
		byte[] ADc_Plain_authenticator_byte = rsa.encrypt_decrypt(ADc_Cipher_authenticator_byte,key, false);
		String IDc_Plain_authenticator_String = new String(IDc_Plain_authenticator_byte, Utils.UTF);
		String ADc_Plain_authenticator_String = new String(ADc_Plain_authenticator_byte, Utils.UTF);
		
		byte[] IDc_Cipher_ticket_byte = ticket_tgs.getIDc();
		byte[] ADc_Cipher_ticket_byte = ticket_tgs.getADc();
		byte[] IDc_Plain_ticket_byte = utils.encrypt_decrypt_tgs(IDc_Cipher_ticket_byte, false);
		byte[] ADc_Plain_ticket_byte = utils.encrypt_decrypt_tgs(ADc_Cipher_ticket_byte, false);
		String IDc_Plain_ticket_String = new String(IDc_Plain_ticket_byte, Utils.UTF);
		String ADc_Plain_ticket_String = new String(ADc_Plain_ticket_byte, Utils.UTF);
		return (IDc_Plain_authenticator_String.equals(IDc_Plain_ticket_String) && ADc_Plain_authenticator_String.equals(ADc_Plain_ticket_String));
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	private TGS_C generateTGS_C(byte[] IDs_byte, long TS4, Ticket_s ticket_s) throws UnsupportedEncodingException{
		
		RSACryptography rsa = new RSACryptography();
		Object keyObject = utils.getKey(Utils.KEY_PATH, "pb"+Utils.CLIENT_KEY );
		Key key = (Key)keyObject;
		long lifeTime = Utils.LIFETIME;
		byte[] IDs_Cipher_byte = rsa.encrypt_decrypt(IDs_byte, key, true);
		TGS_C tgs2c = new TGS_C(IDs_Cipher_byte, TS4, ticket_s, lifeTime);
		return tgs2c;
	}

	private Ticket_s generateServerTicket(byte[] IDs_byte, Authenticator_tgs authenticator, long TS4) throws UnsupportedEncodingException{
		//generate ticket-c,v
		RSACryptography a = new RSACryptography();
		Object kObject = utils.getKey(Utils.KEY_PATH, "pb"+Utils.CLIENT_KEY );
		Key kk = (Key)kObject;
		byte[] IDc_byte = authenticator.getIDc();
		byte[] ADc_byte = authenticator.getADc();
		byte[] IDc_plain_byte = a.encrypt_decrypt(IDc_byte,kk, false);
		byte[] ADc_plain_byte = a.encrypt_decrypt(ADc_byte,kk, false);
		Object keyObject = utils.getKey(Utils.KEY_PATH,"pb"+ Utils.SERVER_KEY );
		Key key = (Key)keyObject;
		Object keyO = utils.getKey(Utils.KEY_PATH,"pb"+ Utils.CLIENT_KEY);
		Key k = (Key)keyO;
		byte[] IDc_Cipher_byte = a.encrypt_decrypt(IDc_plain_byte, key, true);
		byte[] ADc_Cipher_byte = a.encrypt_decrypt(ADc_plain_byte, key, true);
		byte[] IDs_Cipher_byte = a.encrypt_decrypt(IDs_byte, key, true);
		long lifeTime = Utils.LIFETIME;
		Ticket_s ticket = new Ticket_s(k,IDc_Cipher_byte, ADc_Cipher_byte, IDs_Cipher_byte, TS4, lifeTime);
		return ticket;
	}


	public static  void main(String[] args){
		TGSserver tgss = new TGSserver(6000);
		tgss.start();
	}
}
