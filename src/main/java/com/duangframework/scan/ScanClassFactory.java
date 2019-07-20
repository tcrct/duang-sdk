package com.duangframework.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 类扫描工厂
 * Created by laotang on 2018/6/15.
 */
public class ScanClassFactory {

    private static final Logger logger = LoggerFactory.getLogger(ScanClassFactory.class);
    private static final String CLASS_EXTNAME = ".class";

    /**
     * 遍历项目的class文件
     */
    public static List<Class<?>> getAllClass(String folderPath) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        File dir = new File(folderPath);
        if ((!dir.exists()) || (!dir.isDirectory())) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not exists or not is directory!");
        }
        File[] files = dir.listFiles(ClassFileFilter(dir, CLASS_EXTNAME));
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                classList.addAll(getAllClass(file.getAbsolutePath()));
            } else if (file.isFile()) {
                try {
                    classList.add(Class.forName(file.getAbsolutePath()));
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return classList;
    }

    private static FileFilter ClassFileFilter(final File dir, final String extName) {
        return new FileFilter() {
            @Override
            public boolean accept(File file){
                if (CLASS_EXTNAME.equalsIgnoreCase(extName)) {
                    return ((file.isFile()) && (file.getName().endsWith(extName))) || (file.isDirectory());
                }
                throw new IllegalArgumentException();
            }
        };
    }
}
