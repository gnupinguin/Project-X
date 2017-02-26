package ru.dins.web.model.quote;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@Data
@RequiredArgsConstructor
public class Quote {
    @NonNull
    private String quoteAuthor;

    @NonNull
    private String quoteText;
}