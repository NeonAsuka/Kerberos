import java.io.*;

public class FileStream{
	public FileStream(){}
	
	private String filePath="key/enc.txt";
	
	public byte[] readFile() throws Exception{
		File newFile = new File(filePath);
		FileInputStream fis = new FileInputStream(newFile);
		
		byte newByte[] = new byte[fis.available()];
		fis.read(newByte);
		fis.close();
		return newByte;
	}
	
	public void writeFile(byte[] nbyte) throws Exception{
		File newFile = new File(filePath);
		FileOutputStream fos = new FileOutputStream(newFile);
		
		fos.write(nbyte);
		fos.flush();
		fos.close();
	}
	public static void main(String args[]) throws Exception{
		DEScode newds = new DEScode();
		FileStream fs = new FileStream();
		byte test[] = {'1','2','3','4','5'};
		fs.writeFile(test);
		byte testRt[] = fs.readFile();
		System.out.println(new String(testRt,"UTF8"));
	}
}