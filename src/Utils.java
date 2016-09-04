/**
 * 这个类定义了全局定量
 * 整个project多个类经常要使用的静态方法
 */

import java.io.*;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

public class Utils {
		public final static long LIFETIME = 60000;
		public final static String ID_TGS = "123456789";
		public final static String ADc = "localhost";
		public final static String UTF = "UTF8";
		public final static String KEY_PATH = "keys/";
		public final static String TGS_KEY = "tgs.txt";
		public final static String PRIVATE_TGS_KEY = "prtgs.txt";
		public final static String SERVER_KEY = "server.txt";
		public final static String PRIVATE_SERVER_KEY = "prserver.txt";
		
		public final static String CLIENT_KEY = "client.txt";
		public final static String AES_POSTFIX = ".aes";
		public final static String RSA_POSTFIX = ".rsa";
		public final static String PUBLIC_CLIENT_KEY = "client_public.txt";
		
		/**
		 * 获得当前的时间戳
		 * @return
		 */
		public  long getCurrentTime(){
			return System.currentTimeMillis();	
		}
		public void generateKey(String keyPath, String server){
			KeyPairGenerator kpg;
			KeyPair kp;
			RSAPublicKey pbkey;
			RSAPrivateKey prkey;
			try {
			  		 kpg=KeyPairGenerator.getInstance("RSA");
			   		 kpg.initialize(1024);
			   		 kp=kpg.generateKeyPair();
			   		 pbkey=(RSAPublicKey)kp.getPublic();
			   		 prkey=(RSAPrivateKey)kp.getPrivate();
			    	setKey(keyPath,"pb"+server,(Object)pbkey);
			    	System.out.println("public key is "+(Object)pbkey);
					
			    	setKey(keyPath,"pr"+server,(Object)prkey);
			    		
			    }
			catch (Exception exception) {
			             exception.printStackTrace();
			         }
			         
			    	
			    }
		
		/**
		 * 从文件读取密钥
		 * @param keyPath
		 * @param keyName
		 * @return key
		 */
		public  Object getKey(String keyPath, String keyName){
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(keyPath + keyName));
				Object key = in.readObject();
				in.close();
				if(key != null && key instanceof Key)
					return key;
			} catch (Exception e) {} 
			return null;
		}
		public void setKey(String keyPath, String keyName,Object o)throws Exception{
				File newFile = new File(keyPath + keyName);
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(newFile));
				
				out.writeObject(o);
				out.flush();
				out.close();		
		}
		
		/**
		 * 客户端加密解密方法
		 * @param bytes
		 * @param encrypt_decrypt
		 * @return
		 */
		public byte[] encrypt_decrypt_client(byte[] bytes, boolean encrypt_decrypt){
			RSACryptography rsa = new RSACryptography();
			Object keyObject;
			if(encrypt_decrypt){
				keyObject = getKey(Utils.KEY_PATH, "pb"+CLIENT_KEY );
				
				
			}else{
				keyObject = getKey(Utils.KEY_PATH,"pr"+CLIENT_KEY );
				
			}
			Key key = (Key)keyObject;
			byte[] plainBytes = rsa.encrypt_decrypt(bytes, key, encrypt_decrypt);
			return plainBytes;
		}
	
		/**
		 * TGS服务器加密解密方法
		 * @param bytes
		 * @param encrypt_decrypt
		 * @return
		 */
		public byte[] encrypt_decrypt_tgs(byte[] bytes, boolean encrypt_decrypt){
			RSACryptography rsa = new RSACryptography();
			Object keyObject;
			if(encrypt_decrypt){
				keyObject = getKey(Utils.KEY_PATH,  "pb"+Utils.TGS_KEY );
				
				
			}else{
				keyObject = getKey(Utils.KEY_PATH,  "pr"+Utils.TGS_KEY  );
				
			}
			Key key = (Key)keyObject;
			byte[] plainBytes = rsa.encrypt_decrypt(bytes, key, encrypt_decrypt);
			return plainBytes;
		}
		
		/**
		 * 应用服务器加密解密方法
		 * @param bytes
		 * @param encrypt_decrypt
		 * @return
		 */
		public byte[] encrypt_decrypt_server(byte[] bytes, boolean encrypt_decrypt){
			RSACryptography rsa = new RSACryptography();
			Object keyObject;
			if(encrypt_decrypt){
				keyObject = getKey(Utils.KEY_PATH,  "pb"+Utils.SERVER_KEY );
				
				
			}else{
				keyObject = getKey(Utils.KEY_PATH,  "pr"+Utils.SERVER_KEY );
				
			}
			Key key = (Key)keyObject;
			byte[] plainBytes = rsa.encrypt_decrypt(bytes, key, encrypt_decrypt);
			return plainBytes;
		}
		public static void main(String args[]) throws Exception{
			Utils app = new Utils();
			app.generateKey(KEY_PATH, CLIENT_KEY);
			app.generateKey(KEY_PATH, SERVER_KEY);
			app.generateKey(KEY_PATH, TGS_KEY);
			//byte[] plain="lalaaa!".getBytes();
			//byte[]	eb=app.encrypt_decrypt_tgs(plain, true);
			//System.out.println("密文："+eb);
			//byte[]	db=app.encrypt_decrypt_tgs(eb, false);
			//System.out.println("明文："+new String(db));
			}
		
		

}


