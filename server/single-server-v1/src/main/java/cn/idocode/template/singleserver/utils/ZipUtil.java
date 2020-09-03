package cn.idocode.template.singleserver.utils;

import cn.idocode.template.singleserver.exception.ServiceException;
import cn.idocode.template.singleserver.model.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 压缩包操作类
 *
 * @author levicyang
 * 2020-07-28 20:21
 */
@Slf4j
public class ZipUtil {

    private ZipUtil() {
    }

    public static void unzip(File srcFile, String destPath) throws ServiceException {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile);
        } catch (IOException e) {
            log.error("ZipUtil unzip create zip file error. srcFile fileName:" + srcFile.getName() + " destPath:" + destPath, e);
            throw new ServiceException(ResultCode.UNZIP_FILE_ERROR);
        }

        File destFile = new File(destPath);
        if (!destFile.exists()) {
            boolean mkdirResult = destFile.mkdirs();
            if (!mkdirResult) {
                log.error("ZipUtil unzip create destPath dir failed. srcFile fileName:{}, destPath:{}", srcFile.getName(), destPath);
                throw new ServiceException(ResultCode.UNZIP_FILE_ERROR);
            }
        }

        Enumeration<?> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            if (zipEntry.isDirectory()) {
                // 文件夹操作
                String destDirPath = destPath + "/" + zipEntry.getName();
                Path path = Paths.get(destDirPath);
                try {
                    Files.createDirectory(path);
                } catch (IOException e) {
                    log.error("ZipUtil unzip io error create zip dir failed. fileName:" + srcFile.getName() + ", destPath:" + destPath +
                            ", destDirPath:" + destDirPath, e);
                    throw new ServiceException(ResultCode.UNZIP_FILE_ERROR);
                }
            } else {
                // 文件操作
                File targetFile = new File(destFile + "/" + zipEntry.getName());

                // 检查父目录是否存在，不存在创建
                if (!targetFile.getParentFile().exists()) {
                    boolean createDirResult = targetFile.getParentFile().mkdirs();
                    if (!createDirResult) {
                        log.error("ZipUtil unzip create zip parent dir failed. srcFile fileName:{}, destPath:{}, " +
                                "failedCreateParentPath:{}", srcFile.getName(), destPath, targetFile.getParentFile().getAbsolutePath());
                        throw new ServiceException(ResultCode.UNZIP_FILE_ERROR);
                    }
                }

                try (InputStream is = zipFile.getInputStream(zipEntry)) {
                    if (!targetFile.exists()) {
                        boolean createFileResult = targetFile.createNewFile();
                        if (!createFileResult) {
                            log.error("ZipUtil unzip create file error. fileName:{}, destPath:{}, " +
                                    "targetFile:{}", srcFile.getName(), destPath, targetFile.getAbsolutePath());
                            throw new ServiceException(ResultCode.UNZIP_FILE_ERROR);
                        }
                    }
                    try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                        IOUtils.copy(is, fos);
                    }
                } catch (IOException e) {
                    log.error("ZipUtil unzip io error. fileName:" + srcFile.getName() + ", destPath:" + destPath +
                            ", targetFile:" + targetFile.getAbsolutePath(), e);
                    throw new ServiceException(ResultCode.UNZIP_FILE_ERROR);
                }
            }
        }
    }
}
