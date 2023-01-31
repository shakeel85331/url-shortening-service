package com.example.urlshorteningservice.repository;

import com.example.urlshorteningservice.entity.Url;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends CassandraRepository<Url, String> {

}
