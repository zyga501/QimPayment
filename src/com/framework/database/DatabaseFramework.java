package com.framework.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class DatabaseFramework {
    protected static SqlSessionFactory buildSqlSessionFactory(String batisConfig) {
        try {
            InputStream inputStream = Resources.getResourceAsStream(batisConfig);
            return new SqlSessionFactoryBuilder().build(inputStream);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
