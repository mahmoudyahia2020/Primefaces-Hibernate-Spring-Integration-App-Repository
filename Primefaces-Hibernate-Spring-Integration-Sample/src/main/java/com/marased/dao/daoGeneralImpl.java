package com.marased.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("daoGeneral")
public class daoGeneralImpl implements daoGeneral {

    @Autowired
    private SessionFactory sessionFactory;
    private Session session = null;

    @Override
    public void save(Object o) {
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        try {
            session.saveOrUpdate(o);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            try {
                session.merge(o);
            } catch (Exception ex) {
                session.getTransaction().rollback();
                throw new ValidatorException(new FacesMessage());
            }
        } finally {
            session.flush();
            session.close();
        }
    }

    @Override
    public void delete(Object o) {
        // TODO Auto-generated method stub
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(o);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List getAllObjects(Class clazz) {
        session = sessionFactory.openSession();
        Criteria cri = session.createCriteria(clazz);
        List list = cri.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        session.close();
        return list;
    }

    @Override
    public List getObjectsByParameter(Class clazz, String parameter, Object value) {
        session = sessionFactory.openSession();
        List objects = session.createCriteria(clazz).add(Restrictions.eq(parameter, value)).setMaxResults(1000)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        session.close();
        return objects;
    }

    @Override
    public List getMembersByCharity(Integer cId) {
        // TODO Auto-generated method stub
        String sqlQuery = "select m.name, m.`position`,	c.name, c.activity from\n" + "	members m,\n"
                + "charity_members cm,	charity WHERE  c.id = cm.charity_id and m.id = cm.member_id and c.id = :cId";
        Query query = session.createSQLQuery(sqlQuery);
        List membersList = query.setParameter("cId", cId).list();

        return membersList;
    }
}
