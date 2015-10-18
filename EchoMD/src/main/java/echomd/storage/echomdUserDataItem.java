package echomd.storage;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Model representing an item of the echomdUserData table in DynamoDB for the echomd
 * skill.
 */
@DynamoDBTable(tableName = "EchoMDUserData")
public class echomdUserDataItem {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String customerId;

    private echomdGameData gameData;

    @DynamoDBHashKey(attributeName = "CustomerId")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @DynamoDBAttribute(attributeName = "Data")
    @DynamoDBMarshalling(marshallerClass = echomdGameDataMarshaller.class)
    public echomdGameData getGameData() {
        return gameData;
    }

    public void setGameData(echomdGameData gameData) {
        this.gameData = gameData;
    }

    /**
     * A {@link DynamoDBMarshaller} that provides marshalling and unmarshalling logic for
     * {@link echomdGameData} values so that they can be persisted in the database as String.
     */
    public static class echomdGameDataMarshaller implements
            DynamoDBMarshaller<echomdGameData> {

        @Override
        public String marshall(echomdGameData gameData) {
            try {
                return OBJECT_MAPPER.writeValueAsString(gameData);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Unable to marshall game data", e);
            }
        }

        @Override
        public echomdGameData unmarshall(Class<echomdGameData> clazz, String value) {
            try {
                return OBJECT_MAPPER.readValue(value, new TypeReference<echomdGameData>() {
                });
            } catch (Exception e) {
                throw new IllegalStateException("Unable to unmarshall game data value", e);
            }
        }
    }
}
