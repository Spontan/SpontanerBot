package twitch.chat;

import configuration.StartConfiguration;
import configuration.StartConfiguration.StartConfigurationParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitch.ChannelTo;
import twitch.chat.messages.ChatMessageTo;
import twitch.chat.messages.ChatServerMessage;
import twitch.chat.messages.ChatServerMessageType;
import twitch.chat.messages.handlers.ChatServerMessageHandler;


public class ChatConnectionFactory {
    private static Logger logger = LoggerFactory.getLogger(ChatConnectionFactory.class);

    public static ChatConnection getChatConnection(StartConfiguration config){
        ChatConnection chatConnection;

        String serverUrl = config.getParameterValue(StartConfigurationParameters.CHAT_SERVER_URL);
        String serverPort = config.getParameterValue(StartConfigurationParameters.CHAT_SERVER_PORT);
        String token = config.getParameterValue(StartConfigurationParameters.TMI_TOKEN);
        String nick = config.getParameterValue(StartConfigurationParameters.BOT_NICK);

        switch(serverUrl){
            case "irc.chat.twitch.tv":
                chatConnection = getIrcChatConnection(serverUrl, Integer.valueOf(serverPort));
                break;
            default:
                logger.warn("Provided Server URL does not match any supported connection type, using default instead");
                String defaultServerUrl = StartConfigurationParameters.CHAT_SERVER_URL.getDefault();
                String defaultServerPort = StartConfigurationParameters.CHAT_SERVER_PORT.getDefault();
                chatConnection = getIrcChatConnection(defaultServerUrl, Integer.valueOf(defaultServerPort));
        }

        chatConnection.setPassword(token);
        chatConnection.setNickName(nick);
        registerHandlers(chatConnection);

        return chatConnection;
    }

    private static void registerHandlers(ChatConnection chatConnection) {
        chatConnection.registerServerMessageHandler(new ChatServerMessageHandler(ChatServerMessageType.PING) {
            @Override
            public void handle(ChatServerMessage message) {
                if(message.getMessageType() == ChatServerMessageType.PING)
                    chatConnection.sendPong();

            }
        });
        chatConnection.registerServerMessageHandler(new ChatServerMessageHandler(ChatServerMessageType.CHAT_MESSAGE) {
            @Override
            public void handle(ChatServerMessage message) {
                if(message.getMessageType() == ChatServerMessageType.CHAT_MESSAGE) {
                    chatConnection.informChatMessageHandlers(message.extractChatMessage());
                }
            }
        });


    }

    //TODO: think if i'll need it later, otherwise remove
    private static IrcChatConnection getIrcChatConnection(String serverUrl, Integer serverPort) {
        return new IrcChatConnection(serverUrl, serverPort);
    }
}
