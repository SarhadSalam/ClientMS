package dev;

import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class scratch
{
	
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "465";
	private static final String emailMsgTxt = "Test Message Contents";
	private static final String emailSubjectTxt = "A test from gmail";
	private static final String emailFromAddress = "";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private static final String sendTo = "sarhadmaisoon18@gmail.com";
	
	public static void main(String args[]) throws Exception
	{
	}
}