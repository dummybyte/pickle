package dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName="RateLimiter")
public class RateLimiter {

    @DynamoDBHashKey
    private String resourceId;

    @DynamoDBRangeKey
    private Long timestamp;

    public static String fetchKey(final String serviceName, final String api) {
        return serviceName + "_" + api;
    }
}
