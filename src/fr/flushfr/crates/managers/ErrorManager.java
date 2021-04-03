package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.Error;
import fr.flushfr.crates.objects.animation.data.SoundData;
import fr.flushfr.crates.utils.ErrorCategory;
import fr.flushfr.crates.utils.ErrorType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static fr.flushfr.crates.Main.getMainInstance;

public class ErrorManager {

    private static ErrorManager instance;
    public ErrorManager () {
        instance = this;
    }
    public static ErrorManager getInstance() {
        return instance;
    }

    public void addError (Error e) {
        if (e.getErrorCategory() == ErrorCategory.ITEM) {
            getMainInstance().errorList.add("File: "+e.getFileName() +", Item '"+e.getErrorSectionName()+"', "+ e.getVariableName()+" "+e.getErrorType());
        }
        if (e.getErrorCategory() == ErrorCategory.MESSAGE) {
            getMainInstance().errorList.add("File: "+e.getFileName() +", Message '"+e.getErrorSectionName()+"', "+ e.getVariableName()+" "+e.getErrorType());
        }
        if (e.getErrorCategory() == ErrorCategory.ANIMATION) {
            getMainInstance().errorList.add("File: "+e.getFileName() +", "+e.getVariableName()+" in '"+e.getErrorSectionName()+"' "+e.getErrorType());
        }
        if (e.getErrorCategory() == ErrorCategory.SOUND) {
            getMainInstance().errorList.add("File: "+e.getFileName() +", "+e.getVariableName()+" in '"+e.getErrorSectionName()+"' "+e.getErrorType());
        }
    }

    public int getInt(FileConfiguration f, String path) {
        return getInt(f, path,  new Error());
    }

    public int getInt(FileConfiguration f, String path, Error e) {
        if (!f.contains(path)) {
            e.setErrorType(ErrorType.UNDEFINED);
            addError(e);
            return 1;
        } else if (!(f.get(path) instanceof Integer)) {
            e.setErrorType(ErrorType.INCORRECT_INTEGER);
            addError(e);
            return 1;
        }
        return f.getInt(path);
    }

    public String getString(FileConfiguration f, String path) {
        return getString(f,path, new Error());
    }

    public String getString(FileConfiguration f, String path,  Error e) {
        String s = f.getString(path);
        if (!f.contains(path)) {
            e.setErrorType(ErrorType.UNDEFINED);
            addError(e);
            return "";
        } else if (!(f.get(path) instanceof String)) {
            e.setErrorType(ErrorType.INCORRECT_STRING);
            addError(e);
            return "";
        }
        return s;
    }

    public List<String> getStringList(FileConfiguration f, String path) {
        return getStringList(f, path,  new Error());
    }

    public List<String> getStringList(FileConfiguration f, String path, Error e) {
        if (!f.contains(path)) {
            e.setErrorType(ErrorType.UNDEFINED);
            addError(e);
            return new ArrayList<>();
        } else if (!((f.get(path) instanceof List) || (f.get(path) instanceof String))) {
            e.setErrorType(ErrorType.INCORRECT_STRINGLIST);
            addError(e);
            return new ArrayList<>();
        }
        List<String> list = f.getStringList(path);
        if (list.isEmpty()) {
            if (!getString(f, path).isEmpty()) {
                list.add(getString(f, path));
            }
        }
        return  list;
    }

    public Boolean getBoolean(FileConfiguration f, String path) {
        return getBoolean(f, path ,  new Error());
    }

    public Boolean getBoolean(FileConfiguration f, String path,  Error e) {
        if (!f.contains(path)) {
            e.setErrorType(ErrorType.UNDEFINED);
            addError(e);
        } else if (!(f.get(path) instanceof Boolean)) {
            e.setErrorType(ErrorType.INCORRECT_BOOLEAN);
            addError(e);
        }
        return f.getBoolean(path);
    }

    public ItemStack getItemFromConfig (String material,  Error e) {
        Material m = Material.getMaterial(material);
        if (m == null) {
            e.setErrorType(ErrorType.INCORRECT_MATERIAL_ID);
            addError(e);
            return new ItemStack(Material.STONE);
        }
        return new ItemStack(m,1);
    }

    public SoundData getSound(Error error, String soundName, float volume, float pitch) {
        Sound s;
        try {
            s = Sound.valueOf(soundName);
        } catch (IllegalArgumentException e) {
            error.setErrorType(ErrorType.INCORRECT_SOUND);
            addError(error);
            s = Sound.GLASS;
        }
        if (volume==0) {
            volume = 1;
        }
        if (pitch==0) {
            pitch = 1;
        }
        return new SoundData(s,  volume, pitch);
    }
}
