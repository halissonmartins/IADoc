package com.halisson.iadoc_question.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigChat {

	@Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
        		.defaultSystem("Você é uma amigável chat bot que responde as questões em português usando a sessão DOCUMENTOS "
        				+ "que foram informados para fornecer respostas precisas.")
                .build();
    }
}
