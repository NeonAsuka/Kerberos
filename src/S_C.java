
import java.io.*;
public class S_C implements Serializable{
	long TS5;
	byte[] subkey2;
	//S->C: Epub-c [TS5 + 1 || Subkey2]
	public S_C(long TS5, byte[] subkey2)
	{
		this.TS5 = TS5;
		this.subkey2 = subkey2;
	}
}
