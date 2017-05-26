package com.bodansky.videochat.config;

import com.bodansky.videochat.WebRTCVideoChatApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebRTCVideoChatApplication.class);
	}

}
