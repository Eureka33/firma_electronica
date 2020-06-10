package mx.eureka.firma.digital.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import mx.com.neogen.commons.util.UtilStream;


public class ZipCreator {

    public void create( File zipFile, List<String> pathsArchivos) {

        ZipOutputStream zip = null;
        
        try {
            final File parentFile = zipFile.getParentFile();
            parentFile.mkdirs();
            
            zip = new ZipOutputStream( new FileOutputStream( zipFile));
            
            for ( String pathArchivo : pathsArchivos) {
                writeToZipFile( pathArchivo, zip);
            }
            
        } catch ( Exception ex) {
            throw new RuntimeException( "Error al generar archivo ZIP", ex);
        
        } finally {
            UtilStream.close( zip);
            
        }
    }

    private void writeToZipFile( String path, ZipOutputStream zip) throws FileNotFoundException, IOException {

        FileInputStream inputStream = null;
        
        try {
            final File archivo = new File( path);
            
            inputStream = new FileInputStream( archivo);
            zip.putNextEntry( new ZipEntry( archivo.getName()));
            
            byte[] bytes = new byte[ 4096];
            int length;
            while ( (length = inputStream.read( bytes)) >= 0) {
                zip.write( bytes, 0, length);
            }
            
            zip.closeEntry();
        
        } finally {
            UtilStream.close( inputStream);
        
        }
    }
    
}
