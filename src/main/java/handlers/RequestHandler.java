package handlers;

import dao.RateLimiter;
import dao.RateLimiterDao;
import lombok.AllArgsConstructor;

import java.util.List;
// Request Handler class
@AllArgsConstructor
public class RequestHandler {

    private static final long ONE_SEC_IN_NANO = 1000000000;
    private static final int RATE_LIMIT_THRESHOLD = 50;

    private final RateLimiterDao rateLimiterDao;

    public boolean canProcess(final String serviceName, final String api) {
        final String key = RateLimiter.fetchKey(serviceName, api);
        final long endTimestamp = System.nanoTime();
        final long startTimestamp = endTimestamp - ONE_SEC_IN_NANO;

        final List<RateLimiter> rateLimiters = rateLimiterDao.query(key, startTimestamp, endTimestamp);

        return rateLimiters.size() <= RATE_LIMIT_THRESHOLD;
    }

    public void updateRateLimitingData(final String serviceName, final String api) {
        rateLimiterDao.save(RateLimiter.fetchKey(serviceName, api), System.nanoTime());
    }
}
