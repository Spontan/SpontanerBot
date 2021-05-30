package twitch.chat;

import twitch.ChannelTo;
import twitch.chat.messages.ChatServerMessage;

import java.io.IOException;
import java.util.List;

public interface ChatConnection {

    /**
     * Sets the password to be used when connecting to the chat server. This should most likely be the oAuth token of
     * the bot.
     * @param password the password (oAuth token)
     */
    void setPassword(String password);

    /**
     * Sets the nick to be used when connecting to the server. Likely irrelevant, since the oAuth token already identifies the username.
     * @param nickName
     */
    void setNickName(String nickName);

    /**
     * Opens the connection to the server. {@link #setPassword(String) setPassword} should be called first.
     * @throws IOException
     */
    void connect() throws IOException;

    /**
     * Closes the server connection.
     */
    void disconnect();

    /**
     * Causes the chat client to join the specified {@link ChannelTo}, which allows the client to receive messages sent
     * to the channel.
     * @param channel
     */
    void joinChannel(ChannelTo channel);

    /**
     * Cancels the membership of the client in the specified {@link ChannelTo}.
     * @param channel
     */
    void partChannel(ChannelTo channel);

    /**
     * @return a list of {@link ChannelTo} the client is a member of
     */
    List<ChannelTo> getJoinedChannels();

    /**
     * Sends a PING command to the server.
     */
    void sendPing();

    /**
     * Sends a chat message to the specified {@link ChannelTo}.
     * @param target the target to which the message should be sent. Should be either a {@link twitch.User}'s username
     *               or "#" followed by a {@link ChannelTo}'s name
     * @param message
     */
    void sendChatMessage(String target, String message); //TODO: less logic here

    /**
     * @return whether or not the client is currently connected to the server
     */
    boolean isConnected();

    /**
     * @return whether or not there is a message from the server available to be processed
     */
    boolean isMessageInQueue();

    /**
     * Provides the oldest unprocessed {@link ChatServerMessage} and considers it "processed".
     * @return the oldest ChatServerMessage
     */
    ChatServerMessage popServerMessage();

    /**
     * Provides the oldest unprocessed {@link ChatServerMessage} but keeps it available for further processing, meaning
     * additional calls to either this or {@link #popServerMessage()} will return the same ChatServerMessage.
     * @return the oldest ChatServerMessage
     */
    ChatServerMessage peekServerMessage();
}
