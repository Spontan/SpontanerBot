package twitch.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitch.ChannelTo;
import twitch.chat.messages.ChatMessageTo;
import twitch.chat.messages.ChatServerMessage;
import twitch.chat.messages.ChatServerMessageParser;
import twitch.chat.messages.handlers.ChatMessageHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class IrcChatConnection extends AbstractChatConnection {
    private static final Logger logger = LoggerFactory.getLogger(IrcChatConnection.class);

    private String serverUrl = "irc.chat.twitch.tv"; // optional replacement with config param later
    private int serverPort = 6667;

    private Socket ircSocket;
    private BufferedWriter outputWriter;
    private BufferedReader inputReader;
    private Thread readerThread;

    private List<ChatMessageHandler> chatMessageHandlers;


    public IrcChatConnection(String serverUrl, int serverPort){
        this.serverUrl = serverUrl;
        this.serverPort = serverPort;
        this.joinedChannels = new ArrayList<>();
        this.chatMessageHandlers = new ArrayList<>();
    }

    private void setupConnection() throws IOException {
        //TODO: extract server connection to different class so it can be mocked easily
        ircSocket = new Socket(serverUrl, serverPort);
        outputWriter = new BufferedWriter(new OutputStreamWriter(ircSocket.getOutputStream()));
        inputReader = new BufferedReader(new InputStreamReader(ircSocket.getInputStream()));
        startReaderThread();
    }

    private void startReaderThread() {
        readerThread = new Thread(){
            int waitForNewMessage = 100;

            @Override
            synchronized public void run() {
                logger.info("Start listening");
                while(!this.isInterrupted()){
                    if(!readAndProcessLineFromInputStream())
                        waitForNewMessage();
                }
                logger.info("Stop listening");
            }

            private void waitForNewMessage() {
                try {
                    wait(waitForNewMessage);
                } catch (InterruptedException e) {
                    this.interrupt();
                }
            }
        };
        readerThread.start();
    }

    private boolean readAndProcessLineFromInputStream(){
        try {
            String line = inputReader.readLine();

            logger.info("<< " + line);
            if(line==null)
                return false;

            ChatServerMessage message = ChatServerMessageParser.parseServerMessage(line);
            //logger.debug(message.toString());
            informMessageHandlers(message);
        } catch (IOException e){
            logger.error("Something went wrong when trying to receive message from IRC server");
            e.printStackTrace();
            readerThread.interrupt();
            //TODO: add reconnect procedure
        }
        return true;
    }

    @Override
    protected void sendServerMessage(String message){
        try {
            doSendMessage(message);
        } catch (IOException e) {
            logger.error("Something went wrong when trying to send message to IRC server");
            e.printStackTrace();
            //TODO: add retry procedure
        }
    }

    private synchronized void doSendMessage(String message) throws IOException{
        logger.info(">> " + message);
        outputWriter.write(message + "\r\n");
        outputWriter.flush();
    }

    @Override
    public void connect() throws IOException {
        setupConnection();
        sendServerMessage("PASS " + password);
        sendServerMessage("NICK "+ nickName);
    }

    @Override
    public void disconnect() {
        sendServerMessage("QUIT");
        readerThread.interrupt();
    }

    @Override
    public void joinChannel(ChannelTo channel) {
        sendServerMessage("JOIN #" + channel.getName().toLowerCase());
        //adding channel to joined channel list is done on positive response from server
    }

    @Override
    public void partChannel(ChannelTo channel) {
        sendServerMessage("PART " + channel.getName());
    }

    @Override
    public List<ChannelTo> getJoinedChannels() {
        return joinedChannels;
    }

    @Override
    public void sendChatMessage(ChannelTo channel, String message) {
        sendServerMessage("PRIVMSG #" + channel.getName().toLowerCase() + " :" + message);
    }

    @Override
    public void registerChatMessageHandler(ChatMessageHandler handler) {
        chatMessageHandlers.add(handler);
    }

    @Override
    public void informChatMessageHandlers(ChatMessageTo message) {
         chatMessageHandlers.forEach(handler -> handler.handle(message));
    }

    //TODO: deregister Handlers
}
