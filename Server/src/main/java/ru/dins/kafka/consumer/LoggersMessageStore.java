package ru.dins.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by key on 07.03.2017.
 */
public class LoggersMessageStore {
    public static final String ERROR_ADDING_QUOTE_MESSAGE = "When creating quotes error occurred!";
    public static final String SUCCESS_ADDING_QUOTE_MESSAGE = "Quote has been created successfully!";
    public static final String WARNING_REPOSITORY_CONNECTION_FAILED = "Local repository connection failed. Trying to connect remote host";
    public static final String LOST_QUOTE_ERROR_PATTERN = "%s was lost in %s";
    public static final String CONNECTION_REPOSITORY_WARNING = "Connection to repository failed";
    public static final String ADDING_QUOTE_TO_REPLICA_ERROR = "Error with adding quote to replica topic";
}
