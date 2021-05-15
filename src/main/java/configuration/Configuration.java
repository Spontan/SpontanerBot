package configuration;


import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains definition for parameters in the main config file.
 */
public abstract class Configuration {


    private final Map<String, String> parameterKeyToValue;
    static private Logger logger = LoggerFactory.getLogger(Configuration.class);

    public Configuration(){
        this.parameterKeyToValue = new HashMap<>();
    }

    public String getParameterValue(String parameterName){
        return parameterKeyToValue.get(parameterName);
    }

    public void setParameterValue(String parameterName, String parameterValue){
        if(parameterKeyToValue.replace(parameterName, parameterValue) == null)
            logger.info(MessageFormat.format("{0} is not a valid parameter name", parameterName));
    }

    private boolean isValidParameter(String parameterName){
        return false;
    }

    protected void addParameter(String parameterName, String defaultValue){
        if(parameterKeyToValue.containsKey(parameterName))
            logger.info(MessageFormat.format("Configuration already contains parameter \"{0}\" set to \"{1}\". " +
                    "Overwriting with new value \"{2}\"", parameterName, parameterKeyToValue.get(parameterName), defaultValue));
        parameterKeyToValue.put(parameterName, defaultValue);
    }

    abstract protected void parseConfigurationFile();
}
