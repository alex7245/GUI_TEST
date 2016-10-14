import java.awt.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class ReadInFile {
	private BufferedReader br;
	private String inAddress;
	private Hashtable readList;
	
	public Hashtable readFile(String address){
		inAddress = address;
		readList = new Hashtable();
		try {
			br = new BufferedReader(new FileReader(new File(inAddress)));
			for (String x = br.readLine(); x != null; x = br.readLine()){
				System.out.println(x);
				String[] ar = new String[2];
				ar = x.split(",");
				String a = ar[0];
				String b = "";
				if (ar.length == 2) {
					b = ar[1];
				}
				readList.put(a,b);
			}
			return readList;
		}catch (IOException e){
			return null;
		}
		
		
	}
}
