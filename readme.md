
This is a Calibre library offline searcher for Android eReaders. It will search in the in Calibre-s meta data Catalog.
This is special project because it is targeted towards Android Api level 7, to run it on rooted Nook Simple Touch (Glowlight).

Here is how to use it:
1. Put your entire Calibre library on an SD card, including the catalog.
2. Install this software. (If your eReader lacks ES File Manager install that one too).
3. Go to the Program Settings (Beállítások in Hungarian) and select Calibre metadata database (metadata.db).
4. Generate a normalized database (used for faster search) by pressing the button: "Normalizált Adatbázis Frissítés".
5. Get back and Search. Use a Hungarian-English dictinary if in trouble.
6. Simple click (press) is open the book. Long press details.

I wrote this application by having no Android programming experience and sometimes asking help from my cousin.
Any help is welcomed to make the application better.

Here is how to compile:
You need Eclipse to compile this (as I said Api Level 7, Android 2.1).

Download and install: Eclipse for Android Developer

You need an older version of Android SDK:
http://dl-ssl.google.com/android/repository/tools_r19-windows.zip

The follow these steps:
1. Unzip the older Android SDK and set your Eclipse setting to it. This will give you errors and will offer you to open "Android SDK Manager".
2. Open "Android SDK Manager" from Eclipse and install 'Android SDK Tools' and 'Android SDK Platform-tools'. Once done, restart Eclipse.
3. After restart, open "Android SDK Manager" again and install the following:
	* Android SDK Tools -> This is an upgrade.
	* Android SDK Platform-tools -> This is an upgrade.
	* Android SDK Build-tools newest version (Now this is: 19.1)
	* Android 2.1 -> Everything from here.
	* It will say to install Android 4.4.2, but you do need that. (You can choose it and do a compile for your new version).
4. Eclipse restart again.

After this you are ready to compile and run.

