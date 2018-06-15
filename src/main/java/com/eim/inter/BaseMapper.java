package com.eim.inter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {
	/**获得对象*/
	T get(Map<String, Object> params);
	/**获得对象列表*/
	List<T> getList(Map<String, Object> params);
	/**通过主键获得对象*/
	T getByPrimaryKey(String primaryKey);
	/**插入对象*/
	void insert(T t);
	/**通过主键更新对象*/
	void updateByPrimaryKey(T entity);
	/**通过主键删除对象*/
	void deleteByPrimaryKey(String primaryKey);
	/**通过主键批量删除对象*/
	void delete(List<String> ids);
	/**通过主键批量删除对象*/
	void deleteObj(List<T> list);
}
