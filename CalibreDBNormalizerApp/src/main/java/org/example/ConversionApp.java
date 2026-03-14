package org.example;

import org.dbutil.DBNormalizerSecond;
import org.dbutil.DBNormalizer;

import java.io.IOException;
import java.sql.SQLException;

public class ConversionApp {

    public static void main(String[] args) throws SQLException, IOException {
        DBNormalizerSecond dbBookNormalizer = new DBNormalizerSecond();
        // dbBookNormalizer.doConversion("./data/metadata.db","./data/normalized_new.db");
        // dbBookNormalizer.doConversion("p:\\EkartLaszlo\\Konyvtarak2\\CalibreOfflineLibrary\\metadata.db", "p:\\EkartLaszlo\\Konyvtarak2\\CalibreOfflineLibrary\\normalized.db");
    }
}
