package org.apereo.openlrw.oneroster.service;

import org.apache.commons.lang3.StringUtils;
import org.apereo.model.oneroster.AcademicSession;
import org.apereo.openlrw.oneroster.exception.AcademicSessionNotFoundException;
import org.apereo.openlrw.oneroster.service.repository.MongoAcademicSession;
import org.apereo.openlrw.oneroster.service.repository.MongoAcademicSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author stalele
 * @author xchopin <xavier.chopin@univ-lorraine.fr>
 */
@Service
public class AcademicSessionService {

  private static Logger logger = LoggerFactory.getLogger(AcademicSessionService.class);

  private MongoAcademicSessionRepository mongoAcademicSessionRepository;
  
  /**
   * Constructor
   * @param mongoAcademicSessionRepository
   * @param classService
   */
  @Autowired
  public AcademicSessionService(MongoAcademicSessionRepository mongoAcademicSessionRepository, 
      ClassService classService) {
    this.mongoAcademicSessionRepository = mongoAcademicSessionRepository;
  }

  
  /**
   * Find academic session from sourcedId
   * @param tenantId
   * @param orgId
   * @param academicSessionSourcedId
   * @return AcademicSession object
   * @throws AcademicSessionNotFoundException
   */
  public AcademicSession findBySourcedId(final String tenantId, final String orgId, final String academicSessionSourcedId) throws AcademicSessionNotFoundException {
    MongoAcademicSession mongoAcademicSession
      =  mongoAcademicSessionRepository
        .findByTenantIdAndOrgIdAndAcademicSessionSourcedId(tenantId, orgId, academicSessionSourcedId);
    
    if (mongoAcademicSession != null) {
      return mongoAcademicSession.getAcademicSession();
    }
    
    throw new AcademicSessionNotFoundException(String.format("Academic Session not found for sourcedId %s", academicSessionSourcedId));
  }

  
  /**
   * Save the academic session, if it does not exist. If it exists, it updates the existing academic session object
   * @param tenantId
   * @param orgId
   * @param academicSession
   * @return AcademicSession
   */
  public AcademicSession save(final String tenantId, final String orgId, AcademicSession academicSession) {
    if (StringUtils.isBlank(tenantId) 
        || StringUtils.isBlank(orgId)
        || academicSession == null
        || StringUtils.isBlank(academicSession.getSourcedId())) {
      throw new IllegalArgumentException();
    }
    
    MongoAcademicSession mongoAcademicSession
    =  mongoAcademicSessionRepository
      .findByTenantIdAndOrgIdAndAcademicSessionSourcedId(tenantId, orgId, academicSession.getSourcedId());
    
    if (mongoAcademicSession == null) {
      mongoAcademicSession 
        = new MongoAcademicSession.Builder()
          .withAcademicSessionSourcedId(academicSession.getSourcedId())
          .withOrgId(orgId)
          .withTenantId(tenantId)
          .withAcademicSession(academicSession)
          .build();
    }
    else {
      mongoAcademicSession
        = new MongoAcademicSession.Builder()
          .withId(mongoAcademicSession.getId())
          .withAcademicSessionSourcedId(mongoAcademicSession.getAcademicSessionSourcedId())
          .withOrgId(mongoAcademicSession.getOrgId())
          .withTenantId(mongoAcademicSession.getTenantId())
          .withAcademicSession(academicSession)
          .build();
    }
    
    MongoAcademicSession saved = mongoAcademicSessionRepository.save(mongoAcademicSession);

    return saved.getAcademicSession(); 

  }

}
