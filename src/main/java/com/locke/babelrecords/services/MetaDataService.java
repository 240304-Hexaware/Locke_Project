package com.locke.babelrecords.services;

import com.locke.babelrecords.models.MetaTag;
import com.locke.babelrecords.repositories.MetaTagRepository;
import org.springframework.stereotype.Service;

@Service
public class MetaDataService {
  MetaTagRepository metaTagRepository;

  public MetaDataService(MetaTagRepository metaTagRepository) {
    this.metaTagRepository = metaTagRepository;
  }

  public void CreateMetaTag(String userId, String operation, String filename, String fileId) {
    this.metaTagRepository.save(new MetaTag(userId, operation, filename, fileId));
  }
}
