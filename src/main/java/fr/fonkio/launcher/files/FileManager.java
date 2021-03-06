package fr.fonkio.launcher.files;

import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.utils.OperatingSystem;

import java.io.File;

public class FileManager {

    private final String serverName;

    public FileManager(String serverName) {
        this.serverName = serverName;
    }

    public File createGameDir() {
        final String userHome = System.getProperty("user.home");

        final String fileSeparator = File.separator;
        File f;
        switch (OperatingSystem.getCurrentPlatform()) {
            case WINDOWS:
                 f = new File(userHome + fileSeparator + "AppData" + fileSeparator + "Roaming" + fileSeparator + "." + this.serverName);
                 break;
            case MACOS:
                f = new File(userHome + fileSeparator + "Library" + fileSeparator + "Application Support" + fileSeparator + this.serverName);
                break;
            default:
                f = new File(userHome + fileSeparator + "." + this.serverName);
                break;
        }
        if (!f.exists()) {
            boolean created = f.mkdirs();
            if (!created) {
                Main.logger.log("Le dossier n'a pas pu être créé");
            }
        }

        return f;
    }

    /*public File getAssetsFolder() {
        return new File(createGameDir(), "assets");
    }*/
    public File getLauncherLog() { return new File(createGameDir()+"/", "launcher.log"); }
    public File getLauncherProperties() { return new File(createGameDir()+"/", "launcher.properties"); }
    /*public File getNativesFolder() {
        return new File(createGameDir(), "natives");
    }
    public File getLibsFolder() {
        return new File(createGameDir(), "libs");
    }*/
    public File getGameFolder() {
        return createGameDir();
    }
    public File getGameFolder(String version) {
        return new File (createGameDir(), version+"/");
    }
    /*public File getRuntimeFolder() {
        return new File(createGameDir(), "runtime");
    }*/

}
