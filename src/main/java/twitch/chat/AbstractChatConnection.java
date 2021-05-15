package twitch.chat;

import configuration.StartConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitch.*;
import twitch.chat.messages.ChatServerMessage;
import twitch.chat.messages.ChatServerMessageType;
import twitch.chat.messages.handlers.ChatServerMessageHandler;

import java.io.IOException;
import java.nio.channels.NotYetConnectedException;
import java.util.List;

public abstract class AbstractChatConnection implements ChatConnection {
    protected static Logger logger = LoggerFactory.getLogger(AbstractChatConnection.class);

    protected boolean connected = false;
    protected List<ChannelTo> joinedChannels;
    private static ChatConnection instance;
    private boolean connectAutomatically = true;
    protected String password;
    protected String nickName;


    private ChatServerMessageHandlerMap messageHandlers = new ChatServerMessageHandlerMap();

    public static ChatConnection getInstance(StartConfiguration config){
        if(instance == null)
            instance = ChatConnectionFactory.getChatConnection(config);
        return instance;
    }

    protected abstract void sendServerMessage(String message);

    @Override
    public void sendPing(){
        sendServerMessage("ping");
    }

    @Override
    public void sendPong(){
        sendServerMessage("pong");
    }

    public void sendMessage(String message, ChannelTo channel){
        if(!connected){
            if(!connectAutomatically)   throw new NotYetConnectedException();

            try {
                connect();
            } catch (IOException e) {
                logger.error("Could not connect to IRC server");
            }
        }
    }


    @Override
    public boolean isConnected(){
        return connected;
    }

    public void setConnectAutomatically(boolean connectAutomatically){
        this.connectAutomatically = connectAutomatically;
    }

    @Override
    public void setPassword(String password){
        if(isConnected())
            throw new IllegalStateException("Cannot change password while connection is open");
        this.password = password;
    }

    @Override
    public void setNickName(String nickName){
        if(isConnected())
            throw new IllegalStateException("Cannot change password while connection is open");
        this.nickName = nickName;
    }

    @Override
    public void registerServerMessageHandler(ChatServerMessageHandler handler){
        messageHandlers.put(handler);
    }

    protected void informMessageHandlers(ChatServerMessage message){
        ChatServerMessageType messageType = message.getMessageType();
        List<ChatServerMessageHandler> handlersForMessage = messageHandlers.getHandlerForMessageType(messageType);

        handlersForMessage.forEach(handler -> handler.handle(message));
    }
}
