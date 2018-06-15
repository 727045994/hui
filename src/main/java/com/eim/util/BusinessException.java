package com.eim.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class BusinessException extends Exception{
	
	private static Logger logger = LogManager.getLogger(BusinessException.class);
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessException() {
	        super();
	        // TODO Auto-generated constructor stub
	    }

	    public BusinessException(String message) {
	    	
	        super(message);
	        logger.error(message);
	        // TODO Auto-generated constructor stub
	    }
}
