/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DevMan
 */
public class AccueilController implements Initializable {
    
    String idUtilConnecter;
    int fonctionUtilConnecter;
    
    @FXML
    private JFXButton menuCourrier;
    @FXML
    private JFXButton menuDirection;
    @FXML
    private JFXButton menuUtilisateur;
    
    @FXML
    private StackPane rootPane;
    @FXML
    private BorderPane root;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    } 
    
    @FXML
    public void pageDirection() throws Exception {
 	Parent root = FXMLLoader.load(getClass().getResource("Direction.fxml"));
        rootPane.getChildren().setAll(root);
    }
    
    @FXML
    public void pageDeconnexion() throws Exception {
 		Stage st = new Stage();
 		Parent root = FXMLLoader.load(getClass().getResource("Connexion.fxml"));
                st.setTitle("Se connecter");
 		st.setScene(new Scene(root));
                st.setResizable(false);
 		st.show();
 		rootPane.getScene().getWindow().hide();
    }
    
    @FXML
    public void pageUtilisateur() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Utilisateur.fxml"));
	rootPane.getChildren().setAll(root);
    }
    
    @FXML
    public void pageDashboard() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();
        DashboardController controller = (DashboardController) loader.getController();
        controller.idUtilConnecter=idUtilConnecter;
        controller.fonctionUtilConnecter = fonctionUtilConnecter;
	rootPane.getChildren().setAll(root);
    }
    
    @FXML
    public void pageCourrier() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Courrier.fxml"));
        Parent root = loader.load();
        CourrierController controller = (CourrierController) loader.getController();
        controller.idUtilConnecter=idUtilConnecter;
        controller.fonctionUtilConnecter = fonctionUtilConnecter;
        controller.verifierAccesBtn();
        controller.loadCourrier();
	rootPane.getChildren().setAll(root);
    }
    
    @FXML
    public void pageApropos() throws Exception {
 	Stage st = new Stage();
 	Parent root = FXMLLoader.load(getClass().getResource("Apropos.fxml"));
 	st.setTitle("A propos du devéloppeur");
        st.initModality(Modality.APPLICATION_MODAL);
 	st.setScene(new Scene(root));
        st.setResizable(false);
 	st.show();
    }
    
    @FXML
    public void pageProfile() throws Exception {
 		FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
                Parent root = loader.load();
                                   
                ProfileController controller = (ProfileController) loader.getController();
                controller.idUtilConnecter=idUtilConnecter;
                controller.obtenirInfoProfilUtil(idUtilConnecter);
                rootPane.getChildren().setAll(root);
    }
    
    public void verifierAccesMenu(){
        if(fonctionUtilConnecter == 0 || fonctionUtilConnecter == 3){
            menuDirection.setVisible(false);
            menuUtilisateur.setVisible(false);
            menuUtilisateur.toFront();
            menuDirection.toFront();
            menuDirection.getProperties().clear();
            menuUtilisateur.getProperties().clear();
            menuCourrier.setText("Courriers");
        }else if(fonctionUtilConnecter == 2){
            menuDirection.setVisible(false);
            menuUtilisateur.setVisible(false);
            menuUtilisateur.toFront();
            menuDirection.toFront();
            menuDirection.getProperties().clear();
            menuUtilisateur.getProperties().clear();
            menuCourrier.setText("Courriers");
        }
    }
    
    
}
