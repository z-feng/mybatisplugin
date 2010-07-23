package org.zeroturnaround.jrebel.mybatis;

import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.session.SqlSessionFactory;

public abstract interface JrSqlSessionFactoryBuilder
{
	public abstract SqlSessionFactory rebuild(Reader reader, String environment, Properties props);
}