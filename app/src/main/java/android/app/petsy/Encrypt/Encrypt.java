package android.app.petsy.Encrypt;

/**
 * Created by Justinas on 2017-05-26.
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;


public class Encrypt {

    public static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    public static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String finalEncrypt(String code){
        String result = "";
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c < 'J' && c > ' ') {
                result += (char)(c + Math.abs(15-i));
                continue;
            }else if (c > 'J' && c < 'z')
                result += (char)(c - Math.abs(15-i));

        }
        return result;
    }

}

