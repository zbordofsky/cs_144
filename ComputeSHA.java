import java.io.*;
import java.security.*;

public class ComputeSHA
{
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException
	{
		FileInputStream inFile = new FileInputStream(args[0]);
		MessageDigest shaHash = MessageDigest.getInstance("SHA-1"); 

		byte[] byteArray = new byte[1024];
		int bytesRead = -1;

		// todo: use for loop
		while ((bytesRead = inFile.read(byteArray)) != -1) {
			shaHash.update(byteArray, 0, bytesRead);
		}

		//byte[] result = shaHash.digest();
		String result = shaHash.digest().toString();
		System.out.println(result);

		inFile.close();
/*
		//FileInputStream inFile = null;
		//char ch;

		try
		{
		    inFile = new FileInputStream(args[0]);

		    while ((ch = inFile.read()) != -1) 
		    {
			System.out.println(ch);
		    }
		}
		finally
		{
		    if (inFile != null)
		    {
			    inFile.close();
		    }
		}

		inFile.close();
	}
*/
	}
}
