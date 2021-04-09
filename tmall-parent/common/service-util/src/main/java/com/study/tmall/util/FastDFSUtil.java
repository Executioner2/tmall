package com.study.tmall.util;

import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 22:33
 * Versions:1.0.0
 * Description: FastDFS工具类
 */
public class FastDFSUtil {
    static {
        try {
            // 读取FastDFS配置文件，将配置文件中所有的tracker地址读取到内存中
            ClientGlobal.init("fastdfs.conf");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    private FastDFSUtil(){}

    /**
     * 上传文件
     * @param fileBuff 为需要上传的文件的字节数组
     * @param fileExtName 为需要上传的文件的扩展名
     * @return  返回一个String数组，这个数据十分重要，需要妥善保管，建议存入数据库
     *          数组中的第一个元素为文件所在的组名
     *          数组中的第二个元素为文件所在远程路径名
     */
    public static String[] upload(byte[] fileBuff, String fileExtName){
        TrackerServer ts = null;
        StorageServer ss = null;

        try {
            TrackerClient trackerClient = new TrackerClient();
            ts = trackerClient.getConnection();
            ss = trackerClient.getStoreStorage(ts);
            // 定义Storage的客户端对象，需要用这个对象来上传 下载和删除文件
            StorageClient storageClient = new StorageClient(ts, ss);

            String[] result = storageClient.upload_appender_file(fileBuff, fileExtName, null);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } finally {
            if (ss != null){
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ts != null){
                try {
                    ts.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    /**
     * 下载文件
     * @param groupName 需要下载文件所在组名
     * @param remoteFilename 需要下载文件的远程文件名
     * @return 返回字节数组作响应数据
     */
    public static byte[] download(String groupName, String remoteFilename){
        TrackerServer ts = null;
        StorageServer ss = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            ts = trackerClient.getConnection();
            ss = trackerClient.getStoreStorage(ts);
            StorageClient storageClient = new StorageClient(ts, ss);

            byte[] result = storageClient.download_file(groupName, remoteFilename);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } finally {
            if (null != ss){
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != ts) {
                try {
                    ts.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    /**
     * 删除文件
     */
    public static Integer delete(String groupName, String remoteFilename){
        TrackerServer ts = null;
        StorageServer ss = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            ts = trackerClient.getConnection();
            ss = trackerClient.getStoreStorage(ts);
            StorageClient storageClient = new StorageClient(ts, ss);
            /**
             *  参数1： 要删除文件的组名
             *  参数2： 要删除文件的远程文件名
             *  返回一个int类型的数据，返回 0 表示删除成功，返回其它任何非0值都表示删除失败
             *
             */
            Integer result = storageClient.delete_file(groupName, remoteFilename);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } finally {
            if (null != ss){
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != ts){
                try {
                    ts.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
