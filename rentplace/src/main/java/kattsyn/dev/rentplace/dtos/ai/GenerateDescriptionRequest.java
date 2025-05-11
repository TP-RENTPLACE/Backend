package kattsyn.dev.rentplace.dtos.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenerateDescriptionRequest {
    private String systemPrompt;

    private String userPrompt;
}
