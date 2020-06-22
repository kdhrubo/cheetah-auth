package com.cheetahapps.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jodd.mail.MailServer;
import jodd.mail.SmtpServer;

@Configuration
public class SmtpConfiguration {
	@Value("${aws.ses.smtp.username}")
	private String smtpUser;
	@Value("${aws.ses.smtp.password}")
	private String smtpPassword;
	@Value("${aws.ses.smtp.server}")
	private String smtpServer;
	@Value("${aws.ses.smtp.port}")
	private int smtpPort;
	@Value("${aws.ses.smtp.useTls}")
	private boolean allowTls;
	
	@Bean
	public SmtpServer smtpServer() {
		return MailServer.create()
	            .ssl(allowTls).host(smtpServer)
	            .port(smtpPort)
	            .auth(smtpUser, smtpPassword)
	            .buildSmtpMailServer();
		
		
	}
}
