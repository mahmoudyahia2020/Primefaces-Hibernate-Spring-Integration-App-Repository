package com.marased.dao;

import java.util.List;

public interface daoGeneral {

    public void save(Object o);

    public void delete(Object o);

    public List getAllObjects(Class clazz);

    public List getObjectsByParameter(Class clazz, String parameter, Object value);

    public List getMembersByCharity(Integer cId);
}
