package twitch.chat;

import twitch.ChannelTo;
import twitch.TwitchEvent;

public class ChatEvent implements TwitchEvent {
    private ChannelTo channel;

    public ChatEvent(ChannelTo channel){
        this.channel = channel;
    }

    public ChannelTo getChannel(){
        return channel;
    }
}
