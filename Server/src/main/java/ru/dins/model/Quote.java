package ru.dins.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by gnupinguin on 19.02.17.
 */
@Document
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class Quote {
    @Id
    private String id;

    @NonNull
    private String quotestext;

    @NonNull
    private String author;
}
