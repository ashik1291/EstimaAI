package com.paglaai.estimaai.feign;

import com.paglaai.estimaai.domain.request.MlEstimaBody;
import com.paglaai.estimaai.domain.response.ml.MlEstimaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "mlService")
public interface MLFeign {

    @PostMapping(value = "/estima")
    List<MlEstimaResponse> getFeatures(@RequestBody MlEstimaBody stories);

}