package com.eim.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

public class GenCodeUtil {
	private String tablename;
	private String schema;
	private String basePackagePath="com.eim.auto";
	private String entityPath;
	private String mapperPath;
	private String servicePath;
	private String actionPath;
	private String actionRequestMappingValue;
	private String actionFileName;
	private String jspPath;
	private String jsPath;
	private String URL;
	private String NAME;
	private String PASS;
	private String DRIVER;
	private String pks = "";
	private String[] colnames;
	private String[] colTypes;
	private int[] colSizes;
	private int[] colNullables;
	private String[] remarks;
	private String entityName;
	public void genEntity(){
		this.OutFile(this.parseJavaBean(), this.entityName+".java",this.entityPath,"");
	}
	public void genService(){
		this.OutFile(this.parseService(), "I"+this.entityName+"Service.java",this.servicePath,"");
		if(this.servicePath==null||"".equals(this.servicePath)){
			this.OutFile(this.parseServiceImpl(), this.entityName+"ServiceImpl.java",this.servicePath,"");
		}else{
			this.OutFile(this.parseServiceImpl(), this.entityName+"ServiceImpl.java",this.servicePath+".impl","");
		}
		
	}
	public void genMapper(){
		this.OutFile(this.parseMapper(), this.entityName+"Mapper.java",this.mapperPath,"");
		if(DRIVER.contains("mysql")){
			this.OutFile(this.parseXML(), entityName + "-mapper.mysql.xml",this.mapperPath,"");
		}else if(DRIVER.contains("oracle")){
			this.OutFile(this.parseXML(), entityName + "-mapper.oracle.xml",this.mapperPath,"");
		}
	}
	public void genController(){
		this.OutFile(this.parseAction(), this.actionFileName+".java",this.actionPath,"");
	}
	public void genJsp(){
		this.OutFile(this.parseJsp(), entityName.toLowerCase()+".index.jsp",this.jspPath,"jsp");
		this.OutFile(this.parseJsp(), entityName.toLowerCase()+".form.jsp",this.jspPath,"jsp");
	}
	public void genJs(){
		this.OutFile(this.parseJs(), entityName.toLowerCase()+".index.js",this.jsPath,"js");
		this.OutFile(this.parseJs(), entityName.toLowerCase()+".form.js",this.jsPath,"js");
	}
	public GenCodeUtil(Map<String,String> map) {
		this.URL=map.get("URL");
		this.NAME=map.get("NAME");
		this.PASS=map.get("PASS");
		this.DRIVER=map.get("DRIVER");
		this.tablename=map.get("tablename");
		this.schema=map.get("schema");
		this.entityPath=map.get("entityPath");
		this.mapperPath=map.get("mapperPath");
		this.servicePath=map.get("servicePath");
		this.actionPath=map.get("controllerPath");
		this.actionRequestMappingValue=map.get("controllerRequestMappingValue");
		this.actionFileName=map.get("controllerFileName")==null||"".equals(map.get("controllerFileName").trim())?getEntityName(tablename)+"Controller":map.get("controllerFileName");
		this.jspPath=map.get("jspPath");
		this.jsPath=map.get("jsPath");
		if(entityPath==null||"".equals(entityPath)){
			entityPath=basePackagePath;
		}
		if(mapperPath==null||"".equals(mapperPath)){
			mapperPath=basePackagePath;
		}
		if(servicePath==null||"".equals(servicePath)){
			servicePath=basePackagePath;
		}
		if(actionPath==null||"".equals(actionPath)){
			actionPath=basePackagePath;
		}
		if(jspPath==null||"".equals(jspPath)){
			jspPath=basePackagePath;
		}
		if(jsPath==null||"".equals(jsPath)){
			jsPath=basePackagePath;
		}
		Connection con = null;
		tablename=tablename.toUpperCase();
		String sql = "select * from " + tablename;
		Statement pStemt = null;
		try {
			try {
				Class.forName(DRIVER);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			Properties props =new Properties();
			props.put("user", NAME);
			props.put("password", PASS);
			props.put("remarksReporting","true");
			con = DriverManager.getConnection(URL, props);
			
			pStemt = (Statement) con.createStatement();
			ResultSet rs = pStemt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();

			ResultSet r = con.getMetaData().getPrimaryKeys(null, schema, tablename);
			while (r.next()) {pks = pks + r.getString("COLUMN_NAME") + ",";}
			int size = rsmd.getColumnCount();
			colnames = new String[size];
			colTypes = new String[size];
			colSizes = new int[size];
			colNullables = new int[size];
			for (int i = 0; i < size; i++) {
				colnames[i] = rsmd.getColumnName(i + 1);
				colTypes[i] = rsmd.getColumnTypeName(i + 1);
				colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
				colNullables[i] = rsmd.isNullable(i + 1);
			}
			entityName=getEntityName(tablename);
			ResultSet rr = null;
			if(DRIVER.contains("oracle")){
				if(schema==null||"".equals(schema.trim())){
					System.out.println("oracle请填上schema，否则不能生成注释！");
				}
				rr =con.getMetaData().getColumns(null, schema,tablename, "%");
			}else if(DRIVER.contains("mysql")){
				rr = con.getMetaData().getColumns(null, "%", tablename, "%");
			}
			remarks = new String[size];
			int i=0;
			while(rr.next()){
				remarks[i] = rr.getString("REMARKS");
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private void OutFile(String content, String fileName ,String path,String type) {
		PrintWriter pw=null;
		try {
			File directory = new File("");
			String outputPath = "";
			if("jsp".equals(type)){
				outputPath = directory.getAbsolutePath() + "/WebContent/WEB-INF/jsp/" +path.replace(".", "/") + "/" + fileName;
			}else if("js".equals(type)){
				outputPath = directory.getAbsolutePath() + "/WebContent/assets/business/" +path.replace(".", "/") + "/" + fileName;
			}else{
				outputPath = directory.getAbsolutePath() + "/src/main/java/" +path.replace(".", "/") + "/" + fileName;
			}
			pw = new PrintWriter(new FileWriter(outputPath));
			pw.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(pw!=null){
				pw.flush();
				pw.close();
			}
		}
	}
	private String parseJavaBean() {
		StringBuffer sb = new StringBuffer();
		sb.append("package " + this.entityPath + ";\r\n");
		sb.append("\r\n");
		sb.append("import java.util.Date;\r\n");
		sb.append("import java.math.BigDecimal;\r\n");
		if(schema==null||"".equals(schema.trim())){
			sb.append("//" + tablename + "\r\n");
		}else{
			sb.append("//" + tablename + ",schema=" + schema + "\r\n");
		}
		sb.append("public class " + entityName + " implements java.io.Serializable{\r\n");
		processAllAttrs(sb);
		processAllMethod(sb);
		sb.append("}\r\n");
		return sb.toString();
	}

	private String parseMapper() {
		StringBuffer sb = new StringBuffer();
		sb.append("package " + this.mapperPath + ";\r\n");
		sb.append("import com.eim.oa.inter.BaseMapper;\r\n");
		sb.append("import "+this.entityPath+"."+this.entityName+";\r\n");
		sb.append("\r\n");
		sb.append("public interface " + this.entityName + "Mapper extends BaseMapper<"+entityName+">{\r\n");
		sb.append("}\r\n");
		return sb.toString();
	}

	private String parseService() {
		StringBuffer sb = new StringBuffer();
		sb.append("package " + this.servicePath + ";\r\n");
		sb.append("import com.emmc.framework.service.IBaseService;\r\n");
		sb.append("import "+this.entityPath+"."+this.entityName+";\r\n");
		sb.append("\r\n");
		sb.append("public interface I" + this.entityName + "Service extends IBaseService<"+entityName+">{\r\n");
		sb.append("}\r\n");
		return sb.toString();
	}

	private String parseServiceImpl() {
		StringBuffer sb = new StringBuffer();
		sb.append("package " + this.servicePath + ".impl;\r\n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
		sb.append("import org.springframework.stereotype.Service;\r\n");
		sb.append("import com.emmc.framework.service.BaseService;\r\n");
		sb.append("import "+this.entityPath+"."+this.entityName+";\r\n");
		sb.append("import "+this.mapperPath+"."+this.entityName+"Mapper;\r\n");
		sb.append("import "+this.servicePath+".I"+this.entityName+"Service;\r\n");
		sb.append("\r\n");
		sb.append("@Service\r\n");
		sb.append("public class " + this.entityName + "ServiceImpl extends BaseService<"+entityName+"> implements I" + this.entityName + "Service{\r\n");
		sb.append("\t@Autowired\r\n");
		String mapperVar=this.entityName.substring(0,1).toLowerCase()+this.entityName.substring(1);
		sb.append("\tprivate " + this.entityName + "Mapper " + mapperVar+ "Mapper;\r\n");
		sb.append("\t@Autowired\r\n");
		sb.append("\tpublic void setBaseMapper(){super.setBaseMapper("+mapperVar+"Mapper);}\r\n\n");
		sb.append("}\r\n");
		return sb.toString();
	}

	private String parseXML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
		sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n");
		sb.append("\r\n");
		sb.append("<mapper namespace=\""+this.mapperPath+"."+this.entityName+"Mapper\">\r\n");
		sb.append("\t<sql id=\"column\">\r\n");
		for (int i = 0; i < colnames.length; i++) {
			sb.append("\t\tt." + colnames[i] + " " + getAttrName(colnames[i]) + ",\r\n");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("\t</sql>\r\n");
		sb.append("\t<sql id=\"condition\">\r\n");
		for (int i = 0; i < colnames.length; i++) {
			sb.append("\t\t\t<if test=\""+getAttrName(colnames[i])+" != null and "+getAttrName(colnames[i])+" != ''\">");
			sb.append("AND t."+colnames[i]+" = #{"+getAttrName(colnames[i])+"}</if>\r\n");
		}
		sb.append("\t</sql>\r\n");
		sb.append("\t<select id=\"get\" parameterType=\"java.util.Map\" resultType=\""+this.entityPath+"."+this.entityName+"\">\r\n");
		sb.append("\t\tSELECT\r\n");
		sb.append("\t\t\t<include refid=\"column\"/>\r\n");
		if(schema==null||"".equals(schema.trim())){
			sb.append("\t\tFROM " + tablename + " t\r\n");
		}else{
			sb.append("\t\tFROM " + schema + "." + tablename + " t\r\n");
		}
		sb.append("\t\t<trim prefix=\"WHERE\" prefixOverrides=\"AND |OR \">\r\n");
		sb.append("\t\t\t<include refid=\"condition\"/>\r\n");
		sb.append("\t\t</trim>\r\n");
		sb.append("\t</select>\r\n");
		sb.append("\t<select id=\"getList\" parameterType=\"java.util.Map\" resultType=\""+this.entityPath+"."+this.entityName+"\">\r\n");
		sb.append("\t\tSELECT\r\n");
		sb.append("\t\t\t<include refid=\"column\"/>\r\n");
		if(schema==null||"".equals(schema.trim())){
			sb.append("\t\tFROM " + tablename + " t\r\n");
		}else{
			sb.append("\t\tFROM " + schema + "." + tablename + " t\r\n");
		}
		sb.append("\t\t<trim prefix=\"WHERE\" prefixOverrides=\"AND |OR \">\r\n");
		sb.append("\t\t\t<include refid=\"condition\"/>\r\n");
		sb.append("\t\t</trim>\r\n");
		sb.append("\t</select>\r\n");
		sb.append("\t<insert id=\"insert\" parameterType=\""+this.entityPath+"."+this.entityName+"\">\r\n");
		sb.append("\tinsert into "+tablename+"\r\n");
		sb.append("\t(\r\n");
		for (int i = 0; i < colnames.length; i++) {
			sb.append("\t\t"+colnames[i]+",\r\n");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("\t)\r\n");
		sb.append("\tvalues\r\n");
		sb.append("\t(\r\n");
		for (int i = 0; i < colnames.length; i++) {
			sb.append("\t\t#{"+getAttrName(colnames[i])+"},\r\n");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("\t)\r\n");
		sb.append("\t</insert>\r\n");
		sb.append("\t<update id=\"updateByPrimaryKey\" parameterType=\""+this.entityPath+"."+this.entityName+"\">\r\n");
		sb.append("\t\tUPDATE "+tablename+"\r\n");
		sb.append("\t\t<trim prefix=\"SET\" suffixOverrides=\",\">\r\n");
		for (int i = 0; i < colnames.length; i++) {
			if("ID".equals(colnames[i])){
				continue;
			}
			sb.append("\t\t\t<if test=\""+getAttrName(colnames[i])+" != null\">\r\n");
			sb.append("\t\t\t\t"+colnames[i]+" = #{"+getAttrName(colnames[i])+"},\r\n");
			sb.append("\t\t\t</if>\r\n");
		}
		sb.append("\t\t</trim>\r\n");
		String[]  pksArr= pks.split(",");
		for(int i=0;i<pksArr.length;i++){
			if(i==0){
				sb.append("\t\tWHERE ");
			}else{
				sb.append(" AND ");
			}
			sb.append(pksArr[i]+"=#{"+getAttrName(pksArr[i])+"}");
		}
		sb.append("\r\n");
		sb.append("\t</update>\r\n");
		sb.append("\t<delete id=\"deleteByPrimaryKey\" parameterType=\"java.lang.String\">\r\n");
		sb.append("\t\tDELETE FROM "+tablename);
		for(int i=0;i<pksArr.length;i++){
			if(i==0){
				sb.append(" WHERE ");
			}else{
				sb.append(" AND ");
			}
			sb.append(pksArr[i]+"=#{"+getAttrName(pksArr[i])+"}");
		}
		sb.append("\r\n");
		sb.append("\t</delete>\r\n");
		sb.append("\t<delete id=\"delete\" parameterType=\"java.util.List\">\r\n");
		sb.append("\t\tDELETE FROM "+tablename+" WHERE ID IN\r\n");
		sb.append("\t\t<foreach collection=\"list\" item=\"id\" open=\"(\" separator=\",\" close=\")\">\r\n");
		sb.append("\t\t\t#{id}\r\n");
		sb.append("\t\t</foreach>\r\n");
		sb.append("\t</delete>\r\n");
		sb.append("\t<delete id=\"deleteObj\" parameterType=\"java.util.List\">\r\n");
		sb.append("\t\tDELETE FROM "+tablename+" WHERE ID IN\r\n");
		sb.append("\t\t<foreach collection=\"list\" item=\"obj\" open=\"(\" separator=\",\" close=\")\">\r\n");
		sb.append("\t\t\t#{obj.id}\r\n");
		sb.append("\t\t</foreach>\r\n");
		sb.append("\t</delete>\r\n");
		sb.append("\t<select id=\"getByPrimaryKey\" parameterType=\"java.lang.String\" resultType=\""+this.entityPath+"."+this.entityName+"\">\r\n");
		sb.append("\t\tSELECT\r\n");
		sb.append("\t\t\t<include refid=\"column\"/>\r\n");
		if(schema==null||"".equals(schema.trim())){
			sb.append("\t\tFROM " + tablename + " t\r\n");
		}else{
			sb.append("\t\tFROM " + schema + "." + tablename + " t\r\n");
		}
		for(int i=0;i<pksArr.length;i++){
			if(i==0){
				sb.append("\t\tWHERE ");
			}else{
				sb.append(" AND ");
			}
			sb.append(pksArr[i]+"=#{"+getAttrName(pksArr[i])+"}");
		}
		sb.append("\r\n");
		sb.append("\t</select>\r\n");
		sb.append("</mapper>\r\n");
		sb.append("\r\n");
		return sb.toString();
	}
	
	private String parseAction(){
		StringBuffer sb = new StringBuffer();
		sb.append("package " + this.actionPath + ";\r\n");
		sb.append("import java.util.List;\r\n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
		sb.append("import org.springframework.stereotype.Controller;\r\n");
		sb.append("import org.springframework.web.bind.annotation.RequestMapping;\r\n");
		sb.append("import org.springframework.web.bind.annotation.RequestMethod;\r\n");
		sb.append("import org.springframework.web.bind.annotation.RequestParam;\r\n");
		sb.append("import org.springframework.web.bind.annotation.ResponseBody;\r\n");
		sb.append("import com.emmc.framework.controller.BaseController;\r\n");
		sb.append("import com.emmc.framework.util.Page;\r\n");
		sb.append("import "+this.entityPath+"."+this.entityName+";\r\n");
		sb.append("import "+this.servicePath+".I"+this.entityName+"Service;\r\n");
		sb.append("\r\n");
		sb.append("@Controller\r\n");
		sb.append("@RequestMapping(value=\""+this.actionRequestMappingValue+"\")\r\n");
		sb.append("public class "+this.actionFileName+" extends BaseController {\r\n");
		sb.append("\t@Autowired\r\n");
		String entityNameLowercase=this.entityName.substring(0,1).toLowerCase()+this.entityName.substring(1);
		sb.append("\tprivate I"+this.entityName+"Service "+entityNameLowercase+"Service;\r\n");
		sb.append("\r\n");
		sb.append("\t@RequestMapping(value=\"/index\", method=RequestMethod.GET)\r\n");
		sb.append("\tpublic String index(Page<?> page){\r\n");
		sb.append("\t\treturn \"/"+this.jspPath+"/"+entityNameLowercase.toLowerCase()+".index\";\r\n");
		sb.append("\t}\r\n");
		sb.append("\r\n");
		sb.append("\t@ResponseBody\r\n");
		sb.append("\t@RequestMapping(value=\"/list\", method=RequestMethod.POST)\r\n");
		sb.append("\tpublic Page<?> list(Page<?> page) {\r\n");
		sb.append("\t\tList<"+this.entityName+"> list = "+entityNameLowercase+"Service.getList(page.getParams());\r\n");
		sb.append("\t\tpage.setList(list);\r\n");
		sb.append("\t\treturn page;\r\n");
		sb.append("\t}\r\n");
		sb.append("\r\n");
		sb.append("\t@RequestMapping(value=\"/form\", method=RequestMethod.GET)\r\n");
		sb.append("\tpublic String form(Page<?> page){\r\n");
		sb.append("\t\treturn \"/"+this.jspPath+"/"+entityNameLowercase.toLowerCase()+".form\";\r\n");
		sb.append("\t}\r\n");
		sb.append("\r\n");
		sb.append("\t@ResponseBody\r\n");
		sb.append("\t@RequestMapping(value=\"/save\", method=RequestMethod.POST)\r\n");
		sb.append("\tpublic Page<?> save(Page<?> page,"+this.entityName+" "+entityNameLowercase+"){\r\n");
		sb.append("\t\t"+entityNameLowercase+"Service.save("+entityNameLowercase+");\r\n");
		sb.append("\t\treturn page;\r\n");
		sb.append("\t}\r\n");
		sb.append("\r\n");
		sb.append("\t@ResponseBody\r\n");
		sb.append("\t@RequestMapping(value=\"/delete\", method=RequestMethod.POST)\r\n");
		sb.append("\tpublic Page<?> delete(Page<?> page, @RequestParam(\"ids[]\") List<String> ids) {\r\n");
		sb.append("\t\t"+entityNameLowercase+"Service.delete(ids);\r\n");
		sb.append("\t\treturn page;\r\n");
		sb.append("\t}\r\n");
		sb.append("}\r\n");
		return sb.toString();
	}
	private String parseJsp(){
		return "";
	}
	private String parseJs(){
		return "";
	}
	private void processAllAttrs(StringBuffer sb) {
		sb.append("\tprivate static final long serialVersionUID = 1L;\r\n");
		for (int i = 0; i < colnames.length; i++) {
			sb.append("\t/** "+remarks[i]+" */\r\n");
			sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + getAttrName(colnames[i]) + ";\r\n");
		}
	}
	private void processAllMethod(StringBuffer sb) {
		for (int i = 0; i < colnames.length; i++) {
			sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + getEntityName(colnames[i]) + "(){\r\n");
			sb.append("\t\treturn " + getAttrName(colnames[i]) + ";\r\n");
			sb.append("\t}\r\n");
			sb.append("\tpublic void set" + getEntityName(colnames[i]) + "(" + sqlType2JavaType(colTypes[i]) + " " + getAttrName(colnames[i]) + "){\r\n");
			sb.append("\t\tthis." + getAttrName(colnames[i]) + "=" + getAttrName(colnames[i]) + ";\r\n");
			sb.append("\t}\r\n");
		}
	}
	private String initcap(String str) {
		str = str.toLowerCase();
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}

		return new String(ch);
	}
	private String sqlType2JavaType(String sqlType) {

		if (sqlType.equalsIgnoreCase("binary_double")||sqlType.equalsIgnoreCase("double")) {
			return "Double";
		} else if (sqlType.equalsIgnoreCase("binary_float")||sqlType.equalsIgnoreCase("float")) {
			return "float";
		} else if (sqlType.equalsIgnoreCase("blob")) {
			return "byte[]";
		} else if (sqlType.equalsIgnoreCase("int") || sqlType.equalsIgnoreCase("integer")||sqlType.equalsIgnoreCase("smallint")) {
			return "Integer";
		} else if (sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("nvarchar2") || sqlType.equalsIgnoreCase("varchar2")||sqlType.equalsIgnoreCase("varchar")) {
			return "String";
		} else if (sqlType.equalsIgnoreCase("date") || sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("timestamp") || sqlType.equalsIgnoreCase("timestamp with local time zone") || sqlType.equalsIgnoreCase("timestamp with time zone")) {
			return "Date";
		} else if (sqlType.equalsIgnoreCase("number")) {
			return "Double";
		}else if (sqlType.equalsIgnoreCase("bigint")) {
			return "Long";
		}else if (sqlType.equalsIgnoreCase("decimal")) {
			return "BigDecimal";
		}

		return "";
	}

	private String getEntityName(String tableName) {
		String entityName = "";
		String[] names = tableName.split("_");
		for (String n : names) {
			entityName = entityName + initcap(n);
		}
		return entityName;
	}
	private String getAttrName(String colName) {
		String attrName = "";
		String[] names = colName.split("_");
		for (int i = 0; i < names.length; i++) {
			if (i == 0) {
				attrName = attrName + names[i].toLowerCase();
			} else {
				attrName = attrName + initcap(names[i]);
			}
		}
		return attrName;
	}
}