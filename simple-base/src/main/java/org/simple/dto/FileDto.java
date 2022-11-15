package org.simple.dto;

import lombok.Data;

import java.io.InputStream;
import java.util.List;

/**
 * FileDto
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
public class FileDto {

    private List<File> fileList;
    private String nextMarker;
    private String fileName;
    private InputStream input;

    private byte[] fileBytes;
}
