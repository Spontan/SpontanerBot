package twitch.chat.messages.handlers;

import twitch.chat.messages.ChatMessage;

public abstract class ChatMessageHandler {

    abstract public void handle(ChatMessage chatMessage);
}
