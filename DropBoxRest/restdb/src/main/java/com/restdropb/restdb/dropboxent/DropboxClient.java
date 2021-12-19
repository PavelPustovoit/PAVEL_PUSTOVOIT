package com.restdropb.restdb.dropboxent;

import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.NetworkIOException;
import com.dropbox.core.RetryException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.util.IOUtil.ProgressListener;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxPathV2;
import com.dropbox.core.v2.files.CommitInfo;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.dropbox.core.v2.files.DeleteResult;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderContinueErrorException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadBuilder;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.UploadSessionCursor;
import com.dropbox.core.v2.files.UploadSessionFinishErrorException;
import com.dropbox.core.v2.files.UploadSessionLookupErrorException;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.taglibs.standard.lang.jstl.test.Bean2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

//@Configuration
//@PropertySource("classpath:resources/application.properties")
//@PropertySource("file:${app.home}/resources/application.properties")
//@Component
@Service
//@ConfigurationProperties(prefix="com.restdropb.restdb")
public class DropboxClient implements IDropBoxService{
	
    private static final long CHUNKED_UPLOAD_CHUNK_SIZE = 8L << 20; // 8MiB
    private static final int CHUNKED_UPLOAD_MAX_ATTEMPTS = 5;
    
    //@Autowired
    //private Environment env;
    //@Value("${accesstoken}")
    //private String TOKEN;
    
    private DbxRequestConfig requestConfig;
    private DbxClientV2 dbxClient;
    
    @PostConstruct                        
   public void init(){
   //get the env properties or throw injected bean to init other bean
   
   //this.TOKEN=env.getProperty("${app.access_token}");
   }
   @Override
    public void setAppDbxClient(String app,String token){
        requestConfig = new DbxRequestConfig(app);
        //String token="_pKlXmbNMjQAAAAAAAAAAaYEpIwohK0Oi85baKzTXYxKYGlbcAFCX1Fz5jcMwTYqV";
        setDbxClient(new DbxClientV2(requestConfig,token));
    }
    @Override
    public DbxClientV2 getDbxClient() {
        return dbxClient;
    }
    
    public void setDbxClient(DbxClientV2 dbxClient) {
        this.dbxClient = dbxClient;
    }

    @Override
    public String sendFileToDropBox(String pathFile, String dropboxPath){
        String errPath=utils.CheckDropBoxPath(dropboxPath);
        if(errPath=="")
            errPath="\\";

        errPath="";
        File localFile= utils.CheckExistsLocalTempFile(pathFile,errPath);
        if(errPath!="")
            return errPath;
        
        if (localFile.length() <= (2 * CHUNKED_UPLOAD_CHUNK_SIZE)) {
                  uploadFile( localFile, dropboxPath);
        } else {
                  chunkedUploadFile(localFile, dropboxPath);
        }
        return "";
    }
    /**
     * Uploads a file in a single request. This approach is preferred for small files since it
     * eliminates unnecessary round-trips to the servers.
     *
     *  @param dropboxPath Where to upload the file to within Dropbox
     */
    @Override
    public void uploadFile( File localFile, String dropboxPath) {
        try (InputStream in = new FileInputStream(localFile)) {
            ProgressListener progressListener = l -> utils.printProgress(l, localFile.length());

            UploadBuilder a= dbxClient.files().uploadBuilder(dropboxPath).withMode(WriteMode.ADD);
            UploadBuilder b=a.withClientModified(new Date(localFile.lastModified()));
            FileMetadata metadata=b.uploadAndFinish(in, progressListener);
            // FileMetadata metadata = dbxClient.files().uploadBuilder(dropboxPath)
            //     .withMode(WriteMode.ADD)
            //     .withClientModified(new Date(localFile.lastModified()))
            //     .uploadAndFinish(in, progressListener);

            System.out.println(metadata.toStringMultiline());
        } catch (UploadErrorException ex) {
            System.err.println("Error uploading to Dropbox: " + ex.getMessage());
          
        } catch (DbxException ex) {
            System.err.println("Error uploading to Dropbox: " + ex.getMessage());
           
        } catch (IOException ex) {
            System.err.println("Error reading from file \"" + localFile + "\": " + ex.getMessage());
            
        }
    }
    
    /**
     *  Get files and folder metadata from Dropbox root directory
     * @param pathFolder
     * @return
     * @throws ListFolderErrorException
     * @throws DbxException
     */
    @Override
    public List<String> GetFiles( String pathFolder, String err){ //throws ListFolderErrorException, DbxException{
        var listFiles= new ArrayList<String>();
        ListFolderResult result=null;
        try {
            result = dbxClient.files().listFolder(pathFolder);
        }catch (ListFolderContinueErrorException e) {
            e.printStackTrace();
        } catch (DbxException e) {
            e.printStackTrace();
            err=e.getMessage();
            return listFiles;
        }
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower()+" file:"+metadata.getName());
               
                listFiles.add(metadata.getName());
                
            }

            if (!result.getHasMore()) {
                break;
            }

            try {
                result = dbxClient.files().listFolderContinue(result.getCursor());
            } catch (ListFolderContinueErrorException e) {
                e.printStackTrace();
            } catch (DbxException e) {
                e.printStackTrace();
            }
        }
        return listFiles;
    }
    
    /**
     * 
     * @param dbxFile
     * @param pathFolder
     * @throws DeleteErrorException
     * @throws DbxException
     */
    @Override
    public String deleteFile(String pathFolder, String dbxFile ) {//throws DeleteErrorException, DbxException{
        String errPath=utils.CheckDropBoxPath(pathFolder);
        if(errPath!="")
            return errPath;

        ListFolderResult result=null;
        try {
            result = dbxClient.files().listFolder(pathFolder);
        } catch (ListFolderErrorException e) {
            return e.getMessage();
        } catch (DbxException e) {
            return e.getMessage();
        }
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower()+" file:"+metadata.getName());
                if(metadata.getName().equals(dbxFile)){
                    DeleteResult del;
                    try {
                        String p=metadata.getPathLower();
                        del = dbxClient.files().deleteV2(p);
                    } catch (DeleteErrorException e) {
                        return e.getMessage();
                    } catch (DbxException e) {
                        return e.getMessage();
                    }
                    if(del!=null){
                        System.out.println("delete success "+dbxFile);
                        return "";
                    }
                }
            }

            if (!result.getHasMore()) {
                break;
            }

            try {
                result = dbxClient.files().listFolderContinue(result.getCursor());
            } catch (ListFolderContinueErrorException e) {
                return e.getMessage();
            } catch (DbxException e) {
                return e.getMessage();
            }
        }

        return "";
    }
    /**
     * Uploads a file in chunks using multiple requests. This approach is preferred for larger files
     * since it allows for more efficient processing of the file contents on the server side and
     * also allows partial uploads to be retried (e.g. network connection problem will not cause you
     * to re-upload all the bytes).
     *
     * @param dropboxPath Where to upload the file to within Dropbox
     */
    private void chunkedUploadFile(File localFile, String dropboxPath) {
        long size = localFile.length();

        // assert our file is at least the chunk upload size. We make this assumption in the code
        // below to simplify the logic.
        if (size < CHUNKED_UPLOAD_CHUNK_SIZE) {
            System.err.println("File too small, use upload() instead.");
            System.exit(1);
            return;
        }

        long uploaded = 0L;
        DbxException thrown = null;

        ProgressListener progressListener = new ProgressListener() {
            long uploadedBytes = 0;
            @Override
            public void onProgress(long l) {
                utils.printProgress(l + uploadedBytes, size);
                if (l == CHUNKED_UPLOAD_CHUNK_SIZE) uploadedBytes += CHUNKED_UPLOAD_CHUNK_SIZE;
            }
        };

        // Chunked uploads have 3 phases, each of which can accept uploaded bytes:
        //
        //    (1)  Start: initiate the upload and get an upload session ID
        //    (2) Append: upload chunks of the file to append to our session
        //    (3) Finish: commit the upload and close the session
        //
        // We track how many bytes we uploaded to determine which phase we should be in.
        String sessionId = null;
        for (int i = 0; i < CHUNKED_UPLOAD_MAX_ATTEMPTS; ++i) {
            if (i > 0) {
                System.out.printf("Retrying chunked upload (%d / %d attempts)\n", i + 1, CHUNKED_UPLOAD_MAX_ATTEMPTS);
            }

            try (InputStream in = new FileInputStream(localFile)) {
                // if this is a retry, make sure seek to the correct offset
                in.skip(uploaded);

                // (1) Start
                if (sessionId == null) {
                    sessionId = dbxClient.files().uploadSessionStart()
                        .uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE, progressListener)
                        .getSessionId();
                    uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
                    utils.printProgress(uploaded, size);
                }

                UploadSessionCursor cursor = new UploadSessionCursor(sessionId, uploaded);

                // (2) Append
                while ((size - uploaded) > CHUNKED_UPLOAD_CHUNK_SIZE) {
                    dbxClient.files().uploadSessionAppendV2(cursor)
                        .uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE, progressListener);
                    uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
                    utils.printProgress(uploaded, size);
                    cursor = new UploadSessionCursor(sessionId, uploaded);
                }

                // (3) Finish
                long remaining = size - uploaded;
                CommitInfo commitInfo = CommitInfo.newBuilder(dropboxPath)
                    .withMode(WriteMode.ADD)
                    .withClientModified(new Date(localFile.lastModified()))
                    .build();
                FileMetadata metadata = dbxClient.files().uploadSessionFinish(cursor, commitInfo)
                    .uploadAndFinish(in, remaining, progressListener);

                System.out.println(metadata.toStringMultiline());
                return;
            } catch (RetryException ex) {
                thrown = ex;
                // RetryExceptions are never automatically retried by the client for uploads. Must
                // catch this exception even if DbxRequestConfig.getMaxRetries() > 0.
                utils.sleepQuietly(ex.getBackoffMillis());
                continue;
            } catch (NetworkIOException ex) {
                thrown = ex;
                // network issue with Dropbox (maybe a timeout?) try again
                continue;
            } catch (UploadSessionLookupErrorException ex) {
                if (ex.errorValue.isIncorrectOffset()) {
                    thrown = ex;
                    // server offset into the stream doesn't match our offset (uploaded). Seek to
                    // the expected offset according to the server and try again.
                    uploaded = ex.errorValue
                        .getIncorrectOffsetValue()
                        .getCorrectOffset();
                    continue;
                } else {
                    // Some other error occurred, give up.
                    System.err.println("Error uploading to Dropbox: " + ex.getMessage());
                    System.exit(1);
                    return;
                }
            } catch (UploadSessionFinishErrorException ex) {
                if (ex.errorValue.isLookupFailed() && ex.errorValue.getLookupFailedValue().isIncorrectOffset()) {
                    thrown = ex;
                    // server offset into the stream doesn't match our offset (uploaded). Seek to
                    // the expected offset according to the server and try again.
                    uploaded = ex.errorValue
                        .getLookupFailedValue()
                        .getIncorrectOffsetValue()
                        .getCorrectOffset();
                    continue;
                } else {
                    // some other error occurred, give up.
                    System.err.println("Error uploading to Dropbox: " + ex.getMessage());
                    System.exit(1);
                    return;
                }
            } catch (DbxException ex) {
                System.err.println("Error uploading to Dropbox: " + ex.getMessage());
                System.exit(1);
                return;
            } catch (IOException ex) {
                System.err.println("Error reading from file \"" + localFile + "\": " + ex.getMessage());
                System.exit(1);
                return;
            }
        }

        // if we made it here, then we must have run out of attempts
        System.err.println("Maxed out upload attempts to Dropbox. Most recent error: " + thrown.getMessage());
        System.exit(1);
    }
}