package twitch.chat;

import twitch.chat.messages.ChatServerMessageType;
import twitch.chat.messages.handlers.ChatServerMessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ChatServerMessageHandlerMap{

    Map<ChatServerMessageType, List<ChatServerMessageHandler>> messageTypeToHandlerList;

    public ChatServerMessageHandlerMap(){
        this.messageTypeToHandlerList = new HashMap<>();
        Stream.of(ChatServerMessageType.values())
                .forEach(type -> messageTypeToHandlerList.put(type, new ArrayList<>()));
    }

    public void put(ChatServerMessageHandler messageHandler){
        messageTypeToHandlerList.get(messageHandler.getHandledMessageType()).add(messageHandler);
    }

    public List<ChatServerMessageHandler> getHandlerForMessageType(ChatServerMessageType messageType){
        return messageTypeToHandlerList.get(messageType);
    }
}
