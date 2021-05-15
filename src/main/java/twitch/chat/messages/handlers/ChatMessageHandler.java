package twitch.chat.messages.handlers;

import twitch.chat.messages.ChatMessageTo;

public abstract class ChatMessageHandler {

    abstract public void handle(ChatMessageTo chatMessage);
}
