package quoter;

import javax.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quote {
    @NotNull
    private String quoteAuthor;

    @NotNull
    private String quoteText;
}