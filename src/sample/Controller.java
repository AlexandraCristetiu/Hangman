package sample;

import constants.ApplicationConstants;
import helper.CategoryParser;
import helper.Utility;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import model.Category;
import model.Word;

import java.io.IOException;
import java.util.Random;

public class Controller {
    public MenuItem mnuItemChangeCategories;
    public TextField txtFieldUsername;
    public PasswordField passwordFieldPPWD;
    public Button btnLogin;
    public TabPane tabPane;
    public Tab tabPlay;
    public Tab tabLogin;
    public Label lblUsername;
    public Label lblPassword;
    public Tab tabCategories;
    public TextField txtFieldsCategoryName;
    public Button btnAddCategory;
    public ComboBox comboboxCategories;
    public TextField txtWordOrLetter;
    public Button btnPlay;
    public TextField txtWord2Guess;
    public ComboBox comboboxCategory;
    public Button btnHint;
    public Label lblHint;
    public Label lblWordTabCategory;
    public TextField txtFieldNewWord;
    public TextField txtFieldNewHint;
    public Button btnAddWord;
    public Label lblCategoryNameCombobox;
    public CheckBox chckBoxCleanWords;
    public ComboBox comboboxSelectCategoryPlay;
    public TextField txtFieldGuess;
    public Button btnGuess;


    public void initialize() {
        tabPane.getTabs().remove(tabLogin);
        tabPane.getTabs().remove(tabCategories);
    }

    public void loginAction(ActionEvent actionEvent) {
        if (btnLogin.getText().equals(ApplicationConstants.BTN_LOGIN_TEXT)) {

            //correct username and password
            if (txtFieldUsername.getText().equals(ApplicationConstants.APP_USERNAME) &&
                    passwordFieldPPWD.getText().equals(ApplicationConstants.APP_PASSWORD)) {


                btnLogin.setText(ApplicationConstants.BTN_LOGOUT_TEXT);
                lblPassword.setTextFill(Color.WHITE);
                lblUsername.setTextFill(Color.WHITE);
                // add tab categories
                tabPane.getTabs().add(tabCategories);
                //activate tab categories
                tabPane.getSelectionModel().select(tabCategories);

                //do not allow anymore to edit text field user name and password field
                txtFieldUsername.setEditable(false);
                passwordFieldPPWD.setEditable(false);

                // decorate text field with other font

                txtFieldUsername.setFont(javafx.scene.text.Font.font("Verdana", FontWeight.EXTRA_BOLD, 12));


                //incorrect username and password
            } else {
                lblPassword.setTextFill(Color.RED);
                lblUsername.setTextFill(Color.RED);
            }

            // button has text logout
        } else {

            // allow to edit text field username and password field and clear the text
            txtFieldUsername.setEditable(true);
            passwordFieldPPWD.setEditable(true);
            txtFieldUsername.clear();
            passwordFieldPPWD.clear();


            btnLogin.setText(ApplicationConstants.BTN_LOGIN_TEXT);
            tabPane.getSelectionModel().select(tabPlay);
            tabPane.getTabs().remove(tabLogin);
            tabPane.getTabs().remove(tabCategories);


        }

    }

    public void activateLoginTab(ActionEvent actionEvent) {
        tabPane.getTabs().add(tabLogin);
        tabPane.getSelectionModel().select(tabLogin);

    }

    public void loginEnterKey(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            loginAction(null);

        }
    }

    public void addCategory(ActionEvent actionEvent) {
        if (!txtFieldsCategoryName.getText().isEmpty()) {
            Utility.createCategoryFile(txtFieldsCategoryName.getText());
        }
        Utility.listFilesWithoutExtensionFromPath(ApplicationConstants.APP_FOLDER_DATA_PATH
                + "\\" + ApplicationConstants.CATEGORY_FOLDER_NAME);
        fillCategoryCombobox(null);
        comboboxCategories.getSelectionModel().select(txtFieldsCategoryName.getText());
        txtFieldsCategoryName.clear();
    }

    public void fillCategoryCombobox(Event event) {

        if (tabPlay.isSelected()) {
            updateCombobox(comboboxSelectCategoryPlay);
        }
        else if (tabCategories.isSelected()) {
updateCombobox(comboboxCategories);


        }
    }

    
    private void updateCombobox(ComboBox comboboxCategories) {
        comboboxCategories.getItems().clear();
        try {
            comboboxCategories.getItems().addAll(Utility.listFilesWithoutExtensionFromPath(ApplicationConstants.APP_FOLDER_DATA_PATH + "\\"
            + ApplicationConstants.CATEGORY_FOLDER_NAME));
        }catch (Exception e) {
        }
    }

    public void handleAddWord(ActionEvent event) {
        boolean chkBoxCleanActive = chckBoxCleanWords.isSelected();

        //region combobox category
        if (comboboxCategories.getSelectionModel().getSelectedIndex() == -1) {
            lblCategoryNameCombobox.setTextFill(Color.RED);
        } else {
            lblCategoryNameCombobox.setTextFill(Color.WHITE);

            // region Text field new word
            if (!txtFieldNewWord.getText().isEmpty()) {
                lblWordTabCategory.setTextFill(Color.WHITE);
                try {
                    Category category = CategoryParser.parseCategoryFile(chkBoxCleanActive,
                            ApplicationConstants.APP_FOLDER_DATA_PATH + "\\"
                                    + ApplicationConstants.CATEGORY_FOLDER_NAME + "\\" +
                                    comboboxCategories.getSelectionModel().getSelectedItem().toString()
                                    + ApplicationConstants.CATEGORY_FILE_EXTENSION);
                    //word already exists if
                    if (category.wordExists(txtFieldNewWord.getText())) {
                        lblWordTabCategory.setTextFill(Color.RED);
                    }
                    // else word does not exist
                    else {
                        if (chkBoxCleanActive) {
                            Utility.cleanWordsInCategory(category.getWordList(),
                                    comboboxCategories.getSelectionModel().getSelectedItem().toString());
                            lblWordTabCategory.setTextFill(Color.WHITE);}

                            Utility.addWordInCategory(category.getLastIdOfWord() + 1,
                                    txtFieldNewWord.getText(),
                                    txtFieldNewHint.getText(), comboboxCategories.getSelectionModel().getSelectedItem().toString());
                            comboboxCategories.getSelectionModel().select(-1);
                            txtFieldNewWord.clear();
                            txtFieldNewHint.clear();
                        }
                    } catch(IOException e){
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    }

                }
        else{
                    lblWordTabCategory.setTextFill(Color.RED);

                }
                //endregion
            }
            //endregion


        }

    Word wordToGuess;
    String secretWord;

    public void handlePlay(ActionEvent event) {
        try {
            Category category = CategoryParser.parseCategoryFile(false,
                    ApplicationConstants.APP_FOLDER_DATA_PATH + "\\"
                            + ApplicationConstants.CATEGORY_FOLDER_NAME + "\\" +
                            comboboxSelectCategoryPlay.getSelectionModel().getSelectedItem().toString()
                            + ApplicationConstants.CATEGORY_FILE_EXTENSION);
            Random rand = new Random();
            int randomNumber = rand.nextInt(category.getLastIdOfWord());
            wordToGuess = category.getWordList().get(randomNumber);

            for (int i = 0; i <wordToGuess.getName().length(); i++){
                if(wordToGuess.getName().charAt(i)!= ' '){
                    secretWord += "_";
                }
            }
            txtWord2Guess.setText(secretWord);

                }
catch (Exception ex) {
}
    }

    public void handleGuess(ActionEvent event) {

        StringBuilder sb = new StringBuilder(secretWord);

        if(wordToGuess.getName().contains(txtFieldGuess.getText())) {
            for (int i = 0; i < wordToGuess.getName().length(); i++)
                if (wordToGuess.getName().charAt(i) == txtFieldGuess.getText().toCharArray()[0]) {
                    sb.setCharAt(i, txtFieldGuess.getText().toCharArray()[0]);
                } else {
                    secretWord += " ";
                }
        }
                secretWord += sb.toString();
            txtWord2Guess.setText(secretWord);
            }


    }



