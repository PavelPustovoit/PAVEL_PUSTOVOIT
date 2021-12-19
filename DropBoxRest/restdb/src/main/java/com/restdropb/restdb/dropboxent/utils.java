package com.restdropb.restdb.dropboxent;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.dropbox.core.v2.DbxPathV2;

import org.springframework.web.multipart.MultipartFile;

public class utils {
    
    
    static void printProgress(long uploaded, long size) {
        System.out.printf("Uploaded %12d / %12d bytes (%5.2f%%)\n", uploaded, size, 100 * (uploaded / (double) size));
    }

    static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            // just exit
            System.err.println("Error uploading to Dropbox: interrupted during backoff.");
            System.exit(1);
        }
    }
    public static String saveFile(String fullPath, MultipartFile file)  {
        
        final Path root = Paths.get(fullPath);
        try {
          Files.copy(file.getInputStream(), root.resolve( file.getOriginalFilename()));
        } catch (Exception e) {
          throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return root+"/"+file.getOriginalFilename();
      }

      public static String CheckDropBoxPath(String dropboxPath){
        String pathError = DbxPathV2.findError(dropboxPath);
        if (pathError != null) {
            return "Invalid <dropbox-path>: " + pathError;
        }
        return "";
    }
    public static File CheckExistsLocalTempFile(String localPathToFile,String err){
       File localFile = new File(localPathToFile);
        if (!localFile.exists()) {
            err="Invalid <local-path>: file does not exist.";

        }
        return localFile;
    }

}
