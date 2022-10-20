package app;

import helpers.Utils;
import models.User;
import models.game.Game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String hoverBefore = "#dff9f4";
        String hoverAfter = "#e8be2b";

        String textColoBefore = "#35D7BB";
        String textColoAfter = "#11362f";

        String backBefore = "<p class=\"has-line-data\" data-line-start=\"\\d+\" data-line-end=\"\\d+\"><a href=\".+\">к оглавлению</a></p>";
        String backAfter = "<p class=\"has-line-data\" data-line-start=\"27\" data-line-end=\"28\"><a href=\"./index.html\"><b>к оглавлению</b></a></p>";

        List<File> f = new ArrayList<>();
        listf("/Users/o.pendrak/Desktop/theory", f);
        List<File> htmls = f.stream().filter(x->!x.getName().equals(".DS_STORE")).collect(Collectors.toList());

  
        int a = 9;
    }

    public static void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
                }
            }
    }
}
