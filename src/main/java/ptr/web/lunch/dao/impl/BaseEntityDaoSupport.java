package ptr.web.lunch.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ptr.web.lunch.exceptions.BaseEntitySaveException;

import java.io.Serializable;
import java.util.List;

@Repository("baseEntityDaoSupport")
public class BaseEntityDaoSupport extends HibernateDaoSupport {

    protected static final Logger logger = Logger.getLogger(BaseEntityDaoSupport.class);


    @Transactional
    public Object saveEntity(String entityName, Object entity, boolean isInserted)  {

        try {
            logger.debug("=== retrieving the current session ===");
            Session currentSession = getSessionFactory().getCurrentSession();
            if(entity != null){

                    currentSession.saveOrUpdate(entityName, entity);
                    logger.debug("=== entityList " + entityName + " : " + entity + " saved or updated ===");
            }
            return entity;
        }
        catch (Exception ex){
            logger.error("<<<<< Error while saving entityList "+ entity+" >>>> "+ex);
            throw new BaseEntitySaveException(ex.getMessage(), entityName);
        }
    }

    @Transactional
    public Object persistEntity(String entityName, Object entity) {

        Object entityToPersist = entity;
        try{
            logger.debug("=== retrieving the current session ===");
            Session currentSession = getSessionFactory().getCurrentSession();
            if(entity != null){
                currentSession.persist(entityName, entityToPersist);
                logger.debug("=== entityList persisted" + entityToPersist);
            }
            return entity;
        }
        catch (Exception ex){
            logger.error("<<<<< Error while persisting entityList "+ entity+" >>>> "+ex);
            return  entity;
        }
    }

    public void saveEntityList(String entityName, List<? extends Serializable> entityList)  {

        try{
            logger.debug("=== retrieving the current session ===");
            if(entityList != null){
                for (Object item : entityList){
                    saveEntity(entityName, item, false);
                    logger.debug("=== entityName " + entityName + " : " + item + " saved or updated ===");
                }
            }
        }
        catch (Exception ex){
            logger.error("<<<<< Error while saving entityList "+ entityList +" >>>> "+ex);
            throw new BaseEntitySaveException(ex.getMessage(),entityName);
        }
    }
}