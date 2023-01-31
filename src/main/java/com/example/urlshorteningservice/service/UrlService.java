package com.example.urlshorteningservice.service;

import com.example.urlshorteningservice.entity.Url;
import com.example.urlshorteningservice.model.UrlRequest;
import com.example.urlshorteningservice.model.UrlResponse;
import com.example.urlshorteningservice.repository.UrlRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@RequiredArgsConstructor
public class UrlService {

  private static final int KEY_LENGTH = 7;
  private static final String DEFAULT_DOMAIN = "http://localhost:8080/";
  private final UrlRepository urlRepository;
  Set<String> set = new HashSet<>();
  private long counter = 0;

  public UrlResponse createShortUrl(UrlRequest request) throws NoSuchAlgorithmException {

    String key = request.getAlias() == null ? getKey(request) : request.getAlias();

    while (urlRepository.findById(key).isPresent()) {
      if (request.getAlias() != null) {
        throw new RuntimeException("Bad Request, Alias already ");
      }
      key = getKey(request);
    }

    Url url = new Url();
    url.setId(key);
    url.setUrl(request.getUrl());
    url.setDomain(request.getDomain());

    counter++;
    urlRepository.save(url);

    UrlResponse response = new UrlResponse();
    String shortUrl = request.getDomain() == null ? DEFAULT_DOMAIN + key : request.getDomain() + key;
    response.setShortUrl(shortUrl);
    return response;
  }

  private String getKey(UrlRequest request) throws NoSuchAlgorithmException {

    // appending sequence number to the longUrl
    String longUrl = request.getUrl() + counter;

    // MD5 encode of longUrl
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(longUrl.getBytes());
    byte[] digest = md.digest();
    String hash = DigestUtils.md5DigestAsHex(digest);

    // Base64 encoding the MD5 hash
    String encodedString = Base64.getEncoder().encodeToString(hash.getBytes());

    return getRandomizedString(encodedString);
  }

  private String getRandomizedString(String inputString) {

    int max = inputString.length() - 1;
    StringBuilder stringBuilder = new StringBuilder();
    Random random = new Random();

    for (int i = 0; i < KEY_LENGTH; i++) {
      stringBuilder.append(inputString.charAt(random.nextInt(max)));
    }

    return stringBuilder.toString();
  }


  public String getLongUrl(String key) {
    Optional<Url> resp = urlRepository.findById(key);
    return resp.map(Url::getUrl).orElse(null);
  }
}
