package org.simple.dto;

import lombok.Data;

import java.io.InputStream;
import java.util.List;

@Data
public class FIleDto {

    private List<File> fileList;
    private String nextMarker;
    private String fileName;
    private InputStream input;

    private byte[] fileBytes;
}
