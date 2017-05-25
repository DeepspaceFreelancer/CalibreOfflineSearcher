package com.calibreofflinesearcher;

/**
 * Created by zsoltessig on 23/05/2017.
 */

class Settings {
    private static final Settings ourInstance = new Settings();

    static Settings getInstance() {
        return ourInstance;
    }

    private String LibraryLocation = "/sdcard/normalized.db";

    private Settings() {
    }

    public String getLibraryLocation() {
        return LibraryLocation;
    }

    public void setLibraryLocation(String libraryLocation) {
        LibraryLocation = libraryLocation;
    }
}
