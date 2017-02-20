package ru.dins.model.quote;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Quote {
    @NonNull
    private String quoteAuthor;

    @NonNull
    private String quoteText;
}