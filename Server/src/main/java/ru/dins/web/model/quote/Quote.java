package ru.dins.web.model.quote;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * The main entity with which operations are performed in the application.
 *
 * @author Ilja Pavlov
 */
@Document @NoArgsConstructor @Data @RequiredArgsConstructor
public class Quote {
    @NonNull @Id
    private UUID id;

    @NonNull
    private String author;

    @NonNull
    private String text;
}