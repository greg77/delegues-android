package com.vinci.jnetmap.android.manager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

import com.vinci.jnetmap.android.rest.RestClient;


public class JnetmapManager {

	static JnetmapManager instance;

	private String username;
	private static String password;
	private Object object;
	private static final Object __synchonizedObject = new Object();

	private JnetmapManager() {

	}

	public static JnetmapManager getInstance() {
		if (instance == null) {
			synchronized (__synchonizedObject) {
				if (instance == null) {
					instance = new JnetmapManager();
				}
			}
		}
		return instance;
	}

	

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPasswordFromLoginInterface(String password) {
		
				password = md5(password);
				try {
					password = sha1(password);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("passwdSha1", password);
				this.password = password;
	
		
	}
	
	public void setPasswordFromRequestInterface(String password){
		this.password = password;
	}
	
	public static String getDigest(String password, String timeStamp) throws NoSuchAlgorithmException{
		String digest = sha1(password+timeStamp);
		Log.d("digest", digest);
		return digest;
	}
	
	public static String md5(String passwd) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			m.update(passwd.getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        byte s[] = m.digest();
        String result = "";
        for (int i = 0; i < s.length; i++) {
          result += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
        }
		return result;
	}
	
	public static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        return sb.toString();
    }

	
	
}
