package com.halisson.iadoc_document.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ConfigChat {

	//@Bean
	//TODO REMOVER
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
        		.defaultSystem("Você é uma amigável chat bot que responde as questões português usando somente os documentos que foi te informado")
                .build();
    }
}
