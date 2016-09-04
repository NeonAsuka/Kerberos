import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

public class SServer {
	protected Socket clientSocket;
	protected ServerSocket serverSocket;
	protected ObjectInputStream in;
	protected ObjectOutputStream out;
	protected int port;
	private Utils utils =new Utils();
	private AEScode AESencrypt=new AEScode();
	
    public SServer(int port) {
		
		this.port = port;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Service Server is working");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		byte[] keyByte = null;
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				System.out.println(clientSocket);
				/**
				 * (5)C->S: Tickets || Authenticators
				 * (6)S->C: Epub-c [TS5 + 1 || Subkey2] ，
				 * Tickets = Epub-s[Kpub-c || IDc || ADc || IDs || TS4 || Lifetime2] ，
				 * Authenticators = Epri-c[IDc || ADc || TS5 ] 
				 * SessionKey = Subkey2 
				 */
				in = new ObjectInputStream(clientSocket.getInputStream());
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				Object inObject = in.readObject();
				if(inObject instanceof C_S){
					System.out.println("received message from client");
					C_S c_s = (C_S)inObject;
					Ticket_s ticket_s = c_s.getTicket_s();
					Authenticator_s authenticator_s = c_s.getAuthenticator_s();
					long TS4 = ticket_s.getTS4();
					long TS5 = authenticator_s.getTS5();
					long lifeTime = ticket_s.getLifetime2();
					if(!timeValidation(lifeTime, TS4, TS5)){
						out.writeObject("false");
						System.out.println("server time error");
					}else if(!IDc_ADcValidate(ticket_s, authenticator_s)){
						out.writeObject("false");
						System.out.println("server IDc,ADc error");
					}else{
						long TS6 = TS5 + 1000;
						AESKey aesKey = new AESKey();
						Key key = aesKey.getKey();
						keyByte = key.getEncoded();
						//封装S->C
						S_C s2c = new S_C(TS6, keyByte);
						out.writeObject(s2c);
						System.out.println("Server verification pass");
						//通讯
						communication(keyByte);	
					}
					
				}else{
					out.writeObject("false");
					System.out.println("server verificaition error");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
	}
		
	public boolean timeValidation(long lifeTime, long startTime, long endTime){
		long validationLifeTime = startTime + lifeTime;
		return (validationLifeTime > endTime);
	}
	
	private boolean IDc_ADcValidate(Ticket_s ticket_s, Authenticator_s authenticator_s){
		try{
			RSACryptography rsa = new RSACryptography();
			
			Object keyObject = utils.getKey(Utils.KEY_PATH, "pb"+Utils.CLIENT_KEY );
			Key key = (Key)keyObject;
			byte[] IDc_Cipher_authenticator_byte = authenticator_s.getIDc();
			byte[] ADc_Cipher_authenticator_byte = authenticator_s.getADc();
			byte[] IDc_Plain_authenticator_byte = rsa.encrypt_decrypt(IDc_Cipher_authenticator_byte, key, false);
			byte[] ADc_Plain_authenticator_byte = rsa.encrypt_decrypt(ADc_Cipher_authenticator_byte,key, false);
			String IDc_Plain_authenticator_String = new String(IDc_Plain_authenticator_byte, Utils.UTF);
			String ADc_Plain_authenticator_String = new String(ADc_Plain_authenticator_byte, Utils.UTF);
			byte[] IDc_Cipher_ticket_byte = ticket_s.getIDc();
			byte[] ADc_Cipher_ticket_byte = ticket_s.getADc();
			byte[] IDc_Plain_ticket_byte = utils.encrypt_decrypt_server(IDc_Cipher_ticket_byte, false);
			byte[] ADc_Plain_ticket_byte = utils.encrypt_decrypt_server(ADc_Cipher_ticket_byte, false);
			String IDc_Plain_ticket_String = new String(IDc_Plain_ticket_byte, Utils.UTF);
			String ADc_Plain_ticket_String = new String(ADc_Plain_ticket_byte, Utils.UTF);
			return (IDc_Plain_authenticator_String.equals(IDc_Plain_ticket_String) && ADc_Plain_authenticator_String.equals(ADc_Plain_ticket_String));
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	//加密解密
	public byte[] 
			AESencrypt(byte[] plainText,Key k)throws Exception
	{		
		
		Cipher cipher=Cipher.getInstance("AESencrypt");
		
		System.out.println("\n"+cipher.getProvider().getInfo());
		//System.out.println(cipher);
		// 使用私鈅加密	
		cipher.init(Cipher.ENCRYPT_MODE,k);
		byte[] cipherText=cipher.doFinal(plainText);	
		System.out.println("AESCipher----" + new String(cipherText));
		return cipherText;
		
	}
	public String AESdecrypt(byte[] t,Key k)throws Exception
	{
		//decryption
		Cipher cipher=Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE,k);
		byte[] newPlainText = cipher.doFinal(t);
		System.out.println("AESPlainText----" + new String(newPlainText));
		
		return new String(newPlainText,"UTF8");
	}
	
	public void communication(byte[] keyByte)
	{	
		SecretKeySpec sessionKey = new SecretKeySpec(keyByte,"AES");
		//监听client的通信信息
		Object msg;
		try{
			while(true)
			{	
				if ((msg = in.readObject()) == null)
					break;
				    if(msg!=null && msg instanceof byte[])
					{
						byte[] str = (byte[])msg;
						String plain = AESdecrypt(str,sessionKey);
						System.out.println("Server get plaintext : "+plain);
						String replyMsg=null;
						
						if(plain.equals("engine noise")){
							replyMsg="Check oil pressure at the oil pressure sending unit port on the engine with a gauge";
						}else if(plain.equals("battery runs down"))
						{
							replyMsg="The first things you want to check are the battery and the output of the charging system (alternator). "
									+ "One way to do this is to turn on the headlights. "
									+ "If the headlights come on with normal brightness, "
									+ "the no-crank problem is probably not the battery, "
									+ "but a bad starter relay or solenoid,"
									+ "a poor wiring connection between the relay/solenoid and starter, "
									+ "or a bad starter. "
									+ "If the headlights are dim or do not come on at all, "
									+ "you need to check battery voltage and charging output.";							
						}else if(plain.equals("brake noise"))
						{
					        replyMsg="Dampen the pads,"
					        		+ "Lubricate the calipers,"
					        		+ "Replace the caliper hardware (slides, pins or bushings),"
					        		+ "Replace the pads"
                                    + "Resurface the rotors"
                                    + "Clean the brakes (drums)";
                                    }
				
							else{
						replyMsg="no match content";							
						}
						System.out.println(replyMsg);
						String cipher=AEScode.Encrypt(replyMsg,"0102030405060708");
						System.out.println("cipher>>>>>"+cipher);
						System.out.println("Server side plaintext>>>>>>>"+AEScode.Decrypt(cipher, "0102030405060708"));
						out.writeObject(cipher);
						//System.out.println(cipher);
 
						
/*	byte[] AESencrypt(byte[] plainText,Key k)throws Exception
	{		
		
		Cipher cipher=Cipher.getInstance("AESencrypt");
		
		System.out.println("\n"+cipher.getProvider().getInfo());
		//System.out.println(cipher);
		// 使用私鈅加密	
		cipher.init(Cipher.ENCRYPT_MODE,k);
		byte[] cipherText=cipher.doFinal(plainText);	
		System.out.println("AESCipher----" + new String(cipherText));
		return cipherText;
		
	}
						byte[] replyByte = (byte[])reply;
						
						byte[] cipher=AESencrypt(replyByte,sessionKey);
						out.writeObject(cipher);
						System.out.println(cipher);*/
						
						
					
/*						Object reply=replyMsg;
						byte[] replyByte = (byte[])reply;
						AESKey a=new AESKey();
						byte[] cipher= a.AESencrypt(replyByte,sessionKey);
						System.out.println(cipher);*/
						
						
					}
			}
		} catch (Exception se) {
			try {
				in.close();
				clientSocket.close();
			} catch (Exception e) {}
		} 
	}
	
	

	
	public static  void main(String[] args){
		SServer ss = new SServer(9000);
		ss.start();
		
	}
}
