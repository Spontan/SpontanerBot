package configuration;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public class StartConfiguration extends Configuration{

    public enum StartConfigurationParameters{
        TMI_TOKEN("TMI_TOKEN", ""),
        CLIENT_ID("CLIENT_ID", ""),
        BOT_NICK("BOT_NICK", ""),
        BOT_PREFIX("BOT_PREFIX", ""),
        CHANNEL("CHANNEL", ""),
        CHAT_SERVER_URL("CHAT_SERVER_URL", "irc.chat.twitch.tv"),
        CHAT_SERVER_PORT("CHAT_SERVER_PORT", "6667");


        final String name;
        final String defaultValue;

        StartConfigurationParameters(final String stringName, final String defaultValue) {
            this.name = stringName;
            this.defaultValue = defaultValue;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getName(){
            return name;
        }

        public String getDefault(){
            return defaultValue;
        }
    }

    private final String pathToConfig;

    public StartConfiguration(){
        this("config.ini");
    }

    public StartConfiguration(String pathToConfig){
        this.pathToConfig = pathToConfig;
        Stream.of(StartConfigurationParameters.values()).forEach(parameter -> addParameter(parameter.getName(), parameter.getDefault()));
        parseConfigurationFile();
    }

    @Override
    protected void parseConfigurationFile() {
        Map<String, String> parsedParametersMap = ConfigurationParser.parseConfigurationFile(Path.of(pathToConfig));
        parsedParametersMap.forEach(this::setParameterValue);
    }

    public void setParameterValue(StartConfigurationParameters parameter, String value){
        this.setParameterValue(parameter.getName(), value);
    }

    public String getParameterValue(StartConfigurationParameters parameter){
        return this.getParameterValue(parameter.getName());
    }
}
