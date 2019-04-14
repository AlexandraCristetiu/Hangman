package helper;

import constants.ApplicationConstants;
import model.Word;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utility {
    public static void createDirectory(String path) {
        File directory = new File(path);
        if(!directory.exists()){
            directory.mkdir();
        }
    }

    public static void createCategoryFile(String categoryName){

        createDirectory((ApplicationConstants.APP_FOLDER_DATA_PATH + "\\" + ApplicationConstants.CATEGORY_FOLDER_NAME));

        File categoryFile = new File(ApplicationConstants.APP_FOLDER_DATA_PATH
                + "\\" + ApplicationConstants.CATEGORY_FOLDER_NAME
                + "\\" + categoryName + ".txt");
        try {
            categoryFile.createNewFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    public static List<String> listFilesWithoutExtensionFromPath(String path) {

        ArrayList<String> fileNamesWithoutExtension = new ArrayList<>();

        File directory = new File(path);

        for (File file : directory.listFiles()) {
            try{
            fileNamesWithoutExtension.add(file.getName().substring(0,file.getName().indexOf(".")));
        }
    catch(Exception e)
    {

    }
        }

        return fileNamesWithoutExtension;
    }

    public static void addWordInCategory(int id, String newWord, String newHint, String categoryName)
            throws IOException{

        String categoryPath = ApplicationConstants.APP_FOLDER_DATA_PATH + "\\"+
                ApplicationConstants.CATEGORY_FOLDER_NAME +
                "\\"+ categoryName + ApplicationConstants.CATEGORY_FILE_EXTENSION;

        FileWriter fileWriter = new FileWriter(categoryPath, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.append(id + ApplicationConstants.WORD_SEPARATOR_IN_CATEGORY_ENTRY);
        writer.append(newWord);
        if(!newHint.isEmpty()) {
            writer.append(ApplicationConstants.WORD_SEPARATOR_IN_CATEGORY_ENTRY);
            writer.append(newHint);
        }
        writer.newLine();
        writer.close();
        fileWriter.close();

    }

    public static String extractFileNameFromPath(String path){
        File file = new File(path);
        return file.getName().substring(0,file.getName().indexOf("."));
    }
    public static void cleanWordsInCategory(List<Word> words, String categoryName)
            throws IOException{

        String categoryPath = ApplicationConstants.APP_FOLDER_DATA_PATH + "\\"+
                ApplicationConstants.CATEGORY_FOLDER_NAME +
                "\\"+ categoryName + ApplicationConstants.CATEGORY_FILE_EXTENSION;

        FileWriter fileWriter = new FileWriter(categoryPath, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        for(Word word:words) {
            writer.append(word.getId() + ApplicationConstants.WORD_SEPARATOR_IN_CATEGORY_ENTRY);
            writer.append(word.getName());

            if ((word.getHint() != null) && !word.getHint().isEmpty()) {
                writer.append(ApplicationConstants.WORD_SEPARATOR_IN_CATEGORY_ENTRY);
                writer.append(word.getHint());
            }
            writer.newLine();
        }
        writer.close();
        fileWriter.close();

    }
}
