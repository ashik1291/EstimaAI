package com.paglaai.estimaai.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MlEstimaBody {
    private String story;
}
