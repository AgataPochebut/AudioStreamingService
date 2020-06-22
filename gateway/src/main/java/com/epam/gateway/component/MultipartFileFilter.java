//package com.epam.gateway.component;
//
//import com.epam.gateway.model.FSResource;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import lombok.SneakyThrows;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.http.client.methods.HttpGet;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
//import org.springframework.http.RequestEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.util.FileCopyUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.Part;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//
//@Component
//public class MultipartFileFilter extends ZuulFilter {
//
//    @Value("${fs.defaultFolder}")
//    private String defaultBaseFolder;
//
//    @SneakyThrows
//    @Override
//    public Object run() {
//        final RequestContext context = RequestContext.getCurrentContext();
//
//        HttpServletRequest request = context.getRequest();
//        //сохранить файл на диск и указать путь к файлу
//
//        if(!context.getRequest().getParts().isEmpty()) {
//            //request to storageservice, response resourcedto
//
//            Part part = request.getPart("data");
//
//            File file = new File(defaultBaseFolder, part.getSubmittedFileName());
//            file.getParentFile().mkdirs();
//            FileCopyUtils.copy(part.getInputStream(), new FileOutputStream(file));
//
//            FSResource resource = new FSResource();
//            resource.setName(file.getName());
//            resource.setSize(file.length());
//            resource.setChecksum(DigestUtils.md5Hex(new FileInputStream(file)));
//            resource.setPath(file.getAbsolutePath());
//
//            request.se
//            RequestEntity
//            //request to storageservice, response resourcedto
//            //удалить атрибут data
//            //добавить атрибут resource
//            //request куда было изначально + resourcedto
//
//            HttpGet request = new HttpGet("https://www.google.com/search?q=mkyong");
//
//            // add request headers
//            request.addHeader("custom-key", "mkyong");
//            request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
//
//            HttpServletRequest request1 = new
//            part.delete();
//
//            context.set("requestEntity", new ByteArrayInputStream(baos.toByteArray()));
//        }
//
//        return null;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        // Always execute filter
//        return true;
//    }
//
//    @Override
//    public String filterType() {
//        // Execute before dispatching to microservice
//        return FilterConstants.PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        // Priority within filter chain
//        return FilterConstants.PRE_DECORATION_FILTER_ORDER;
//    }
//}
