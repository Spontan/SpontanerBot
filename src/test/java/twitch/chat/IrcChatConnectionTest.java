package twitch.chat;

import configuration.StartConfiguration;
import org.junit.Before;
import org.junit.Test;
import twitch.chat.messages.ChatServerMessage;
import twitch.chat.messages.ChatServerMessageType;
import twitch.chat.messages.handlers.ChatServerMessageHandler;

import java.io.IOException;

public class IrcChatConnectionTest {
    IrcChatConnection ircChatConnection;

    String token;
    String nick;

    @Before
    public void init(){
        StartConfiguration testConfig = new StartConfiguration("test_config.ini");
        token = testConfig.getParameterValue(StartConfiguration.StartConfigurationParameters.TMI_TOKEN);
        nick = testConfig.getParameterValue(StartConfiguration.StartConfigurationParameters.BOT_NICK);
        String serverUrl = "irc.chat.twitch.tv";
        int serverPort = 6667;

        ircChatConnection = new IrcChatConnection(serverUrl, serverPort);
        ircChatConnection.setPassword(token);
        ircChatConnection.setNickName(nick);
    }

    private boolean responseReceived = false;

    @Test
    synchronized public void connectToServerSuccess() throws InterruptedException, IOException {
        //given
        responseReceived = false;
        ircChatConnection.registerServerMessageHandler(new ChatServerMessageHandler(ChatServerMessageType.UNKNOWN) {
            @Override
            public void handle(ChatServerMessage message) {
                received();
            }
        });

        //when
        ircChatConnection.connect();
        wait(1000);

        //then
        assert(responseReceived);
    }

    private void received(){
        responseReceived=true;
    }
}
