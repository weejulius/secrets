package jyu.secret;

import android.content.Context;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import jyu.secret.model.DBSession;
import jyu.secret.model.Secret;
import jyu.secret.model.User;

/**
 * * 生成随机数rnd
 * * 保存到数据库
 * * encrypt_rnd = sha1(rnd)
 * * 生成seed key = sha1(sha1(short_pwd) + encrypt_rnd),保存在内存中
 * * 加密数据 aes(aes(seed_key,encrypt_rnd),data)
 * * 加密密码 sha1(sha1(pwd) + encrypt_rnd)
 * * 解密数据 aes_decrypt(aes_decrypt(seed_key,encrypt_rnd),data)
 * <p/>
 * <p/>
 * Created by jyu on 15-6-5.
 */
public class Encrypts {

    private static final Encrypts ins = new Encrypts();

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    private String encryptedRnd;
    private String seedForShortUserPwd;

    private Encrypts() {
    }

    public static Encrypts ins() {
        return ins;
    }

    /**
     * 生成random key
     */
    public String genRandomKey() {

        return UUID.randomUUID().toString();

    }


    public String loadRandomKey(String rnd) {

        if (rnd == null) {
            return null;
        }
        encryptedRnd = encode("SHA1", rnd);
        return encryptedRnd;
    }


    public void saveRandomKey(Context ctx, User user) {
        if (user == null) {
            return;
        }

        user.setRnd(encryptedRnd);
        DBSession.ins(ctx).getUserDao().update(user);
    }


    public void encryptSecret(String userPwd, Secret secret) {
        secret.setName(encryptSecretData(secret.getName()));
        secret.setTitle(encryptSecretData(secret.getTitle()));
        secret.setPwd(encryptSecretPwd(userPwd, secret.getPwd()));
    }


    public void decryptSecret(Secret secret) {
        secret.setName(decryptSecretData(secret.getName()));
        secret.setTitle(decryptSecretData(secret.getTitle()));
    }

    /**
     * the seed to encrypt
     * <p/>
     * seed = sha1(sha1(short_pwd) + encrypt_rnd)
     *
     * @return
     */
    public String genSeed(String userPwd) {

        return encode("SHA1", encode("SHA1", userPwd) + encryptedRnd);
    }

    public String genSeedForShortPwd(String shortUserPwd) {

        String encode = genSeed(shortUserPwd);
        seedForShortUserPwd = encode;
        return encode;
    }

    /**
     * aes(aes(seed_key,encrypt_rnd),data)
     *
     * @return
     */
    public String encryptSecretData(String data) {

        return encodeByAes(encodeByAes(seedForShortUserPwd, encryptedRnd), data);
    }


    /**
     * aes(seed,raw)
     *
     * @param data
     * @return
     */
    public String decryptSecretData(String data) {


        return decodeByAes(encodeByAes(seedForShortUserPwd, encryptedRnd), data);
    }


    /**
     * aes(aes(seed_key,encrypt_rnd),data)
     *
     * @return
     */
    public String encryptSecretPwd(String userPwd, String secretPwd) {

        return encodeByAes(encodeByAes(genSeed(userPwd), encryptedRnd), secretPwd);
    }


    public String decryptSecretPwd(String userPwd, String secretPwd) {
        return decodeByAes(encodeByAes(genSeed(userPwd), encryptedRnd), secretPwd);
    }


    /**
     * 更新密码
     */
    public void updatePwd() {

    }


    /**
     * encode string
     *
     * @param algorithm "SHA1" or "MD5"
     * @param str
     * @return String
     */
    public String encode(String algorithm, String str) {
        if (str == null) {
            return null;
        }


        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }


    public String encodeByAes(String seed, String clearText) {
        // Log.d(TAG, "加密前的seed=" + seed + ",内容为:" + clearText);
        byte[] result = null;
        try {
            byte[] rawkey = getRawKey(seed.getBytes());
            result = encrypt(rawkey, clearText.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String content = toHex(result);
        // Log.d(TAG, "加密后的内容为:" + content);
        return content;

    }

    public String decodeByAes(String seed, String encrypted) {
        // Log.d(TAG, "解密前的seed=" + seed + ",内容为:" + encrypted);
        byte[] rawKey;
        try {
            rawKey = getRawKey(seed.getBytes());
            byte[] enc = toByte(encrypted);
            byte[] result = decrypt(rawKey, enc);
            String coentn = new String(result);
            // Log.d(TAG, "解密后的内容为:" + coentn);
            return coentn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = null;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        kgen.init(128, sr);
        SecretKey sKey = kgen.generateKey();
        byte[] raw = sKey.getEncoded();

        return raw;
    }

    private byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        //     Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;

    }

    private byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    private String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    private String fromHex(String hex) {
        return new String(toByte(hex));
    }

    private byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    private String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private void appendHex(StringBuffer sb, byte b) {
        final String HEX = "0123456789ABCDEF";
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }


}
