
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*******************************************************************************
 * AES encryption & decryption algorithm
 * 
 * @ π”√byte2hex◊ˆ◊™¬Î
 * 
 */

public class AEScode{

    // encryption
    public static String Encrypt(String plaintext, String sKey) throws Exception {
        if (sKey == null) {
            System.out.println("Key cannot be null during Encryption");
            return null;
        }
        
        if (sKey.length() != 16) {
            System.out.println("Key's length must be 16 during Encryption");
            return null;
        }
        
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());

        return byte2hex(encrypted).toLowerCase();
    }

    // decryption
    public static String Decrypt(String myCipher, String sKey) throws Exception {
        try {
            
            if (sKey == null) {
                System.out.println("Key cannot be null during Decryption");
                return null;
            }
            
            if (sKey.length() != 16) {
                System.out.println("Key's length must be 16 during Decryption");
                return null;
            }
            
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = hex2byte(myCipher);
            
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
            
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
   public static void main(String args[]) throws Exception{
		//AEScode app = new AEScode();
	   System.out.println("0102030405060708".length());
	   String cipher=AEScode.Encrypt("linlin","0102030405060708");
	   System.out.println(cipher);
	   String plaintext=AEScode.Decrypt(cipher, "0102030405060708");
	   System.out.println(plaintext);
		//String plain="lalaaa!".getString();
		//String	eb=app. Encrypt(plain, true);
		//System.out.println("密文："+eb);
		//String	db=app.Decrypt(eb, false);
		//System.out.println("明文："+new String(db));
		}
  
}

