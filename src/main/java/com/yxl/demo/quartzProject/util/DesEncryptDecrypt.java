package com.yxl.demo.quartzProject.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * 统一加密解密的类
 *
 */

public class DesEncryptDecrypt {
	
	private static final Logger LOG = LoggerFactory.getLogger(DesEncryptDecrypt.class);
	
    //定义加密算法，有DES、DESede(即3DES)、Blowfish
    private static final String Algorithm = "DESede";    
    private static final String PASSWORD_CRYPT_KEY = "2015DWwarehousemysqlhomemsms51234housreqwqweiqwjhsdsa";
    private final static byte[] hex = "0123456789ABCDEF".getBytes();
    
    
    /**
     * 加密方法
     * @param src 源数据的字节数组
     * @return 
     */
    public String encryptMode(byte[] src) {
        try {
             SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);    //生成密钥
             Cipher c1 = Cipher.getInstance(Algorithm);    //实例化负责加密/解密的Cipher工具类
             c1.init(Cipher.ENCRYPT_MODE, deskey);    //初始化为加密模式
             return Bytes2HexString(c1.doFinal(src));
         } catch (java.security.NoSuchAlgorithmException e1) {
        	 LOG.error("encryptMode happend error: " ,e1);
         } catch (javax.crypto.NoSuchPaddingException e2) {
        	 LOG.error("encryptMode happend error: " ,e2);
         } catch (Exception e3) {
        	 LOG.error("encryptMode happend error: " ,e3);
         }
         return null;
     }
    
    
    /**
     * 解密函数
     * @param src 密文的字节数组
     * @return
     */
    public String decryptMode(byte[] src) {      
        try {
            SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);    //初始化为解密模式
            return new String(c1.doFinal(src));
        } catch (java.security.NoSuchAlgorithmException e1) {
        	LOG.error("decryptMode happend error: " ,e1);
        } catch (javax.crypto.NoSuchPaddingException e2) {
        	LOG.error("decryptMode happend error: " ,e2);
        } catch (Exception e3) {
        	LOG.error("decryptMode happend error: " ,e3);
        }
        return null;
     }
    
    
    /*
     * 根据字符串生成密钥字节数组 
     * @param keyStr 密钥字符串
     * @return 
     * @throws UnsupportedEncodingException
     */
    public byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException{
        byte[] key = new byte[24];    //声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes("UTF-8");    //将字符串转成字节数组
        
        /*
         * 执行数组拷贝
         * System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
         */
        if(key.length > temp.length){
            //如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        }else{
            //如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    } 
	private int parse(char c) {
		 if (c >= 'a')
		 return (c - 'a' + 10) & 0x0f;
		 if (c >= 'A')
		 return (c - 'A' + 10) & 0x0f;
		 return (c - '0') & 0x0f;
	}
	
	 // 从字节数组到十六进制字符串转换
	 public String Bytes2HexString(byte[] b) {
		 byte[] buff = new byte[2 * b.length];
		 for (int i = 0; i < b.length; i++) {
		 buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			 buff[2 * i + 1] = hex[b[i] & 0x0f];
		 }
		 return new String(buff);
	 }

	 // 从十六进制字符串到字节数组转换
	 public byte[] HexString2Bytes(String hexstr) {
		 byte[] b = new byte[hexstr.length() / 2];
		 int j = 0;
		 for (int i = 0; i < b.length; i++) {
		 char c0 = hexstr.charAt(j++);
		 char c1 = hexstr.charAt(j++);
		 b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		 }
		 return b;
	 }
	 
	public static void main(String[] args){
//		DesEncryptDecrypt d = new DesEncryptDecrypt();
//		String mingwen = "this is a Test !@#$%^&*()_+~`12345<>?";
//		String miwen = d.encryptMode(mingwen.getBytes());
//		System.out.println(miwen);
//		System.out.println(d.decryptMode(mingwen.getBytes())); 
		
		try {
			String mingwen = "ak(su#ksKdi9";
			
			DesEncryptDecrypt desc = new DesEncryptDecrypt();
			String encode = desc.encryptMode(mingwen.getBytes());
			System.out.println("encode:"+encode);
			String decode = desc.decryptMode(desc.HexString2Bytes(encode));
			System.out.println("decode:"+decode);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
