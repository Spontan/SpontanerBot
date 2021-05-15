package modules;

import twitch.TwitchEvent;
import twitch.chat.ChatEvent;
import twitch.chat.messages.ChatMessageEvent;

import java.util.List;

/**
 * not sure what it should do yet, probably Module is registered as a listener to twitch events
 * then implements methods for the event types it needs to be informed about.
 * New Modules would only need to implement the event listeners for things they actually need.
 * Not sure yet how to make it so event listeners are only registered for the implemented methods.
 * Maybe have a list of Event classes the module wants to be informed about?
 *
 * TODO: Make extension of this interface to handle access requirements to chat etc.?
 */
public interface Module {

    List<Class<? extends TwitchEvent>> getRequiredEvents();

    /**
     * Called when chat message is posted in one of joined channels
     */
    default void onChatMessage(ChatMessageEvent e){ }

    /**
     * Called when connection with chat server is established
     */
    default void onChatServerConnect(ChatEvent e){ }

    /**
     * called when joining a channel
     */
    default void onChannelJoin(ChatEvent e){ }

    /**
     * called when leaving a channel
     */
    default void onChannelPart(ChatEvent e){ }
}
