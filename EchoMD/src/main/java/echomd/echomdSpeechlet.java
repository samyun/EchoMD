package echomd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class echomdSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(echomdSpeechlet.class);

    private AmazonDynamoDBClient amazonDynamoDBClient;

    private echomdManager echomdManager;

    private SkillContext skillContext;

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        initializeComponents();

        // if user said a one shot command that triggered an intent event,
        // it will start a new session, and then we should avoid speaking too many words.
        skillContext.setNeedsMoreHelp(false);
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        skillContext.setNeedsMoreHelp(true);
        return echomdManager.getLaunchResponse(request, session);
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        initializeComponents();

        Intent intent = request.getIntent();
        if ("JustTookIntent".equals(intent.getName())) {
            return echomdManager.getJustTookIntentResponse(intent, session, skillContext);

        } else if ("NeedToTakeIntent".equals(intent.getName())) {
            return echomdManager.getNeedToTakeIntentResponse(intent, session, skillContext);
			
        } else if ("ResetPlayersIntent".equals(intent.getName())) {
            return echomdManager.getResetPlayersIntentResponse(intent, session, skillContext);
			
        } else if ("HelpIntent".equals(intent.getName())) {
            return echomdManager.getHelpIntentResponse(intent, session, skillContext);

        } else if ("ExitIntent".equals(intent.getName())) {
            return echomdManager.getExitIntentResponse(intent, session, skillContext);

        } else {
            throw new IllegalArgumentException("Unrecognized intent: " + intent.getName());
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Initializes the instance components if needed.
     */
    private void initializeComponents() {
        if (amazonDynamoDBClient == null) {
            amazonDynamoDBClient = new AmazonDynamoDBClient();
            echomdManager = new echomdManager(amazonDynamoDBClient);
            skillContext = new SkillContext();
        }
    }
}
