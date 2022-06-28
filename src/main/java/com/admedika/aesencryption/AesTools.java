package com.admedika.aesencryption;


import java.security.*;
import java.util.Arrays;


import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Hello world!
 *
 */
public class AesTools
{

    public AesTools() {
    }

    static byte[] deriveKeyAndIV(byte[] var0, byte[] var1) throws DigestException, NoSuchAlgorithmException {
        byte[] var2 = new byte[48];
        MessageDigest var3 = MessageDigest.getInstance("MD5");
        var3.update(var0);
        var3.update(var1);
        byte[] var4 = var3.digest();
        var3.reset();
        var3.update(var4);
        var3.update(var0);
        var3.update(var1);
        byte[] var5 = var3.digest();
        var3.reset();
        var3.update(var5);
        var3.update(var0);
        var3.update(var1);
        byte[] var6 = var3.digest();
        System.arraycopy(var4, 0, var2, 0, 16);
        System.arraycopy(var5, 0, var2, 16, 16);
        System.arraycopy(var6, 0, var2, 32, 16);
        return var2;
    }

    public static String aesDecrypt(String secretKey, String data) throws DigestException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] var2 = Base64.decode(secretKey, 0);
        byte[] var3 = Arrays.copyOfRange(var2, 8, 16);
        byte[] var4 = Arrays.copyOfRange(var2, 16, var2.length);
        byte[] var5 = deriveKeyAndIV(data.getBytes(), var3);
        byte[] var6 = Arrays.copyOfRange(var5, 0, 32);
        byte[] var7 = Arrays.copyOfRange(var5, 32, 48);
        SecretKeySpec var8 = new SecretKeySpec(var6, "AES");
        IvParameterSpec var9 = new IvParameterSpec(var7);
        Cipher var10 = Cipher.getInstance("AES/CBC/PKCS5Padding");
        var10.init(2, var8, var9);
        return new String(var10.doFinal(var4));
    }

    public static String aesEncrypt(String secretKey, String data) throws DigestException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] var2 = new byte[8];
        SecureRandom var3 = new SecureRandom();
        var3.nextBytes(var2);
        byte[] var4 = deriveKeyAndIV(secretKey.getBytes(), var2);
        byte[] var5 = Arrays.copyOfRange(var4, 0, 32);
        byte[] var6 = Arrays.copyOfRange(var4, 32, 48);
        SecretKeySpec var7 = new SecretKeySpec(var5, "AES");
        IvParameterSpec var8 = new IvParameterSpec(var6);
        Cipher var9 = Cipher.getInstance("AES/CBC/PKCS5Padding");
        var9.init(1, var7, var8);
        byte[] var10 = var9.doFinal(data.getBytes());
        byte[] var11 = new byte[16 + var10.length];
        System.arraycopy("Salted__".getBytes(), 0, var11, 0, 8);
        System.arraycopy(var2, 0, var11, 8, 8);
        System.arraycopy(var10, 0, var11, 16, var10.length);
        return Base64.encodeToString(var11, 0);
    }
}
