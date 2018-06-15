package com.eim.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eim.mapper.CustServiceMapper;
import com.eim.model.CustServiceInfo;
import com.eim.service.CustService;
import com.eim.util.BusinessException;
@Service
public class CustServiceImpl implements CustService{
	@Autowired
	private CustServiceMapper custServiceMapper;

	@Override
	public CustServiceInfo get(Map<String, Object> params) throws BusinessException {
		return custServiceMapper.get(params);
	}

	@Override
	public List<CustServiceInfo> getList(Map<String, Object> params) throws BusinessException {
		// TODO Auto-generated method stub
		return custServiceMapper.getList(params);
	}

	@Override
	public CustServiceInfo getByPrimaryKey(String primaryKey) throws BusinessException {
		// TODO Auto-generated method stub
		return custServiceMapper.getByPrimaryKey(primaryKey);
	}

	@Override
	public void insert(CustServiceInfo t) throws BusinessException {
		// TODO Auto-generated method stub
		custServiceMapper.insert(t);
	}

	@Override
	public void updateByPrimaryKey(CustServiceInfo entity) throws BusinessException {
		// TODO Auto-generated method stub
		custServiceMapper.updateByPrimaryKey(entity);
	}

	@Override
	public void deleteByPrimaryKey(String primaryKey) throws BusinessException {
		// TODO Auto-generated method stub
		custServiceMapper.deleteByPrimaryKey(primaryKey);
	}

	

}
