jammer
======

IRC->Yammer Bridge

Jammer is a simple IRC bot that posts all messages from authorised users in an IRC channel to a specified group in Yammer.

Jammer is written in Java and currently just a quick proof of concept. There is no security. Tokens are stored in plain text etc. 

It might be useful to someone to learn how to use Yammer.

$ mvn compile
$ mvn assembly:single
$ java -jar ./target/jammer-0.0.1-SNAPSHOT-jar-with-dependencies.jar <irc server (in quotes)> <channel> <yammer group id> <yammer client id> <yammer client secret>

Then in your IRC client:

 /msg jammer auth

Click the link provided, approve the app, and copy the authentication token back with

 /msg jammer auth <token>
 
Now simply join the channel the bot is in and start talking and all messages will be cross-posted to Yammer.
