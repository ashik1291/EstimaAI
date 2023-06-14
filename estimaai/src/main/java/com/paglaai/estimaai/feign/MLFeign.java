package com.paglaai.estimaai.feign;

import com.paglaai.estimaai.domain.request.MLEstimaBodyRequest;
import com.paglaai.estimaai.domain.response.ml.MlEstimaResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mlService")
public interface MLFeign {

  @PostMapping(value = "/estima")
  List<MlEstimaResponse> getFeatures(@RequestBody MLEstimaBodyRequest stories);
}
