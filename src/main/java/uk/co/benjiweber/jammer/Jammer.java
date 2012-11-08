package uk.co.benjiweber.jammer;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class Jammer extends PircBot {
	private final YammerCredentials credentials = new YammerCredentials();
	private final YammerClient yammerClient;
	private final String yammerGroup;
	private final String server;
	private final String channel;
	
	public Jammer(String server, String channel, String group, YammerClient yammerClient) {
		this.server = server;
		this.channel = channel;
		this.yammerGroup = group;
		this.setName(getClass().getSimpleName());
		this.setVerbose(true);
		
		this.yammerClient = yammerClient;
	}

	public static void main(String... args) throws Exception {
		if (args.length < 5) {
			System.err.println("Usage: Jammer <irc server> <channel> <yammer group id> <yammer client id> <yammer client secret>");
			System.exit(1);
		}
		String clientId = args[3];
		String clientSecret = args[4];
		
		String server = args[0];
		String channel = args[1];
		String group = args[2];
		
		Jammer bot = new Jammer(server, channel, group, new YammerClient(clientId, clientSecret));
		bot.connect();

	}

	private void connect() throws NickAlreadyInUseException, IOException, IrcException {
		this.connect(server);
		this.joinChannel(channel);
	}

	@Override
	public void onMessage(String messageChannel, String sender, String login, String hostname, String message) {
		if (this.channel.equals(messageChannel)) {
			bridge(message, sender);
		}
	}
	
	@Override
	protected void onPrivateMessage(String sender, String login, String hostname, String message) {
		if (message.equals("auth")) {
			sendMessage(sender, "Please use " + yammerClient.getAuthUrl());
			sendMessage(sender, "... and then pm me with \"auth <token>\"");
		} else if (message.startsWith("auth ")) {
			String token = message.split(" ")[1];
			credentials.saveToken(sender, token);
			sendMessage(sender, "Saved token " + token + " your messages should now be bridged to yammer");
		}
	}
	
	private void bridge(String message, String nick) {
		if (credentials.isAuthenticated(nick)) {
			System.out.println(nick + " sending to yammer " + message);
			yammerClient.sendMessage(message, credentials.getToken(nick), yammerGroup);
			System.out.println("Sent");
		}
	}

}
