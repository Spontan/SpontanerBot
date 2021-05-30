import configuration.StartConfiguration;
import configuration.StartConfiguration.StartConfigurationParameters;
import modules.SimpleResponseModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitch.TwitchConnection;
import twitch.TwitchConnectionImpl;
import twitch.User;
import twitch.chat.ChatConnectionFactory;
import twitch.chat.ChatConnection;
import twitch.webservices.tmi.TwitchTmiClient;
import webservices.WebServerManager;

import java.io.IOException;
import java.util.Collection;


public class Main {
    private static StartConfiguration config;
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static WebServerManager wsManager;

    public static void main(String[] args) {
        config = new StartConfiguration();
        //startIrcConsole();
        //testDB();
        //testChatConnection();
        testTwitchConnection();
        //testWebServer();
        //testGetChatters();
    }

    private static void testGetChatters() {
        TwitchTmiClient tmiClient = new TwitchTmiClient();
        Collection<User> currentUsers = tmiClient.getChatters();

        currentUsers.forEach(user -> System.out.println(user.getName()));
    }

    private static void testWebServer() {
        wsManager = new WebServerManager();
        wsManager.startServer(8080);
    }

    private static void testTwitchConnection() {
        TwitchConnection twitchConnection = new TwitchConnectionImpl(config);
        SimpleResponseModule responseModule = new SimpleResponseModule(twitchConnection);
        responseModule.putResponse("ping", "pong");
        twitchConnection.registerListeningModule(responseModule);
    }

    private static void startIrcConsole(){
       twitch.IrcSenderSimple.test(
               config.getParameterValue(StartConfigurationParameters.BOT_NICK), config.getParameterValue(StartConfigurationParameters.TMI_TOKEN));
   }

   private static void testDB(){
       persistence.ExampleDB.testDB();
   }

   private static void testChatConnection(){
        ChatConnection chatConnection = ChatConnectionFactory.getChatConnection(config);
        try {
            chatConnection.connect();
        } catch (IOException e) {
            logger.error("Could not connect to IRC server");
        }
   }
}
