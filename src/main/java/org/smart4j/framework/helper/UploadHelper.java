package org.smart4j.framework.helper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.FileParam;
import org.smart4j.framework.bean.FormParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.FileUtil;
import org.smart4j.framework.util.StreamUtil;
import org.smart4j.framework.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author psl
 * @date 2020/7/14
 *  文件上传助手类
 */
public final class UploadHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);
    /**
     * Apache Commons FileUpload 提供的Servlet文件上传对象
     */
    private static ServletFileUpload servletFileUpload;
    /**
     * 初始化
     */
    public static void init(ServletContext servletContext) {
        // public static final String TEMPDIR = "javax.servlet.context.tempdir";
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(
                new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
        int uploadLimit = 100;
        if (uploadLimit != 0){
            servletFileUpload.setFileSizeMax(uploadLimit * 1024 * 1024);
        }
    }

    /**
     * 判断请求是否为multipart类型
     * @param request
     * @return
     */
    public static boolean isMultipart(HttpServletRequest request){
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 创建请求对象
     * @param request
     * @return
     */
    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<>();
        List<FileParam> fileParamList = new ArrayList<>();
        try {
            Map<String, List<FileItem>> fileItemListMap =  servletFileUpload.parseParameterMap(request);

            if (CollectionUtil.isNotEmpty(fileItemListMap)) {
             for (Map.Entry<String, List<FileItem>> fileItemListEntry :fileItemListMap.entrySet()) {
                 String fieldName = fileItemListEntry.getKey();
                 List<FileItem> fileItemList = fileItemListEntry.getValue();

                 if (CollectionUtil.isNotEmpty(fileItemList)) {
                     for (FileItem fileItem : fileItemList) {
                         // 是否是简单表单字段
                         if (fileItem.isFormField()) {
                             String fieldValue = fileItem.getString("UTF-8");
                             formParamList.add(new FormParam(fieldName, fieldValue));
                         }else {
                             // 返回真实文件名（不含路径）
                             String filename = FileUtil.getRealFileName(new String(fileItem.
                                     getName().getBytes(), "UTF-8"));
                             if (StringUtil.isNotEmpty(fieldName)) {
                                 long fileSize = fileItem.getSize();
                                 String contentType = fileItem.getContentType();
                                 InputStream inputStream = fileItem.getInputStream();
                                 fileParamList.add(new FileParam(fieldName, filename, fileSize, contentType, inputStream));
                             }
                         }
                     }
                 }
             }
            }
        }catch (Exception e) {
            LOGGER.error("create param failure", e);
            throw new RuntimeException(e);
        }
        return new Param(formParamList, fileParamList);
    }

    /**
     * 上传文件
     * @param basePath
     * @param fileParam
     */
    public static void uploadFile(String basePath, FileParam fileParam) {
        try {
            if (fileParam != null) {
                String filePath = basePath + fileParam.getFieldName();
                FileUtil.createFile(filePath);
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                // 拷贝到filePath
                StreamUtil.copyStream(inputStream, outputStream);
            }
        } catch (Exception e) {
            LOGGER.error("upload file failure", e);
            throw new RuntimeException(e);
        }
    }

    public static void uploadFile(String basePath, List<FileParam> fileParamList) {
        try {
            if (CollectionUtil.isNotEmpty(fileParamList)) {
                for (FileParam fileParam : fileParamList) {
                    uploadFile(basePath, fileParam);
                }
            }
        }catch (Exception e) {
            LOGGER.error("upload file failure", e);
            throw new RuntimeException(e);
        }
    }
}
