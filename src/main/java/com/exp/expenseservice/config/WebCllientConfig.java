package com.exp.expenseservice.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebCllientConfig {
	
	@Value("${app.microservice.url}")
	private String categoryMicroServiceEndpoint;

	@Bean
	WebClient getWebClient() {

		HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10))
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000).option(ChannelOption.SO_KEEPALIVE, true);

		return WebClient.builder().baseUrl(categoryMicroServiceEndpoint)
				.clientConnector(new ReactorClientHttpConnector(httpClient)).build();

	}

}
