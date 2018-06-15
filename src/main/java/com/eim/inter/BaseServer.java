package com.eim.inter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.eim.util.BusinessException;

public interface BaseServer<T> {
	/**获得对象*/
	T get(Map<String, Object> params)throws BusinessException;
	/**获得对象列表*/
	List<T> getList(Map<String, Object> params)throws BusinessException;;
	/**通过主键获得对象*/
	T getByPrimaryKey(String primaryKey)throws BusinessException;;
	/**插入对象*/
	void insert(T t)throws BusinessException;;
	/**通过主键更新对象*/
	void updateByPrimaryKey(T entity)throws BusinessException;;
	/**通过主键删除对象*/
	void deleteByPrimaryKey(String primaryKey)throws BusinessException;;
}
