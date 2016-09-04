import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.*;
/**
 * RSACryptography
 * RSACryptography use the privated key to encrypt the plain text and decrypt
 * the cipher text with the public key
 */
public class RSACryptography {
	Cipher cipher;
	
	public RSACryptography() {
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();	
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	
	
	public byte[] encrypt_decrypt(byte[] byteInput, Key key, boolean crypto) {
		try {			
			if(crypto){
				cipher.init(Cipher.ENCRYPT_MODE,key);
			}else{
				cipher.init(Cipher.DECRYPT_MODE,key);
			}
			byte[] cipherByte = cipher.doFinal(byteInput);
			return cipherByte;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
}

