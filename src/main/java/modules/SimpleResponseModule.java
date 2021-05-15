package modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitch.TwitchConnection;
import twitch.TwitchEvent;
import twitch.chat.IrcChatConnection;
import twitch.chat.messages.ChatMessageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleResponseModule implements Module{
    private static final Logger logger = LoggerFactory.getLogger(SimpleResponseModule.class);

    private List<Class<? extends TwitchEvent>> listenToEvents;
    private TwitchConnection twitchConnection;
    private Map<String, String> messageToResponse;

    public SimpleResponseModule(TwitchConnection twitchConnection){
        listenToEvents = new ArrayList<>();
        listenToEvents.add(ChatMessageEvent.class);
        this.twitchConnection = twitchConnection;

        messageToResponse = new HashMap<>();
    }

    public void putResponse(String message, String response){
        messageToResponse.put(message, response);
    }

    public void removeResponseForMessage(String message){
        messageToResponse.remove(message);
    }

    @Override
    public List<Class<? extends TwitchEvent>> getRequiredEvents() {
        return listenToEvents;
    }

    @Override
    public void onChatMessage(ChatMessageEvent e) {
        logger.info("onChatMessage");
        String response = messageToResponse.get(e.getText());
        if(response != null && e.getChannel() != null)
            twitchConnection.sendChatMessage(e.getChannel(), response);
    }
}
