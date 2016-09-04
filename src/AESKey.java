import java.security.*;
import javax.crypto.*;

public class AESKey{
	private final static int BIT = 128;
	private KeyGenerator keyGen;
	private static Key key;
	
	public AESKey() {
		try {
			keyGen = KeyGenerator.getInstance("AES");
			generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	public byte[] AESencrypt(byte[] plainText,Key k)throws Exception
	{	
		Cipher cipher=Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE,k);
		byte[] cipherText=cipher.doFinal(plainText);	
		return cipherText;	
	}
	
	
	public String AESdecrypt(byte[] t,Key k)throws Exception
	{
		Cipher cipher=Cipher.getInstance("AES");	
		cipher.init(Cipher.DECRYPT_MODE,k);
		byte[] newPlainText = cipher.doFinal(t);	
		return new String(newPlainText,"UTF8");		
	}
	private void generateKey() {
		keyGen.init(BIT);
		key = keyGen.generateKey();
		System.out.println("key>>>>>>>>"+key);
	}
	
	public static Key getKey(){
		return key;
	}
	/*public static void main(String args[]) throws Exception{
		AESKey app = new AESKey();
		byte[] plain="lalaaa!".getBytes();
		System.out.println("plain>>>>>>>"+plain.toString());
		byte[] cipherByte=app.AESencrypt(plain, getKey());
		System.out.println(x);*/
		
		
		/*app.KeyGen;
		app.generateKey(KEY_PATH, SERVER_KEY);
		app.generateKey(KEY_PATH, TGS_KEY);
		byte[] plain="lalaaa!".getBytes();
		byte[]	eb=app.encrypt_decrypt_tgs(plain, true);
		System.out.println("ÃÜÎÄ£º"+eb);
		byte[]	db=app.encrypt_decrypt_tgs(eb, false);
		System.out.println("Ã÷ÎÄ£º"+new String(db));
		}*/
	
	
	}



