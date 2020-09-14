package com.store.common.repository;

import java.util.Map;

/**
 * 功能：阿里云oss相关的操作
 * @author sunpeng
 * @date 2018
 */
public interface AliOssRepository {

    /**
     * 功能：前端获取上传到oss时的签名，dir参数是上传的路径
     * 备注：例如，上传房屋的图片，上传的bucket是4zlinkimg,
     *      盛行天下在这个bucket的权限只有sxtximg这个文件夹的读写权限，
     *      那个上传时的路径是：sxtximg/house/,这个时候参数dir传递的是house/
     * @param dir
     * @return
     */
    Map<String, String> getOssPolicy(String dir);

    /**
     * 功能：上传文件
     * @param sourcePath 文件的本地路径
     * @param filename oss上路径，说明同上，fileName是house/123.jpg
     * @return
     */
    int uploadFile(String sourcePath, String filename);

    /**
     * 功能：删除oss上的图片
     * @param picPath 同uploadFile的filename
     * @return
     */
    int deleteFile(String picPath);
}