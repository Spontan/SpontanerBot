package twitch.chat.messages;

import twitch.ChannelTo;
import twitch.User;

public class ChatMessageTo {

    //TODO: This should serve to transport identifiers instead of all information
    // Therefore replace user and channel with names only
    private User user;
    private String message;
    private ChannelTo channel;  //TODO: Message targets can also be users. Make MessageTarget interface
    private static String botCommandPrefix = "!";

    public boolean isBotCommand(){
        return message.startsWith(botCommandPrefix);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChannelTo getChannel() {
        return channel;
    }

    public void setChannel(ChannelTo channel) {
        this.channel = channel;
    }

    public static String getBotCommandPrefix() {
        return botCommandPrefix;
    }

    public static void setBotCommandPrefix(String botCommandPrefix) {
        ChatMessageTo.botCommandPrefix = botCommandPrefix;
    }

    @Override
    public String toString(){
        return String.format("[source: %s, target: %s, content: \"%s\"]", user.getName(), channel.getName(), message);
    }
}
