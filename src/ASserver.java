import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import java.security.Key;

public class ASserver {

	private ServerSocket serverSocket;
	private int port;
	
	byte[] ID_TGS, TS_TGS, lifetime, ticket_TGS;
	private Socket clientSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Utils utils =new Utils();
	
	public ASserver(int port) {
		this.port = port;
	    try {
	      serverSocket = new ServerSocket(port);
	      System.out.println
	        ("AS is working" );
	      acceptConnection();
	    } catch(Exception e) {
	      System.out.println("Server.constructor: " + e);
	    }
	}
	 
	public void acceptConnection() {
		while (true) {
			try {
					clientSocket = serverSocket.accept();
					/**
					 * (1)C->AS: IDc || TS1
					 * (2)AS->C: Epub-c[IDtgs || TS2 || Lifttime1 || Tickettgs]，
					 * Tickettgs = Epub-tgs[Kpub-c || IDc || ADc || IDtgs || TS2 || Lifetime1] 
					 */
					in = new ObjectInputStream(clientSocket.getInputStream());
					out = new ObjectOutputStream(clientSocket.getOutputStream());
					Object inObject = in.readObject();
					if(inObject instanceof C_AS){
						System.out.println("AS received message from client successfully");
					C_AS c_as = (C_AS)inObject;
					String IDc = c_as.getIDc();
					long TS2 = utils.getCurrentTime();
									
					byte[] IDc_byte = IDc.getBytes(Utils.UTF);
					byte[] IDtgs_byte = Utils.ID_TGS.getBytes(Utils.UTF);
					long lifeTime = Utils.LIFETIME;
					//生成Tickettgs
					RSACryptography rsa = new RSACryptography();
					Object keyObjectRSA = utils.getKey(Utils.KEY_PATH,  "pb"+Utils.TGS_KEY );
					Key RSAkey = (Key)keyObjectRSA;
					Object pubCkeyObject = utils.getKey(Utils.KEY_PATH,  "pb"+Utils.CLIENT_KEY );
					Key pubCkey = (Key)pubCkeyObject;
					byte[] IDc_Cipher_byte_tgs = rsa.encrypt_decrypt(IDc_byte, RSAkey, true);
					byte[] ADc_Cipher_byte_tgs = rsa.encrypt_decrypt(Utils.ADc.getBytes(), RSAkey, true);
					byte[] IDtgs_Cipher_byte_tgs = rsa.encrypt_decrypt(IDtgs_byte,RSAkey, true);
					Ticket_tgs ticket = new Ticket_tgs(pubCkey,IDc_Cipher_byte_tgs, ADc_Cipher_byte_tgs, IDtgs_Cipher_byte_tgs, TS2, lifeTime);
							
					//生成AS->C
					RSACryptography rsa2 = new RSACryptography();
					
					byte[] IDtgs_Cipher_byte = rsa2.encrypt_decrypt(IDtgs_byte,pubCkey, true);
					AS_C as_c = new AS_C(IDtgs_Cipher_byte, TS2, lifeTime, ticket);
					if(IDc.equals("North"))
						out.writeObject(as_c);
					else
						out.writeObject("false");		
						}else{
							System.out.println("as error");
							out.writeObject("as error");
						}
							
						
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("As verification pass");
					System.out.println("As send message to client");
				}
		  }

	
	public static void main(String[] args) throws Exception {
		ASserver myServer = new ASserver(5000);
		
		
	}
}
