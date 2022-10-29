package helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.user.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static helpers.Constants.*;

public class JsonResourceHelper {
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonResourceHelper() {
        List<Module> modules = ObjectMapper.findModules();
        mapper.registerModules(modules);
    }

    public List<String> getAllFileNamesInDir(String dir){
        File dirName = new File(dir);
        List<String> nameOfFile = new ArrayList<>();
        for (File file : dirName.listFiles()) {
            if (file.isFile()) {
                nameOfFile.add(file.getName());
            }
        }
        return nameOfFile;
    }

    public <T> List<T> getAll(String dirPath, Class<T> pojo) {
        List<T> pojos = new ArrayList<>();
        List<String> fileNames = getAllFileNamesInDir(dirPath);
        fileNames.forEach(x -> pojos.add(readFromJson(x, dirPath, pojo)));
        return pojos;
    }

    public <T> T readFromJson(String fileName, String dir, Class<T> pojo){
        File file = new File(dir + fileName);
        try {
            return mapper.readValue(file, pojo);
        } catch (IOException e) {
            return null;
        }
    }

    public String readJsonAsString(String fileName, String dir){
        File file = new File(dir + fileName);
        try {
            return mapper.readValue(file, new TypeReference<String>(){});
        } catch (IOException e) {
            return null;
        }
    }

    public <T> List<T> readListFromJson(String path, Class<T> pojo){
        File file = new File(BASE_JSONS_PATH + path);
        try {
            return mapper.readerForListOf(pojo).readValue(file);
        } catch (IOException e) {
            return null;
        }
    }

    public User readUserFromJson(String path) {
        File file = new File(BASE_USERS_DIR_PATH + path);
        try {
            return mapper.readValue(file, User.class);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean isFileExist(String name, String dir) {
        File file = new File(dir + name);
        return Files.exists(Paths.get(file.getAbsolutePath()));
    }

    public boolean deleteFile(String name, String dir) {
        File file = new File(dir + name);
        try {
            return Files.deleteIfExists(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createFile(String jsonToCreate, String dir, String fileName) {
        try {
            FileWriter file = new FileWriter(dir + fileName);
            file.write(jsonToCreate);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
