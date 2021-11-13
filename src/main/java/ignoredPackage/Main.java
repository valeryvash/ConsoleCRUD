package ignoredPackage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Tag;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) throws IOException {

        TreeMap<Long,Tag> tags = new TreeMap<>();

        tags.put(1L,new Tag(1,"cats"));
        tags.put(2L,new Tag(2,"dogs"));
        tags.put(3L,new Tag(3,"games"));

        String jsonString = new Gson().toJson(tags);

//        System.out.println(jsonString);

        String pathname = "src/main/java/ignoredPackage/filesDataBase/tags.json";

        File file = new File(pathname);

        FileWriter jsonTags = new FileWriter(pathname);

        jsonTags.write(jsonString);
        jsonTags.close();

        Files.writeString(Paths.get(pathname), jsonString, StandardOpenOption.WRITE);


        String readedFromFile = new String(
                Files.readAllBytes(file.toPath())
        );
//        jsonTagsReader.read(CharBuffer.wrap(readedFromFile));



        System.out.println(readedFromFile);

        Type typeToken = new TypeToken<TreeMap<Long, Tag>>() {
        }.getType();

        tags = new Gson().fromJson(readedFromFile, typeToken);

        for (Map.Entry<Long,Tag> entry:
             tags.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().toString());
        }


    }
}
