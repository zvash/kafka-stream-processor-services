package xyz.gouril.microservices.demo.twitter.to.kafka.service.runner.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import xyz.gouril.microservices.demo.config.TwitterToKafkaServiceConfigData;
import xyz.gouril.microservices.demo.twitter.to.kafka.service.exception.TwitterToKafkaServiceException;
import xyz.gouril.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import xyz.gouril.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "true")
public class MockKafkaStreamRunner implements StreamRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MockKafkaStreamRunner.class);

    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
    private final TwitterKafkaStatusListener twitterKafkaStatusListener;

    private static final Random RANDOM = new Random();

    private static final String[] WORDS = {
            "feugiat",
            "in",
            "ante",
            "metus",
            "dictum",
            "at",
            "tempor",
            "commodo",
            "ullamcorper",
            "a",
            "lacus",
            "vestibulum",
            "sed",
            "arcu",
            "non",
            "odio",
            "euismod",
            "lacinia",
            "at",
            "quis"
    };

    private static final String tweetAsRawJson = "{" +
            "\"created_at\":\"{0}\"," +
            "\"id\":\"{1}\"," +
            "\"text\":\"{2}\"," +
            "\"user\":{\"id\": \"{3}\"}" +
            "}";

    private static final String TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    public MockKafkaStreamRunner(TwitterToKafkaServiceConfigData configData,
                                 TwitterKafkaStatusListener statusListener) {
        this.twitterToKafkaServiceConfigData = configData;
        this.twitterKafkaStatusListener = statusListener;
    }

    @Override
    public void start() throws TwitterException {
        String[] keywords = twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[0]);
        long sleepTimeMs = twitterToKafkaServiceConfigData.getMockSleepMs();
        int mockMinTweetLength = twitterToKafkaServiceConfigData.getMockMinTweetLength();
        int mockMaxTweetLength = twitterToKafkaServiceConfigData.getMockMaxTweetLength();
        LOG.info("Start to mock twitter stream for keywords: {}", Arrays.toString(keywords));
        simulateTwitterStream(keywords, sleepTimeMs, mockMinTweetLength, mockMaxTweetLength);
    }

    private void simulateTwitterStream(String[] keywords, long sleepTimeMs, int mockMinTweetLength, int mockMaxTweetLength)
            throws TwitterException {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    String formattedTweetAsRawJson = getFormattedTweet(keywords, mockMinTweetLength, mockMaxTweetLength);
                    Status status = TwitterObjectFactory.createStatus(formattedTweetAsRawJson);
                    twitterKafkaStatusListener.onStatus(status);
                    sleep(sleepTimeMs);
                }
            } catch (TwitterException e) {
                LOG.info("Error creating twitter status", e);
            }
        });
    }

    private void sleep(long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            throw new TwitterToKafkaServiceException("Error while sleeping for waiting for new status to create.");
        }
    }

    private String getFormattedTweet(String[] keywords, int mockMinTweetLength, int mockMaxTweetLength) {
        String[] params = new String[]{
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
                getRandomTweetContent(keywords, mockMinTweetLength, mockMaxTweetLength),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
        };
        return formatTweetAsJsonWithParams(params);
    }

    private String formatTweetAsJsonWithParams(String[] params) {
        String tweet = tweetAsRawJson;
        for (int i = 0; i < params.length; i++) {
            tweet = tweet.replace("{" + i + "}", params[i]);
        }
        return tweet;
    }

    private String getRandomTweetContent(String[] keywords, int mockMinTweetLength, int mockMaxTweetLength) {
        StringBuilder tweet = new StringBuilder();
        int tweetLength = RANDOM.nextInt(mockMaxTweetLength - mockMinTweetLength + 1) + mockMinTweetLength;
        return constructRandomTweet(keywords, tweet, tweetLength);
    }

    private String constructRandomTweet(String[] keywords, StringBuilder tweet, int tweetLength) {
        for (int i = 0; i < tweetLength; i++) {
            tweet.append(WORDS[RANDOM.nextInt(WORDS.length)]).append(" ");
            if (i == tweetLength / 2) {
                tweet.append(keywords[RANDOM.nextInt(keywords.length)]).append(" ");
            }
        }
        return tweet.toString().trim();
    }
}
