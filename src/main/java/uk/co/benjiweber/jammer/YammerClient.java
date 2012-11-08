package uk.co.benjiweber.jammer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class YammerClient {
	
	private final String clientId;
	private final String clientSecret;

	public YammerClient(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}
	
	public String getAuthUrl() {
		return String.format(
			"https://www.yammer.com/dialog/oauth?client_id=%s&redirect_uri=%s&response_type=token",
			clientId,
			clientSecret
		);
	}
	
	public String authenticate(String url, String accessKey) {
		return url + "?access_token=" + accessKey;
	}
	
	public void sendMessage(String content, String accessKey, String yammerGroup) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(authenticate("https://www.yammer.com/api/v1/messages.json", accessKey));
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("body", content));
			nameValuePairs.add(new BasicNameValuePair("group_id", yammerGroup));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			HttpResponse response = client.execute(post);
			
			printResponse(response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void printResponse(HttpResponse response) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		}
	}
}
