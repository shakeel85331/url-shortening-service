package com.example.urlshorteningservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UrlRequest {

  private int id;
  private String url;
  private String alias;
  private String domain;
  private int ttl;

}
