package echomd.storage;

import com.amazon.speech.speechlet.Session;

/**
 * Contains the methods to interact with the persistence layer for echomd in DynamoDB.
 */
public class echomdDao {
    private final echomdDynamoDbClient dynamoDbClient;

    public echomdDao(echomdDynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    /**
     * Reads and returns the {@link echomdGame} using user information from the session.
     * <p>
     * Returns null if the item could not be found in the database.
     * 
     * @param session
     * @return
     */
    public echomdGame getechomdGame(Session session) {
        echomdUserDataItem item = new echomdUserDataItem();
        item.setCustomerId(session.getUser().getUserId());

        item = dynamoDbClient.loadItem(item);

        if (item == null) {
            return null;
        }

        return echomdGame.newInstance(session, item.getGameData());
    }

    /**
     * Saves the {@link echomdGame} into the database.
     * 
     * @param game
     */
    public void saveechomdGame(echomdGame game) {
        echomdUserDataItem item = new echomdUserDataItem();
        item.setCustomerId(game.getSession().getUser().getUserId());
        item.setGameData(game.getGameData());

        dynamoDbClient.saveItem(item);
    }
}
