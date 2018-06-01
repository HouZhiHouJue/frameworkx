package com.luckytiger.framework.dfs;

import com.luckytiger.framework.dfs.internals.Exception.HttpException;

import java.io.File;
import java.util.concurrent.ExecutionException;

public interface DfsClient {
    UploadResult upload(FileInfo fileInfo) throws ExecutionException, InterruptedException, HttpException;

    UploadResult upload(FileInfo fileInfo, int requestTimeout) throws InterruptedException, ExecutionException, HttpException;

    UploadResult upload(FileInfo fileInfo, int requestTimeout, int maxRetry) throws HttpException, ExecutionException, InterruptedException;
}