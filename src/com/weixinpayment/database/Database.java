package com.weixinpayment.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class Database {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession(false);
        sqlSession.close();
    }

    static {
        buildSqlSessionFactory();
    }

    public static SqlSessionFactory SqlSessionFactory() {
        return sqlSessionFactory_;
    }

    private static void buildSqlSessionFactory() {
        try {
            String mybatisConfig = "com/weixinpayment/database/conf.xml";
            InputStream inputStream = Resources.getResourceAsStream(mybatisConfig);
            sqlSessionFactory_ = new SqlSessionFactoryBuilder().build(inputStream);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static SqlSessionFactory sqlSessionFactory_;
}
