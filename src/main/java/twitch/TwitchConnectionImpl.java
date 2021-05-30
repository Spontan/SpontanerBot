package twitch;

import configuration.StartConfiguration;
import configuration.StartConfiguration.StartConfigurationParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitch.chat.ChatConnection;
import twitch.chat.ChatConnectionFactory;
import twitch.chat.messages.ChatMessageEvent;
import twitch.chat.messages.ChatServerMessage;
import modules.Module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitchConnectionImpl implements TwitchConnection{
    private ChatConnection chatConnection;
    private List<Module> registeredModules;
    private static Logger logger = LoggerFactory.getLogger(TwitchConnectionImpl.class);
    private ChannelTo mainChannel;
    private Thread eventProcessingThread;

    public TwitchConnectionImpl(StartConfiguration config){
        this.chatConnection = ChatConnectionFactory.getChatConnection(config);
        registeredModules = new ArrayList<>();
        mainChannel = new ChannelTo();
        mainChannel.setName(config.getParameterValue(StartConfigurationParameters.CHANNEL));
        try {
            chatConnection.connect();
        } catch (IOException e) {
            logger.error("Could not connect to IRC server");
        }
        chatConnection.joinChannel(mainChannel);
        startEventThread();
    }

    //TODO: make processing thread class
    private void startEventThread(){
        eventProcessingThread = new Thread(){
            int waitForNewMessage = 100;
            synchronized public void run() {
                while (!isInterrupted()) {
                    boolean hasProcessed = false;
                    hasProcessed = hasProcessed || processServerMessage();

                    if (!hasProcessed) {
                        try {
                            wait(waitForNewMessage);
                        } catch (InterruptedException e) {
                            interrupt();
                        }
                    }
                }
            }
        };
        eventProcessingThread.start();
    }

    synchronized private boolean processServerMessage(){
        if(!chatConnection.isMessageInQueue())
            return false;

        ChatServerMessage message = chatConnection.popServerMessage();
        switch(message.getMessageType()){
            case ERROR:
                startReconnect();
                break;
            case CHAT_MESSAGE:
                ChatMessageEvent e = makeChatMessageEvent(message);
                informListeners(e);
                break;
        }

        return true;
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
    public void sendChatMessage(MessagingChannel channel, String message) {
        chatConnection.sendChatMessage(channel.getTargetAddress(), message);
    }

    //TODO: compare channel and user names from the server message to the current user list and list of connected channels
    //  and use those in the ChatMessage object instead of generating new ones
    private ChatMessageEvent makeChatMessageEvent(ChatServerMessage serverMessage){
        return new ChatMessageEvent(serverMessage.extractChatMessage());
    }

    private void informListeners(TwitchEvent e){
        logger.info("Informing Modules");
        if(e instanceof ChatMessageEvent)
            registeredModules.forEach(module -> module.onChatMessage((ChatMessageEvent) e));
    }

    private void startReconnect(){
        if(chatConnection.isConnected())
            chatConnection.disconnect();
        try {
            chatConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
