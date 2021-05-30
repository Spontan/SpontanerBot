package twitch.chat.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatServerMessageParser {
    private static final Logger logger = LoggerFactory.getLogger(ChatServerMessageParser.class);

    public static ChatServerMessage parseServerMessage(String message){
        ChatServerMessage result = new ChatServerMessage();

        String[] splits = message.split(" ", 4);

        if(splits[0].equalsIgnoreCase("ping")){
            result.setMessageType(ChatServerMessageType.PING);
            return result;
        }

        if(splits[0].equalsIgnoreCase("pong")){
            result.setMessageType(ChatServerMessageType.PONG);
            return result;
        }


        String target = "";
        String messageText = "";
        String source = splits[0];
        if(splits.length<2){
            result.setMessageType(ChatServerMessageType.OTHER);
            return result;
        }

        String identifier = splits[1];

        if(identifier.equalsIgnoreCase("privmsg")){
            if(splits.length<4) {
                result.setMessageType(ChatServerMessageType.ERROR);
                return result;
            }
            target = splits[2];
            messageText = splits[3];


            result.setSource(source);
            result.setTarget(target);
            result.setMessageText(messageText);
            result.setMessageType(ChatServerMessageType.CHAT_MESSAGE);
            return result;
        }


        result.setMessageType(ChatServerMessageType.OTHER);
        return result;
    }
}
