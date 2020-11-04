package org.labs.workshops.checker.Checker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class CheckerTask implements Callable<String>{

	String user;
	String pass;
	public CheckerTask(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}
	@Override
	public String call() throws Exception {
		HttpPost post = new HttpPost(Checker.LOGIN_URL);
		// add request parameter, form parameters
		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("user", user));
		urlParameters.add(new BasicNameValuePair("pass", pass));
		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(post);

			return (response.getStatusLine().getStatusCode()==200)?user+":"+pass:null;
		}
		catch (Exception e) { return null;}		
	}
 

}
