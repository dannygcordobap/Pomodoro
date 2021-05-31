/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.applet.*;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import java.io.IOException;
import static java.lang.Character.isDigit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Daniel Cordoba Paez <dcordob@wgu.edu>
 */
public class MainController implements Initializable {
   
    @FXML
    private Button exit;
    
    @FXML
    private Button start;
    
    @FXML
    private TextField workingTime;
    
    @FXML
    private TextField breakTime;
    
    @FXML
    private Label onBreak;
    
    @FXML
    private TextField totalPomodoros;
    
    @FXML
    private Label remainingTime;
    
    @FXML
    private Label remainingPomodoros;
    
    @FXML
    private Label minutes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void cycles() {
        
        Timer timer = new Timer();
        
        int delay = 60000;
        int period = 1000;
        long breakDuration = Long.parseLong(breakTime.getText());
        long workDuration = Long.parseLong(workingTime.getText());
        
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                Platform.runLater(() -> {
                    
                    int timeRemaining = Integer.parseInt(remainingTime.getText());
                    --timeRemaining;
                    remainingTime.setText(Integer.toString(timeRemaining));
                    
                    if (timeRemaining == 0) {

                        AudioClip timerAlert = Applet.newAudioClip(getClass().getResource("/controller/soundAlert.wav"));
                        timerAlert.play();
                        
                        minutes.setVisible(false);
                        remainingTime.setVisible(false);
                        onBreak.setVisible(true);
                        
                        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                        confirm.setTitle("Pomodoro complete!");
                        confirm.setHeaderText("Get ready for a break!");
                        confirm.setContentText("Your pomodoro is now complete!\nNow you will have a five minute break, "
                                + "just be sure to actually take the break!");
                        confirm.showAndWait();
                        remainingPomodoros.setText(Integer.toString(Integer.parseInt(remainingPomodoros.getText()) - 1));
                        
                        int completed = (Integer.parseInt(totalPomodoros.getText()) - Integer.parseInt(remainingPomodoros.getText()));
                        
                        if (completed % 4 == 0) {

                            timer.schedule(new TimerTask() {

                                @Override
                                public void run() {
                                    Platform.runLater(() -> {

                                        AudioClip timerAlert = Applet.newAudioClip(getClass().getResource("/controller/soundAlert.wav"));
                                        timerAlert.play();

                                        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                                        confirm.setTitle("Break complete!");
                                        confirm.setHeaderText("Your break is over!");
                                        confirm.setContentText("Your break is now complete!\nNow you will have another working period, "
                                                + "just be sure focus and be productive!");
                                        confirm.showAndWait();

                                        minutes.setVisible(true);
                                        remainingTime.setText(workingTime.getText());
                                        remainingTime.setVisible(true);
                                        onBreak.setVisible(false);

                                        if (Integer.parseInt(remainingPomodoros.getText()) == 0) {
                                            timer.cancel();
                                            timer.purge();
                                        }
                                    });
                                }
                            }, (delay * (breakDuration + workDuration)));
                        } else {
                            timer.schedule(new TimerTask() {

                                @Override
                                public void run() {
                                    Platform.runLater(() -> {

                                        AudioClip timerAlert = Applet.newAudioClip(getClass().getResource("/controller/soundAlert.wav"));
                                        timerAlert.play();
                                        
                                        if (Integer.parseInt(remainingPomodoros.getText()) == 0) {
                                            timer.cancel();
                                            timer.purge();
                                            Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                                            confirm.setTitle("No more pomodoros!");
                                            confirm.setHeaderText("You finished!");
                                            confirm.setContentText("You have finish all of your pomodoros!\nNow go give your dog attention!"
                                                    + " (Go get one if you don't have one yet.)");
                                            confirm.showAndWait();
                                            breakTime.setEditable(true);
                                            totalPomodoros.setEditable(true);
                                            workingTime.setEditable(true);
                                            start.setVisible(true);
                                        } else {
                                            Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                                            confirm.setTitle("Break complete!");
                                            confirm.setHeaderText("Your break is over!");
                                            confirm.setContentText("Your break is now complete!\nNow you will have another working period, "
                                                    + "just be sure focus and be productive!");
                                            confirm.showAndWait();
                                        }

                                        minutes.setVisible(true);
                                        remainingTime.setText(workingTime.getText());
                                        remainingTime.setVisible(true);
                                        onBreak.setVisible(false);
                                    });
                                }
                            }, (long)delay * breakDuration);
                        }
                    }
                });
            }
        }, delay, period);
    }
    
    
    public void exitButtonPressed(ActionEvent event) throws IOException {
        
        Alert exit = new Alert(Alert.AlertType.CONFIRMATION);
        exit.setTitle("Exit Pomodoro Timer");
        exit.setHeaderText("Are you sure you want to exit?");
        exit.setContentText("Press OK to exit the program. \nPress Cancel to return to the program.");
        exit.showAndWait();
        if(exit.getResult() == ButtonType.OK) {
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.close();
            System.exit(0);
        } else {
            exit.close();
        }
    }
    
    public void updateWorkingTime(KeyEvent event) throws IOException {
        remainingTime.setText(workingTime.getText());
    }
    
    public void updatePomodoros(KeyEvent event) throws IOException {
        remainingPomodoros.setText(totalPomodoros.getText());
    }
    
    public void startButtonPressed(ActionEvent event) {
        
        breakTime.setEditable(false);
        totalPomodoros.setEditable(false);
        workingTime.setEditable(false);
        start.setVisible(false);
        
        this.cycles();
    }
}