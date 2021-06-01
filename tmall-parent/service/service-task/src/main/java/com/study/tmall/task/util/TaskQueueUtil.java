package com.study.tmall.task.util;

import com.study.tmall.dto.TimerTask;

import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-29 17:47
 * Versions:1.0.0
 * Description:
 */
public class TaskQueueUtil {
    private TaskQueueUtil() {}

    /**
     * 序列化保存倒计时任务到本地文件中去
     * @param queue
     */
    public static void writeTaskQueue(ConcurrentLinkedQueue<TimerTask> queue, String filePath) {
        File file = new File(filePath);
        // 放在try中的流会被自动关闭
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(queue);
            oos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConcurrentLinkedQueue<TimerTask> readTaskQueue(String filePath) {
        ConcurrentLinkedQueue<TimerTask> taskQueue = null;
        File file = new File(filePath);

        // 如果该文件不存在则返回一个新的队列对象
        if (!file.exists()) {
            return new ConcurrentLinkedQueue<>();
        }
        // 放在try中的流会被自动关闭
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            taskQueue = (ConcurrentLinkedQueue<TimerTask>)ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return taskQueue;
    }
}
