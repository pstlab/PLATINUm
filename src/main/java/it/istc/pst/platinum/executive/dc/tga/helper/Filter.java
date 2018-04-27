package it.istc.pst.platinum.executive.dc.tga.helper;

import java.io.File;
import java.io.FilenameFilter;

public class Filter {

    public File[] finder(){
        File dir = new File("/etc");

        return dir.listFiles(new FilenameFilter() { 
                 public boolean accept(File dir, String filename)
                      { return filename.endsWith(".xta"); }
        } );

    }

}