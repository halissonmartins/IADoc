package com.halisson.iadoc_question.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

class ConfigTest {

    @Test
    void testConfigChat_CreatesChatClient() {
        ConfigChat config = new ConfigChat();
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        
        // Mock the builder chain
        org.mockito.Mockito.when(builder.defaultSystem(org.mockito.ArgumentMatchers.anyString()))
            .thenReturn(builder);
        org.mockito.Mockito.when(builder.build())
            .thenReturn(mock(ChatClient.class));

        ChatClient chatClient = config.chatClient(builder);

        assertNotNull(chatClient);
    }
}
