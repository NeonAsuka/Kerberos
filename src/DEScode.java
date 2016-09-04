import java.security.*; 
import javax.crypto.*; 
import javax.crypto.spec.*; 
import java.security.*; 
import javax.crypto.*; 
import javax.crypto.spec.*; 
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.awt.*;
import java.lang.*;
import  javax.crypto.KeyGenerator;
import javax.rmi.CORBA.Util;

 
	public class DEScode{ 
		
		DEScode(){} 

		public byte[] encryptByDES(byte[] bytP,byte[] bytKey) throws Exception{ 
			DESKeySpec desKS = new DESKeySpec(bytKey); 
			SecretKeyFactory skf = SecretKeyFactory.getInstance("DES"); 
			SecretKey sk = skf.generateSecret(desKS); 
			Cipher cip = Cipher.getInstance("DES"); 
			cip.init(Cipher.ENCRYPT_MODE,sk); 
			byte[] nbyte= cip.doFinal(bytP); 
			FileStream fs=new FileStream();
			fs.writeFile(nbyte);
			return nbyte;
		} 

 
		public byte[] decryptByDES(byte[] bytKey) throws Exception{
			FileStream fs=new FileStream();
			byte[] bytE=fs.readFile();
			DESKeySpec desKS = new DESKeySpec(bytKey); 
			SecretKeyFactory skf = SecretKeyFactory.getInstance("DES"); 
			SecretKey sk = skf.generateSecret(desKS); 
			Cipher cip = Cipher.getInstance("DES"); 
			cip.init(Cipher.DECRYPT_MODE,sk); 
			byte[] dbyte= cip.doFinal(bytE); 
			return dbyte;
		} 
	
	
		
} 
                                                                           
