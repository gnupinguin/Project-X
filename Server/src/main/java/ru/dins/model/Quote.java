package ru.dins.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by gnupinguin on 19.02.17.
 */
@Document
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class Quote {
    @NonNull
    private String quotestext;

    @NonNull
    private String author;
}
