package com.luckytiger.framework.dfs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileInfo {
    private String fileName;
    private byte[] bytes;

}
