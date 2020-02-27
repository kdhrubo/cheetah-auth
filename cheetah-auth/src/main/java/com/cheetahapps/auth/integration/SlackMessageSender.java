package com.cheetahapps.auth.integration;

import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Component;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SlackMessageSender {
	
	private final String url = "https://hooks.slack.com/services/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";
	
	public WebhookResponse send(String message) {
		Payload payload = Payload.builder().text(message)
				.build();

		Slack slack = Slack.getInstance();
		try {
			WebhookResponse response = slack.send(url, payload);
			log.debug("Response  - {} | {}", response.getBody() , response.getCode());
			
			return response;
		} catch (Exception e) {
			log.error("Error", e);
			throw new RemoteAccessException("Error sending slack message", e);
		}
	}
}
