package org.smart4j.framework.bean;

import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author psl
 * @date 2020/7/6
 *  请求参数对象
 */
public class Param {
    private List<FormParam> formParamList;
    private List<FileParam> fileParamList;

//    private Map<String, Object> paramMap;

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }
    /**
     * 获取请求参数映射
     */
    public Map<String, Object> getFieldMap() {
        Map<String, Object> fieldMap = new HashMap<>();

        if (CollectionUtil.isNotEmpty(formParamList)) {
            // 普通字段
            for(FormParam formParam : formParamList){
                String fieldName = formParam.getFieldName();
                Object fieldValue = formParam.getFieldValue();
                if (fieldMap.containsKey(fieldName)) {
                    // 分割器
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
                }
                fieldMap.put(fieldName, fieldValue);
            }
        }
        return fieldMap;
    }
    /**
     * 获取上传文件映射
     *
     */
    public Map<String, List<FileParam>> getFileMap() {
        // 针对同名上传多个文件
        Map<String, List<FileParam>> fileMap = new HashMap<>();

        if (CollectionUtil.isNotEmpty(fileParamList)) {
            for (FileParam fileParam : fileParamList){
                String fieldName = fileParam.getFieldName();
                List<FileParam> fileParamList;
                if (fileMap.containsKey(fieldName)) {
                    fileParamList = fileMap.get(fieldName);
                }else {
                    fileParamList = new ArrayList<>();
                }
                fileParamList.add(fileParam);
                fileMap.put(fieldName, fileParamList);
            }
        }
        return fileMap;
    }
    /**
     * 获取所有同名上传的所有文件
     */
    public List<FileParam> getFileList(String fieldName) {
        return getFileMap().get(fieldName);
    }
    /**
     * 获取唯一上传文件
     */
    public FileParam getFile(String fieldName) {
        List<FileParam> fileParamList = getFileList(fieldName);
        if (CollectionUtil.isNotEmpty(fileParamList) && fileParamList.size() == 1){
            return fileParamList.get(0);
        }
        return null;
    }
    /**
     * 验证参数是否为空
     */
    public boolean isEmpty() {
        return CollectionUtil.isEmpty(formParamList) && CollectionUtil.isEmpty(fileParamList);
    }
    /**
     * 根据参数名获取String型参数
     */
    public String getString(String name) {
        return getFieldMap().get(name).toString();
    }
    /**
     * 根据参数名获取double型参数
     */
    public double getDouble(String name) {
        return Double.parseDouble(getFieldMap().get(name).toString());
    }
    /**
     * 根据参数名获取String型参数
     */
    public long getSLong(String name) {
        return Long.parseLong(getFieldMap().get(name).toString());
    }
    /**
     * 根据参数名获取String型参数
     */
    public boolean getBoolean(String name) {
        return Boolean.parseBoolean(getFieldMap().get(name).toString());
    }


//    public Param(Map<String, Object> paramMap) {
//        this.paramMap = paramMap;
//    }

    /**
     * 根据参数名获取 long 型参数
     * @param name
     * @return
     */
    public long getLong(String name) {
        return Long.parseLong(name);
    }

    /**
     * 获取所有字段信息
     * @return
     */
//    public Map<String, Object> getMap() {
//        return this.paramMap;
//    }

    /**
     * 验证参数是否为空
     * @return
     */
//    public boolean isEmpty() {
//        return CollectionUtil.isEmpty(paramMap);
//    }
}
