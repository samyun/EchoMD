package echomd;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public final class echomdSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds;

    static {
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("INSERT_YOUR-APPLICATION_ID");
    }

    public echomdSpeechletRequestStreamHandler() {
        super(new echomdSpeechlet(), supportedApplicationIds);
    }
}
