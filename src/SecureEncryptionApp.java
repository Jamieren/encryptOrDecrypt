import java.security.MessageDigest;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple program that allows users to encrypt and decrypt text using various methods.
 * Author: Jamie Ren
 * 
 */
public class SecureEncryptionApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the text: ");
        String input = scanner.nextLine();

        String operationChoice;
        do {
            System.out.print("Do you want to encrypt or decrypt? (encrypt/decrypt): ");
            operationChoice = scanner.nextLine();

            if (operationChoice.equalsIgnoreCase("encrypt")) {
                System.out.println("Choose an encryption method:");
                System.out.println("1. MD5 Encryption");
                System.out.println("2. AES Encryption");
                System.out.println("3. Base64 Encoding");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                String result = "";
                switch (choice) {
                    case 1:
                        result = encryptMD5(input);
                        break;
                    case 2:
                        System.out.print("Enter the AES encryption key (16 characters): ");
                        String aesKey = scanner.nextLine();
                        result = encryptAES(input, aesKey);
                        break;
                    case 3:
                        result = encodeBase64(input);
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        continue; // Restart the loop if the choice is invalid
                }

                System.out.println("Encrypted/Encoded Text: " + result);
            } else if (operationChoice.equalsIgnoreCase("decrypt")) {
                System.out.print("Enter the text to decrypt: ");
                String encryptedText = scanner.nextLine();
                System.out.print("Enter the decryption key: ");
                String decryptionKey = scanner.nextLine();

                String decryptedText = decryptAES(encryptedText, decryptionKey);
                System.out.println("Decrypted Text: " + decryptedText);
            } else {
                System.out.println("Invalid operation choice.");
            }
        } while (!operationChoice.equalsIgnoreCase("encrypt") && !operationChoice.equalsIgnoreCase("decrypt"));

        scanner.close();
    }

    /**
     * Encrypts the input text using the MD5 hash algorithm.
     *
     * @param input The text to be encrypted.
     * @return The MD5 hash of the input text.
     */
    public static String encryptMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            return bytesToHexString(digest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Encrypts the input text using the AES encryption algorithm.
     *
     * @param input The text to be encrypted.
     * @param key   The encryption key.
     * @return The Base64-encoded encrypted text.
     */
    public static String encryptAES(String input, String key) {
        try {
            byte[] keyBytes = key.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Encodes the input text using Base64 encoding.
     *
     * @param input The text to be encoded.
     * @return The Base64-encoded text.
     */
    public static String encodeBase64(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    /**
     * Decrypts the encrypted text using the AES decryption algorithm.
     *
     * @param encryptedText The encrypted text to be decrypted.
     * @param key           The decryption key.
     * @return The decrypted text.
     */
    public static String decryptAES(String encryptedText, String key) {
        try {
            byte[] keyBytes = key.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes The byte array to convert.
     * @return The hexadecimal representation of the byte array.
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
