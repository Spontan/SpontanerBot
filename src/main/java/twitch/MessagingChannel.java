package twitch;

/**
 * A channel through which chat messages can be sent. Distinct from a user's streaming {@link ChannelTo}.
 */
public interface MessagingChannel {

    /**
     * Provides an identifier which can be used to send a chat message to this target.
     * @return
     */
    String getTargetAddress();
}
