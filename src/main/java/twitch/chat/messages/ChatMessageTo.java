package twitch.chat.messages;

import twitch.ChannelTo;
import twitch.User;

public class ChatMessageTo {
    private User user;
    private String message;
    private ChannelTo channel;
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
}
