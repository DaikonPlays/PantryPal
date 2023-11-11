package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.geometry.Insets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.*;
import java.awt.BorderLayout;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;

class RecipeDetails extends BorderPane {
    private Header header;
    private Footer footer;
    private Details details;
    private Button saveButton;
    private Button deleteButton;

    private RecipeDisplay recipeDisplay;

    RecipeDetails(RecipeDisplay recipeDisplay) {
        this.recipeDisplay = recipeDisplay;

        // Initialise the header Object
        header = new Header();
        // Create a details Object to hold the recipe details
        details = new Details();
        // Initialise the Footer Object
        footer = new Footer();
        ScrollPane scrollPane = new ScrollPane(details);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scrollPane);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);
        // Initialise Button Variables through the getters in Footer
        saveButton = footer.getSaveButton();
        deleteButton = footer.getDeleteButton();

        updateTitleAndDetails(recipeDisplay.getRecipe());

        // Call Event Listeners for the Buttons
        addListeners(recipeDisplay);
    }

    // RecipeDetails Header
    class Header extends HBox {
        Header() {
            this.setPrefSize(500, 60); // Size of the header
            this.setStyle("-fx-background-color: #F0F8FF;");

            Text titleText = new Text("Recipe Details"); // Text of the Header
            titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
            this.getChildren().add(titleText);
            this.setAlignment(Pos.CENTER); // Align the text to the Center
        }
    }

    // RecipeDetails Footer
    class Footer extends HBox {
        private Button saveButton;
        private Button deleteButton;

        Footer() {
            this.setPrefSize(500, 60);
            this.setStyle("-fx-background-color: #F0F8FF;");
            this.setSpacing(15);

            // set a default style for buttons - background color, font size, italics
            String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

            saveButton = new Button("Save Changes"); // text displayed on add button
            saveButton.setStyle(defaultButtonStyle); // styling the button
            deleteButton = new Button("Delete Recipe"); // text displayed on add button
            deleteButton.setStyle(defaultButtonStyle); // styling the button

            this.getChildren().addAll(saveButton, deleteButton); // adding buttons to footer
            this.setAlignment(Pos.CENTER); // aligning the buttons to center
        }

        public Button getSaveButton() {
            return saveButton;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public void addListeners(RecipeDisplay recipeDisplay) {
        // Add button functionality
        saveButton.setOnAction(e -> {
            recipeDisplay.getRecipe().setRecipeName((details.getTitle()));
            recipeDisplay.getRecipe().setRecipe((details.getDetails()));
            updateTitleAndDetails(recipeDisplay.getRecipe());
            recipeDisplay.setRecipeName(recipeDisplay.getRecipe());
        });

        // Add button functionality
        deleteButton.setOnAction(e -> {

        });
    }

    public void updateTitleAndDetails(Recipe recipe){
        details.setTitle(recipe.getRecipeName());
        details.setDetails(recipe.getRecipeDetails());
    }

    public String getTitle(){
        return details.getTitle();
    }

    public String getDetails(){
        return details.getDetails();
    }

}
