package twitch.chat.messages.handlers;

import twitch.chat.messages.ChatServerMessageType;
import twitch.chat.messages.ChatServerMessage;

//TODO: remove this. Outside of chat connection there is no need to have access to the server messages.

public abstract class ChatServerMessageHandler {
    private ChatServerMessageType messageType;

    public ChatServerMessageHandler(ChatServerMessageType messageType){
        this.messageType = messageType;
    }

    public ChatServerMessageType getHandledMessageType(){
        return messageType;
    }

    public abstract void handle(ChatServerMessage message);
}
