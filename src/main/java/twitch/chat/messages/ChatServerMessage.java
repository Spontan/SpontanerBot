package twitch.chat.messages;

import twitch.ChannelTo;

/**
 * Contains parsed information on received messages from the chat server.
 */

public class ChatServerMessage{

    private ChatServerMessageType messageType;
    private String source;
    private ChatMessageTo chatMessage;
    private int responseCode;
    private String target;
    private String messageText;


    public ChatServerMessageType getMessageType(){
        return messageType;
    }


    public ChatMessageTo getChatMessage() {
        return chatMessage;
    }

    public void setMessageType(ChatServerMessageType messageType) {
        this.messageType = messageType;
    }

    public void setChatMessage(ChatMessageTo chatMessage) {
        this.chatMessage = chatMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString(){
        return String.format("[source: %s, identifier: %s, target: %s, message: %s", source, messageType.name(), target, messageText);
    }
}
