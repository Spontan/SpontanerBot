package twitch.chat.messages;

import twitch.User;
import twitch.chat.ChatEvent;

public class ChatMessageEvent extends ChatEvent {
    private User user;
    private String text;

    public ChatMessageEvent(@org.jetbrains.annotations.NotNull ChatMessageTo message) {
        super(message.getChannel());
        this.user = message.getUser();
        this.text = message.getMessage();
        System.out.println(message);
    }

    public User getUser(){
        return user;
    }

    public String getText() {
        return text;
    }
}
