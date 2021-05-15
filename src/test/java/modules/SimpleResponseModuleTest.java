package modules;

import org.junit.Before;
import org.junit.Test;
import twitch.ChannelTo;
import twitch.TwitchConnection;
import twitch.chat.messages.ChatMessageTo;
import twitch.chat.messages.ChatMessageEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;

public class SimpleResponseModuleTest {

    SimpleResponseModule simpleResponseModule;

    TwitchConnection twitchConnection = mock(TwitchConnection.class);

    ChannelTo channel;

    @Before
    public void init(){
        simpleResponseModule = new SimpleResponseModule(twitchConnection);
        channel = new ChannelTo();
        channel.setName("Channel");
    }

    @Test
    public void succeedRespondingToRegisteredPattern(){
        //given
        String message = "Message";
        String response = "Response";
        simpleResponseModule.putResponse(message, response);
        ChatMessageEvent event = new ChatMessageEvent(makeChatMessage("Message"));

        //when
        simpleResponseModule.onChatMessage(event);

        //then
        verify(twitchConnection).sendChatMessage(channel, response);
    }

    @Test
    public void failRespondingToUnregisteredPattern(){
        //given
        String message = "Message2";
        String response = "Response";
        simpleResponseModule.putResponse(message, response);
        ChatMessageEvent event = new ChatMessageEvent(makeChatMessage("Message"));

        //when
        simpleResponseModule.onChatMessage(event);

        //then
        verifyNoInteractions(twitchConnection);
    }

    @Test
    public void failRespondingAfterPatternRemoval(){
        //given
        String message = "Message";
        String response = "Response";
        simpleResponseModule.putResponse(message, response);
        ChatMessageEvent event = new ChatMessageEvent(makeChatMessage("Message"));
        simpleResponseModule.removeResponseForMessage("Message");

        //when
        simpleResponseModule.onChatMessage(event);
        //then
        verifyNoInteractions(twitchConnection);
    }

    private ChatMessageTo makeChatMessage(String message){
        ChatMessageTo chatMessage = new ChatMessageTo();
        chatMessage.setMessage(message);
        chatMessage.setChannel(channel);

        return chatMessage;
    }
}
