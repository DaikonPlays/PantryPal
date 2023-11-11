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
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.sound.sampled.*;

public class RecipeGenerate extends BorderPane {
    private boolean isRecording = false;
    Label recordingLabel = new Label("Recording...");
    public String mealType;
    private String recipeIntro = "Give_me_a_recipe_for_";
    private String recipeIntro2 = "using_the_following_ingredients_";
    public String whisperResponse;
    private TargetDataLine targetLine;
    private File outputFile;
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    public RecipeGenerate() {
        ListView<String> recipeList = new ListView<>();
        this.setCenter(recipeList);
        HBox footer = new HBox(10);
        Button recordButton = new Button();
        recordingLabel.setStyle(defaultLabelStyle);

        footer.getChildren().addAll(recordButton, recordingLabel);
        this.setBottom(footer);
        recordButton.setOnAction(e -> toggleRecord());

    }

    public boolean toggleRecord() {
        if (isRecording) {
            stopRecord();
        } else {
            startRecord();
        }
        isRecording = !isRecording;
        return isRecording;
    }

    public void startRecord() {
        try {
            AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open(format);
            targetLine.start();
            recordingLabel.setVisible(true);

            outputFile = new File("src/main/java/voiceinstructions.wav");
            Thread recordThread = new Thread(() -> {
                try {
                    AudioSystem.write(new AudioInputStream(targetLine), AudioFileFormat.Type.WAVE, outputFile);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
            recordThread.setDaemon(true);
            recordThread.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        targetLine.stop();
        targetLine.close();
        recordingLabel.setVisible(false);
    }
    public String getWhisperResponse() {
        Model model = new Model();
        String mod = "";
        try {
            whisperResponse = model.performRequest("GET", "whisper", "voiceinstructions.wav");
            String mealTypecheck = whisperResponse.toLowerCase();
            if(mealTypecheck.contains("breakfast")) {
                mealType = "breakfast";
            }
            else if(mealTypecheck.contains("lunch")) {
                mealType = "lunch";
            }
            else if(mealTypecheck.contains("dinner")) {
                mealType = "dinner";
            }
            mod = whisperResponse.replaceAll(" ", "_");
            // System.out.println(mod); 
        }
        catch (Exception e) {
            System.err.println("No input detected");
        }
        return mod;
    }
    public String getResponse() {
        Model model = new Model();
        String gptResponse = "";
        try {
            // System.out.println(mod);
            // gptResponse = model.performRequest("GET", "gpt", "300," + getWhisperResponse()); //TODO FIX
            // FRENCH
            // if(!whisperResponse.toLowerCase().contains("breakfast") && !whisperResponse.toLowerCase().contains("lunch") && !whisperResponse.toLowerCase().contains("dinner")) {
                String ingredients = getWhisperResponse();
                gptResponse = model.performRequest("GET", "gpt", "500," + recipeIntro + mealType + recipeIntro2 + ingredients);
                //"300, make me a meal for " + mealType + " " + mod
            // }
            // else {
            //     mealType = gptResponse;
            // }
            // System.out.println(gptResponse);
        } catch (Exception e) {
            System.out.println("No input detected");
        }

        return gptResponse;
        // return "this is a test string"; // MOCK
    }

}
