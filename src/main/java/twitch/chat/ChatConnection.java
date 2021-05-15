package twitch.chat;

import twitch.ChannelTo;
import twitch.chat.messages.ChatMessageTo;
import twitch.chat.messages.handlers.ChatMessageHandler;
import twitch.chat.messages.handlers.ChatServerMessageHandler;

import java.io.IOException;
import java.util.List;

public interface ChatConnection {

    void setPassword(String password);

    void setNickName(String nickName);

    void connect() throws IOException;

    void disconnect();

    void joinChannel(ChannelTo channel);

    void partChannel(ChannelTo channel);

    List<ChannelTo> getJoinedChannels();

    void sendPing();

    void sendPong();

    void sendChatMessage(ChannelTo channel, String message); //TODO: less logic here

    boolean isConnected();

    void registerServerMessageHandler(ChatServerMessageHandler handler);

    void registerChatMessageHandler(ChatMessageHandler handler);

    void informChatMessageHandlers(ChatMessageTo message); //TODO: remove again when server messages are handled internally
}
