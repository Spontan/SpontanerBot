package twitch;


import modules.Module;

/**
 * Delegates responsibilities for exchanging information to api and irc connections.
 * TODO: refactor event listener structure
 */

public interface TwitchConnection {

    void registerListeningModule(Module module);

    void deregisterListeningModule(Module module);

    void sendChatMessage(MessagingChannel channel, String message);
}
