package configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConfigurationParser {

    static private Logger logger = LoggerFactory.getLogger(ConfigurationParser.class);

    public static Map<String, String> parseConfigurationFile(Path pathToFile){
        File configurationFile = pathToFile.toFile();
        if(!configurationFile.isFile()) {
            logger.error(MessageFormat.format("Specified file \"{0}\" does not exist", configurationFile.getAbsolutePath()));
            return new HashMap<>();
        }
        if(!configurationFile.canRead()) {
            logger.error(MessageFormat.format("No read access to file \"{0}\"", configurationFile.getAbsolutePath()));
            return new HashMap<>();
        }
        return doParseConfigurationFile(configurationFile);
    }

    private static Map<String, String> doParseConfigurationFile(File configurationFile){
        Map<String, String> parameterKeyToValue = new HashMap<>();

        Scanner configurationReader;
        try {
            configurationReader = new Scanner(configurationFile);
        } catch (FileNotFoundException e) {
            logger.error(MessageFormat.format("Specified file \"{0}\" could not be opened", configurationFile.getAbsolutePath()));
            return parameterKeyToValue;
        }


        for(int currentLine = 0; configurationReader.hasNext(); currentLine++){
            parseLine(configurationReader.nextLine(), parameterKeyToValue, currentLine, configurationFile.getPath());
        }
        return parameterKeyToValue;
    }

    private static void parseLine(String line, Map<String, String> parameterKeyToValue, int currentLine, String pathToConfig) {
        line = line.split("#")[0]; //ignore comments
        String[] parameterKeyAndValue = line.split("=");

        if(!checkAssignmentValid(parameterKeyAndValue, currentLine, pathToConfig))
            return;

        String key = parameterKeyAndValue[0].trim();
        String value = removeOuterQuotation(parameterKeyAndValue[1].trim());

        parameterKeyToValue.put(key, value);
    }

    private static boolean checkAssignmentValid(String[] parameterKeyAndValue, int lineInConfig, String pathToConfig){
        if(parameterKeyAndValue.length == 0 || parameterKeyAndValue[0].isBlank()){
            return false;
        }
        if(parameterKeyAndValue.length == 1){
            logger.warn(MessageFormat.format("Invalid parameter assignment in line {0} of file {1}: " +
                    "Missing assignment", lineInConfig, pathToConfig));
            return false;
        }
        if(parameterKeyAndValue.length > 2) {
            logger.warn(MessageFormat.format("Invalid parameter assignment in line {0} of file {1}: " +
                    "Multiple assignments in one line", lineInConfig, pathToConfig));
            return false;
        }
        if(parameterKeyAndValue[0].trim().contains(" ")){
            logger.warn(MessageFormat.format("Invalid parameter assignment in line {0} of file {1}: " +
                    "Parameter names may not contain whitespaces", lineInConfig, pathToConfig));
            return false;
        }
        return true;
    }

    private static String removeOuterQuotation(String quotedString) {
        if(quotedString.startsWith("\"") && quotedString.endsWith("\""))
            quotedString = quotedString.substring(1, quotedString.length()-1);
        return quotedString;
    }
}
