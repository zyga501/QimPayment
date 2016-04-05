package com.merchant.database;

import com.framework.database.DatabaseFramework;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class Database extends DatabaseFramework {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession(false);
        sqlSession.close();
    }

    public static SqlSessionFactory SqlSessionFactory() {
        return sqlSessionFactory_;
    }

    static {
        String mybatisConfig = "com/merchant/database/conf.xml";
        sqlSessionFactory_ = DatabaseFramework.buildSqlSessionFactory(mybatisConfig);
    }

    private static SqlSessionFactory sqlSessionFactory_;
}
