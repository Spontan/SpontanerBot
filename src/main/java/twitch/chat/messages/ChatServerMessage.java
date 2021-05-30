package twitch.chat.messages;

import twitch.ChannelTo;
import twitch.MessagingChannel;
import twitch.User;

/**
 * Contains parsed information on received messages from the chat server.
 */

public class ChatServerMessage{
    static private String serverAddress = "tmi.twitch.tv";

    private ChatServerMessageType messageType;
    private String source;
    private ChatMessage chatMessage;
    private int responseCode;
    private String target;
    private String messageText;


    public ChatServerMessageType getMessageType(){
        return messageType;
    }


    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setMessageType(ChatServerMessageType messageType) {
        this.messageType = messageType;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatMessage extractChatMessage(){
        if(messageType != ChatServerMessageType.CHAT_MESSAGE)
            throw new IllegalArgumentException("This server message does not contain a chat message");
        if(source == null)
            throw new IllegalArgumentException("No source was provided for this chat message");

        ChatMessage result = new ChatMessage();

        if(target == null)
            target = "";
        if(messageText == null)
            messageText = "";

        result.setSender(extractSender());
        result.setMessage(messageText.substring(1));
        MessagingChannel messageTarget = extractTarget();

        if(messageTarget instanceof User)
            result.setPrivateMessage(true);
        result.setTarget(messageTarget);



        return result;
    }

    private User extractSender(){
        // possible patterns: :username!username@username.serveraddress, :username.serveraddress, :serveraddress
        String sender = this.source;

        sender = sender.split("!")[0];  //:username, :username.serveraddress, :serveraddress
        sender = sender.split("." + serverAddress)[0]; //:username, :serveraddress
        sender = sender.substring(1);

        return new User(sender);
    }

    private MessagingChannel extractTarget(){
        if(target.startsWith("#")){
            ChannelTo channel = new ChannelTo();
            channel.setName(target.substring(1));
            return channel;
        }
        return new User(target);
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
