import java.io.*;
import java.security.*;

// Implement Java class 'ComputeSHA' to compute SHA-1 hash over content of an input file
public class ComputeSHA
{
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		// Check that an input file was correctly supplied to the program
		if (args.length != 1) {
			System.err.println("error: " + args.length + " arguments supplied, expected 1");
			System.err.println("usage: java ComputeSHA inputfile");
			System.exit(-1);
		}

		// Prepare to read contents of the input file, using MessageDigest to obtain SHA-1 hash
		FileInputStream inputFile = new FileInputStream(args[0]);
		MessageDigest shaHash = MessageDigest.getInstance("SHA-1");

		// Read 512 bytes from the file at a time, until we reach end of file (bytesRead = -1)
		byte[] byteArray = new byte[512];
		int bytesRead = -1;

		while ((bytesRead = inputFile.read(byteArray)) != -1) {
			shaHash.update(byteArray, 0, bytesRead);
		}

		// When finished reading from input file, close file stream and compute final message digest
		inputFile.close();
		byte[] byteDigest = shaHash.digest();
		
		// Convert byte form of message digest to a hexadecimal string that will be output
		StringBuilder hexDigest = new StringBuilder();
		for (int i = 0; i < byteDigest.length; i++) {
			hexDigest.append(String.format("%02x", byteDigest[i]));
		}

		// Print the resulting SHA-1 hash for the input file
		System.out.println(hexDigest.toString());
	}
}
