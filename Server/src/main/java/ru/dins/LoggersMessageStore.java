package ru.dins;


/**
 * Class with error/ warning messages for logger.
 *
 * @author Olga Taranova
 */
public class LoggersMessageStore {
    public static final String ADDING_QUOTE_MESSAGE_ERROR = "When creating quotes error occurred!";
    public static final String SUCCESS_ADDING_QUOTE_MESSAGE = "Quote has been created successfully!";
    public static final String REPOSITORY_CONNECTION_FAILED_WARNING = "Local repository connection failed. Trying to connect remote host";
    public static final String LOST_QUOTE_ERROR_PATTERN = "%s was lost in %s";
    public static final String CONNECTION_REPOSITORY_WARNING = "Connection to repository failed";
    public static final String ADDING_QUOTE_TO_REPLICA_ERROR = "Error with adding quote to replica topic";
}
