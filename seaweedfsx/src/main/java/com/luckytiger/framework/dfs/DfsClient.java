package com.luckytiger.framework.dfs;

import com.luckytiger.framework.dfs.internals.Exception.HttpException;

import java.io.File;
import java.util.concurrent.ExecutionException;

public interface DfsClient {
    UploadResult upload(FileInfo fileInfo) throws ExecutionException, InterruptedException, HttpException;

    UploadResult upload(FileInfo fileInfo, int requestTimeout) throws ExecutionException, InterruptedException, HttpException;

    UploadResult upload(FileInfo fileInfo, int requestTimeout, int maxRetry) throws ExecutionException, InterruptedException, HttpException;

    DownloadResult download(String fid) throws ExecutionException, InterruptedException, HttpException;

    DownloadResult download(String fid, int requestTimeout) throws ExecutionException, InterruptedException, HttpException;

    DownloadResult download(String fid, int requestTimeout, int maxRetry) throws ExecutionException, InterruptedException, HttpException;
}
