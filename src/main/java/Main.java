import configuration.StartConfiguration;
import configuration.StartConfiguration.StartConfigurationParameters;
import modules.SimpleResponseModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitch.TwitchConnection;
import twitch.TwitchConnectionImpl;
import twitch.chat.ChatConnectionFactory;
import twitch.chat.ChatConnection;
import twitch.chat.ChatTestInterface;
import twitch.chat.IrcChatConnection;

import java.io.IOException;


public class Main {
    private static StartConfiguration config;
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        config = new StartConfiguration();
        //testIRC();
        //testDB();
        //testChatConnection();
        testTwitchConnection();
    }

    private static void testTwitchConnection() {
        TwitchConnection twitchConnection = new TwitchConnectionImpl(config);
        SimpleResponseModule responseModule = new SimpleResponseModule(twitchConnection);
        responseModule.putResponse("ping", "pong");
        twitchConnection.registerListeningModule(responseModule);
    }

    private static void testIRC(){
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
