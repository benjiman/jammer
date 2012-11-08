package uk.co.benjiweber.jammer;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class YammerCredentials {
	private static final String CREDENTIALS_FILE = "yammerCredentials.properties";
	
	public String getToken(String nick) {
		Properties credentials = loadCredentials();
		return credentials.getProperty(nick);
	}
	
	public void saveToken(String nick, String token)  {
		Properties credentials = loadCredentials();
		credentials.put(nick, token);
		saveCredentials(credentials);
	}
	
	public boolean isAuthenticated(String nick) {
		return getToken(nick) != null;
	}
	
	private Properties loadCredentials() {
		InputStream credentialsStream = null;
		try {
			credentialsStream = new FileInputStream(CREDENTIALS_FILE);
			Properties credentials = new Properties();
			credentials.load(credentialsStream);
			credentialsStream.close();
			return credentials;
		} catch (FileNotFoundException e){
			return new Properties();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(credentialsStream);
		}
	}
	
	private void saveCredentials(Properties credentials) {
		OutputStream credentialsStream = null;
		try {
			credentialsStream = new FileOutputStream(CREDENTIALS_FILE);
			credentials.store(credentialsStream,"Credentials Properties");
			credentialsStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(credentialsStream);
		}
	}
	
	private static void close(Closeable stream) {
		try {
			if (stream != null) stream.close();
		} catch (Exception e) {
			
		}
	}
}
