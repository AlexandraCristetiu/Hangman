package helper;

import constants.ApplicationConstants;
import model.Word;

public class WordParser extends Parser {
    public static Word parseWord(String line){
        String[] wordGroups = line.split(ApplicationConstants.WORD_SEPARATOR_IN_CATEGORY_ENTRY);
        if(wordGroups.length == 2){
            return new Word(Integer.parseInt(wordGroups[0]), wordGroups[1]);
        }
        else if(wordGroups.length == 3){
            return new Word(Integer.parseInt(wordGroups[0]),wordGroups[1], wordGroups[2]);
        }
        return null;
    }
}
