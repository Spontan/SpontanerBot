package twitch.chat.messages;

import twitch.ChannelTo;
import twitch.User;

import java.util.regex.Pattern;

/**
 * Contains parsed information on received messages from the chat server.
 */

public class ChatServerMessage{
    static private String serverAddress = "tmi.twitch.tv";

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

    public ChatMessageTo extractChatMessage(){
        if(messageType != ChatServerMessageType.CHAT_MESSAGE)
            throw new IllegalArgumentException("This server message does not contain a chat message");
        if(source == null)
            throw new IllegalArgumentException("No source was provided for this chat message");

        ChatMessageTo result = new ChatMessageTo();

        if(target == null)
            target = "";
        if(messageText == null)
            messageText = "";

        result.setUser(extractSender());
        result.setMessage(messageText.substring(1));
        result.setChannel(extractTarget());

        return result;
    }

    private User extractSender(){
        // possible patterns: :username!username@username.serveraddress, :username.serveraddress, :serveraddress
        String source = this.source;

        source = source.split("!")[0];  //:username, :username.serveraddress, :serveraddress
        source = source.split("." + serverAddress)[0]; //:username, :serveraddress
        source = source.substring(1);

        User user = new User(source);

        return user;
    }

    private ChannelTo extractTarget(){
        String target = this.target;
        ChannelTo result = new ChannelTo();

        if(target.startsWith("#")){
            result.setName(target.substring(1));
            return result;
        }else{
            throw new UnsupportedOperationException("Private messages are not yet implemented");
        }
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

    public static void setServerAddress(String address){
        serverAddress = address;
    }
}
