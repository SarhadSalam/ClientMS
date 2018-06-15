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
	public static void main(String args[]) throws Exception
	{
		System.out.println(System.getProperty("user.dir")+"/print_dump/invoice_"+System.currentTimeMillis()+".pdf");
	}
}