package ru.dins.web.model.quote;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@Data
@RequiredArgsConstructor
public class Quote {
    @NonNull
    private String author;

    @NonNull
    private String text;
}