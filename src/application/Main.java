package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	
	public static boolean isLoaded = false;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Connexion.fxml"));
			primaryStage.setScene(new Scene(root));
                        primaryStage.setResizable(false);
                        primaryStage.getIcons().add(new Image(getClass().getResource("images/LoginPage.png").toString(),true));
			primaryStage.show();
		} catch(IOException e) {
                    e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
