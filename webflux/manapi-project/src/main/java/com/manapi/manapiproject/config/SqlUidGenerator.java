package com.manapi.manapiproject.config;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SqlUidGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generate'");
    }

    
    
}
