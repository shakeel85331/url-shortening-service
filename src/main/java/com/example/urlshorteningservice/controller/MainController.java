package com.example.urlshorteningservice.controller;

import com.example.urlshorteningservice.model.UrlRequest;
import com.example.urlshorteningservice.model.UrlResponse;
import com.example.urlshorteningservice.service.UrlService;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {

  private final UrlService urlService;

  @PostMapping(value = "/tinyUrl", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UrlResponse> createShortUrl(@RequestBody UrlRequest request)
      throws NoSuchAlgorithmException {
    return new ResponseEntity<>(urlService.createShortUrl(request), HttpStatus.OK);
  }

  @GetMapping(value = "/{key}")
  public ResponseEntity<Void> redirectToLongUrl(@PathVariable("key") String key) {

    String shortUrl = urlService.getLongUrl(key);
    HttpHeaders headers = new HttpHeaders();
    headers.add("location", shortUrl);
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

}
