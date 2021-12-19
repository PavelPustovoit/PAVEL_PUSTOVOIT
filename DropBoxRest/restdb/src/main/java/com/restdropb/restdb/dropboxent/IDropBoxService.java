package com.restdropb.restdb.dropboxent;

import java.io.File;
import java.util.List;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeleteErrorException;

public interface IDropBoxService {
    void setAppDbxClient(String app,String token);
    DbxClientV2 getDbxClient();
    String sendFileToDropBox(String pathFile, String dropboxPath);
    void uploadFile( File localFile, String dropboxPath);
    List<String> GetFiles( String pathFolder, String err);
    String deleteFile( String dbxFile, String pathFolder);
}
