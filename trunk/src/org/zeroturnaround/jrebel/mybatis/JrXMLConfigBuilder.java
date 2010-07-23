package org.zeroturnaround.jrebel.mybatis;

import org.apache.ibatis.session.Configuration;

public abstract interface JrXMLConfigBuilder
{
	public abstract Configuration reparse();
}