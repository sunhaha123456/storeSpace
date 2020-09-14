package com.store.common.util;

import com.store.common.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileUtil {

    public static List<String> fileExtList = Arrays.asList(".exe", ".bat", ".sh", ".js", ".html", ".xml");

    /**
     * 功能：获取文件后缀名，比如123.txt，返回 .txt，均以小写返回
     * @param fileName
     * @return
     */
    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }

    public static void fileValidate(List<MultipartFile> fileList, Long fileMaxSizeMByte) {
        if (fileList != null && fileList.size() > 0) {
            for (MultipartFile file : fileList) {
                if (file != null) {
                    if (StringUtil.isEmpty(file.getOriginalFilename()) || !file.getOriginalFilename().contains(".") || fileExtList.contains(getFileExt(file.getOriginalFilename()))) {
                        throw new BusinessException("文件上传失败！");
                    }
                    if (file.getSize() == 0) {
                        throw new BusinessException(file.getOriginalFilename() + "是空文件不允许上传！");
                    }
                    if (fileMaxSizeMByte * 1024 * 1024 < file.getSize()) {
                        throw new BusinessException(file.getOriginalFilename() + "过大最大只能" + fileMaxSizeMByte + "M！");
                    }
                }
            }
        }
    }
}