package twitch.chat;

import configuration.StartConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitch.*;

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

    protected void sendPong(){
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
}
