package twitch;

import configuration.StartConfiguration;
import configuration.StartConfiguration.StartConfigurationParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitch.chat.ChatConnection;
import twitch.chat.ChatConnectionFactory;
import twitch.chat.messages.ChatMessageEvent;
import twitch.chat.messages.ChatMessageTo;
import twitch.chat.messages.handlers.ChatMessageHandler;
import modules.Module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitchConnectionImpl implements TwitchConnection{
    private ChatConnection chatConnection;
    private List<Module> registeredModules;
    private static Logger logger = LoggerFactory.getLogger(TwitchConnectionImpl.class);
    private ChannelTo mainChannel;

    public TwitchConnectionImpl(StartConfiguration config){
        this.chatConnection = ChatConnectionFactory.getChatConnection(config);
        chatConnection.registerChatMessageHandler(new ChatMessageHandler() {
            @Override
            public void handle(ChatMessageTo chatMessage) {
                informListeners(new ChatMessageEvent(chatMessage));
            }
        });
        registeredModules = new ArrayList<>();
        mainChannel = new ChannelTo();
        mainChannel.setName(config.getParameterValue(StartConfigurationParameters.CHANNEL));
        try {
            chatConnection.connect();
        } catch (IOException e) {
            logger.error("Could not connect to IRC server");
        }
        chatConnection.joinChannel(mainChannel);
    }

    @Override
    public void registerListeningModule(Module module) {
        registeredModules.add(module);
    }

    @Override
    public void deregisterListeningModule(Module module) {
        registeredModules.remove(module);
    }

    @Override
    public void sendChatMessage(ChannelTo channel, String message) {
        chatConnection.sendChatMessage(channel, message);
    }

    private void informListeners(TwitchEvent e){
        logger.info("Informing Modules");
        if(e instanceof ChatMessageEvent)
            registeredModules.forEach(module -> module.onChatMessage((ChatMessageEvent) e));
    }
}
