package dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RateLimiterDao {

    private final DynamoDBMapper ddbMapper;

    public List<RateLimiter> query(final String resourceId, final Long startTimestamp, final Long endTimestamp) {
        final RateLimiter rateLimiter = RateLimiter.builder()
                .resourceId(resourceId)
                .build();

        final Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.BETWEEN)
                .withAttributeValueList(ImmutableList.of(
                        new AttributeValue().withN(String.valueOf(startTimestamp)),
                        new AttributeValue().withN(String.valueOf(endTimestamp))));
        final DynamoDBQueryExpression<RateLimiter> expression = new DynamoDBQueryExpression<RateLimiter>()
                .withHashKeyValues(rateLimiter)
                .withRangeKeyCondition("timestamp", rangeKeyCondition);

        return ddbMapper.query(RateLimiter.class, expression);
    }

    public void save(final String resourceId, final Long timestamp) {
        final RateLimiter rateLimiter = RateLimiter.builder()
                .resourceId(resourceId)
                .timestamp(timestamp)
                .build();
        ddbMapper.save(rateLimiter, DynamoDBMapperConfig.DEFAULT);
    }
}
