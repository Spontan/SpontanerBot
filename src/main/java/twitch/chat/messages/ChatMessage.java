package twitch.chat.messages;

import twitch.MessagingChannel;
import twitch.User;

public class ChatMessage {

    //TODO: This should serve to transport identifiers instead of all information
    // Therefore replace user and channel with names only
    private User sender;
    private String message;
    private MessagingChannel target;  //TODO: Message targets can also be users. Make MessageTarget interface
    private boolean isPrivateMessage = false;
    private static String botCommandPrefix = "!";

    public boolean isBotCommand(){
        return message.startsWith(botCommandPrefix);
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessagingChannel getTarget() {
        return target;
    }

    public void setTarget(MessagingChannel target) {
        this.target = target;
    }

    public boolean isPrivateMessage() {
        return isPrivateMessage;
    }

    public void setPrivateMessage(boolean privateMessage) {
        isPrivateMessage = privateMessage;
    }

    public static String getBotCommandPrefix() {
        return botCommandPrefix;
    }

    public static void setBotCommandPrefix(String botCommandPrefix) {
        ChatMessage.botCommandPrefix = botCommandPrefix;
    }

    @Override
    public String toString(){
        return String.format("[source: %s, target: %s, content: \"%s\"]", sender.getName(), target.getTargetAddress(), message);
    }
}
