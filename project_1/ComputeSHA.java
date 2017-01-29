import java.io.*;
import java.security.*;

public class ComputeSHA
{
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		if (args.length != 1) {
			System.err.println("error: " + args.length + " arguments supplied, expected 1");
			System.err.println("usage: java ComputeSHA inputfile");
			System.exit(-1);
		}

		FileInputStream inputFile = new FileInputStream(args[0]);
		MessageDigest shaHash = MessageDigest.getInstance("SHA-1");

		byte[] byteArray = new byte[512];
		int bytesRead = -1;

		while ((bytesRead = inputFile.read(byteArray)) != -1) {
			shaHash.update(byteArray, 0, bytesRead);
		}

		inputFile.close();
		byte[] byteDigest = shaHash.digest();
		
		StringBuilder hexDigest = new StringBuilder();
		for (int i = 0; i < byteDigest.length; i++) {
			hexDigest.append(String.format("%02x", byteDigest[i]));
		}

		System.out.println(hexDigest.toString());

		//StringBuilder hexDigest = new StringBuilder();
		//for (int i = 0; i < byteDigest.length; i++) {
		//	hexDigest.append(Integer.toString( (byteDigest[i] & 0xFF) + 0x100, 16 ).substring(1));
		//}

		//System.out.println(hexDigest);
	}
}
