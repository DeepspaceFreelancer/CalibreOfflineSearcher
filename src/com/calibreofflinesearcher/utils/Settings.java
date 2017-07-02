package com.calibreofflinesearcher.utils;

import java.io.File;

public class Settings {
    private static final Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }

    private String calibreLibraryLocation = "/sdcard/CalibreOfflineLibrary/metadata.db";
    private String libraryLocation = "/sdcard/CalibreOfflineLibrary/normalized.db";
    private String libraryRootLocation = "/sdcard/CalibreOfflineLibrary";

    private Settings() {
    }
    
    public String getCalibreLibraryLocation() {
        return calibreLibraryLocation;
    }
    
    public void setCalibreLibraryLocation(final String calibreLibraryLocation) {
        this.calibreLibraryLocation = calibreLibraryLocation;
        this.libraryRootLocation = getAbsoluteDirectory(this.calibreLibraryLocation);
        this.libraryLocation = this.libraryRootLocation + File.separator + "normalized.db";
    }

    public String getLibraryLocation() {
        return libraryLocation;
    }
    
    public String getLibraryRootLocation() {
        return libraryRootLocation;
    }

    public void setLibraryLocation(final String libraryLocation) {
        this.libraryLocation = libraryLocation;
        this.libraryRootLocation = getAbsoluteDirectory(this.libraryLocation);                
    }
    
    private static String getAbsoluteDirectory(final String path) {
    	final File file = new File(path);
        if (file.exists()) {
        	String absolutePath = file.getAbsolutePath();
        	return absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
        }
        return "";
    }
}
