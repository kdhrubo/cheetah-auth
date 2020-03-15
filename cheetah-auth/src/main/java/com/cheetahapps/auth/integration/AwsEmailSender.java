package com.cheetahapps.auth.integration;



import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import jodd.mail.Email;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.template.MapTemplateParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class AwsEmailSender {

	
	private String from = "support@bongreads.com";
	private String fromName = "Bongreads";
	
	private final SmtpServer smtpServer;
	
	@Async
	public void send(String to, String subject, String templateName, Map<String,String> data) throws Exception{
		
		log.info("Trying to send email via AWS SES SMTP");
		String body = getMergedTemplate(templateName, data);
		log.info("Body - {}", body);
		Email email = Email.create()
				.from(fromName, from)
				.to(to)
				.replyTo(to)
				.subject(subject)
				.htmlMessage(body);
		
        try {
        log.info("Going ....");
        
		sendMail(email);
        
		log.info("Gone ....");
		
        }
        catch(Exception e) {
        	log.error("Error sending email - ", e);
        }
	}
	
	private void sendMail(Email email) {
		SendMailSession session = smtpServer.createSession();
	    session.open();
	    session.sendMail(email);
	    session.close();
	}
	
	private String getMergedTemplate(String templateName, Map<String,String> data) throws Exception{
		ClassPathResource resource = new ClassPathResource("/email/welcome.html");
		byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
        String templateTxt = new String(bdata, StandardCharsets.UTF_8);
        
        MapTemplateParser parser = new MapTemplateParser();
        
        return parser.parseWithMap(templateTxt, data);
	}
	

}


