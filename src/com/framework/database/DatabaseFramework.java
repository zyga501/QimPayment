package com.framework.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class DatabaseFramework {
    public static SqlSessionFactory SqlSessionFactory() {
        return sqlSessionFactory_;
    }

    protected static void buildSqlSessionFactory(String batisConfig) {
        try {
            InputStream inputStream = Resources.getResourceAsStream(batisConfig);
            sqlSessionFactory_ = new SqlSessionFactoryBuilder().build(inputStream);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static SqlSessionFactory sqlSessionFactory_;
}
