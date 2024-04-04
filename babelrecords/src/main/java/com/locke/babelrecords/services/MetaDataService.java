package com.locke.babelrecords.services;

import com.locke.babelrecords.models.MetaTag;
import com.locke.babelrecords.repositories.MetaTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaDataService {
  MetaTagRepository metaTagRepository;

  public MetaDataService(MetaTagRepository metaTagRepository) {
    this.metaTagRepository = metaTagRepository;
  }

  public void createMetaTag(String userId, String operation, String filename, String fileId) {
    this.metaTagRepository.save(new MetaTag(userId, operation, filename, fileId));
  }

  public void createMetaTag(String userId, String operation, String filename, String fileId, List<String> recordsCreated) {
    this.metaTagRepository.save(new MetaTag(userId, operation, filename, fileId, recordsCreated));
  }

  public List<MetaTag> findByUserId(String userId) {
    return this.metaTagRepository.findByUserId(userId);
  }

  public List<MetaTag> findLastByUserId(String userId) {
    return this.metaTagRepository.findByUserIdOrderByCreatedAtDesc(userId);
  }
}
