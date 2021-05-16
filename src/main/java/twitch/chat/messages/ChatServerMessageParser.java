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


        String target = "";
        String messageText = "";
        String source = splits[0];
        if(splits.length<2){
            result.setMessageType(ChatServerMessageType.UNKNOWN);
            return result;
        }

        String identifier = splits[1];
        try {
            int responseCode = Integer.parseInt(identifier);
            if(splits.length<4){
                logger.error(String.format("Received Response with code %d but contained no information.\n" +
                        "Message: \"%s\"", responseCode, message));
                result.setMessageType(ChatServerMessageType.UNKNOWN);
                return result;
            }

            result.setMessageType(ChatServerMessageType.RESPONSE);
            result.setResponseCode(responseCode);
            result.setMessageText(splits[3]);
            return result;
        }catch(NumberFormatException e){
            logger.debug(String.format("message identifier \"%s\" is not a response code", identifier));
        }


        if(splits.length>2)
            target = splits[2];
        if(splits.length>3)
            messageText = splits[3];


        result.setSource(source);
        result.setTarget(target);
        result.setMessageText(messageText);



        if(identifier.equalsIgnoreCase("privmsg")){
            result.setMessageType(ChatServerMessageType.CHAT_MESSAGE);
            return result;
        }

        result.setMessageType(ChatServerMessageType.UNKNOWN);
        return result;
    }
}
