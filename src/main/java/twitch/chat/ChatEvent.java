package twitch.chat;

import twitch.MessagingChannel;
import twitch.TwitchEvent;

public class ChatEvent implements TwitchEvent {
    private MessagingChannel channel;

    public ChatEvent(MessagingChannel channel){
        this.channel = channel;
    }

    public MessagingChannel getChannel(){
        return channel;
    }
}
