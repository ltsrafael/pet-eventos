package com.rafael.peteventos.integration.openfeign;

import com.rafael.peteventos.domain.dto.GoogleApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "googleapi", url = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=")
public interface GoogleApi {
    @GetMapping("{accesstoken}")
    GoogleApiResponseDto retornaUsuario(@PathVariable String accessToken);
}
