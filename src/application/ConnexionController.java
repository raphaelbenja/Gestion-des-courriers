/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import sun.misc.BASE64Encoder;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

/**
 *
 * @author DevMan
 */
public class ConnexionController implements Initializable{

        private String query;
	
	@FXML
	private TextField pseudo;
	
        @FXML
        private Label msg;
        
	@FXML
	private PasswordField mot_de_pass;
	
	@FXML
	private Button connecter;
	
	@FXML
	private AnchorPane root;
        
	@FXML
	private StackPane rootPane;
        
        @FXML
        private RadioButton utilisateurrb;

        @FXML
        private ToggleGroup UserOrAdminOrSecr;

        @FXML
        private RadioButton adminrb;
        
        @FXML
        private RadioButton secretairerb;

	BoxBlur blur = new BoxBlur(3,3,3);
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    public void ConnecterUtilisateur() throws IOException, NoSuchAlgorithmException {
	if(!"".equals(pseudo.getText()) && !"".equals(mot_de_pass.getText())){ 
            
            String encoding="UTF-8",hash,plaintext=mot_de_pass.getText();
            
            MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
            msgDigest.update(plaintext.getBytes(encoding));
            byte rawByte[] = msgDigest.digest();
            hash = (new BASE64Encoder().encode(rawByte));
            
            if(utilisateurrb.isSelected()){
                
                query = "select * from utilisateur WHERE pseudo_util ='" +pseudo.getText()
                    +"' and mot_de_passe_util='"+hash+"'";
                try {
                    
                   Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                   ResultSet res = state.executeQuery(query);

                   if(res.first()){ 
                       BoxBlur blur = new BoxBlur(3,3,3);
                       JFXDialogLayout dialogLayout = new JFXDialogLayout();
                       JFXButton button = new JFXButton("Okay");
                       JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                       button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                               dialog.close();
                       });
                       button.getStyleClass().add("dialog-button");

                       dialogLayout.setHeading(new Label("Login success"));
                       dialogLayout.setBody(new Text("Vous etes connecté "+res.getString("pseudo_util")));
                       dialogLayout.setActions(button);
                       dialog.show();
                       dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                               String title = "Connexion Réussie";
                               String message = null;
                               try {
                                   message = "Bienvenue "+res.getString("pseudo_util").toUpperCase();
                               } catch (SQLException e1) {
                               }
                               Image img = new Image(getClass().getResource("images/Circled.png").toString(),true);
                               TrayNotification tray = new TrayNotification();
                               tray.setTitle(title);
                               tray.setMessage(message);
                               //tray.setNotificationType(notification);
                               //tray.setRectangleFill(Paint.valueOf("#2A9A84"));
                               tray.setAnimationType(AnimationType.SLIDE);
                               tray.setImage(img);
                               tray.showAndDismiss(Duration.seconds(2));
                               root.setEffect(blur);
                               try {
                                   pseudo.setText("");
                                   mot_de_pass.setText("");
                                   
                                   FXMLLoader loader = new FXMLLoader(getClass().getResource("Accueil.fxml"));
                                   Parent root = loader.load();
                                   
                                   AccueilController controller = (AccueilController) loader.getController();
                                   controller.idUtilConnecter=res.getString("id_utilisateur");
                                   controller.fonctionUtilConnecter = 3;
                                   controller.pageDashboard();
                                   controller.verifierAccesMenu();
                                   
                                   Stage load = new Stage();
                                   load.setScene(new Scene(root));
                                   load.show();
                                   connecter.getScene().getWindow().hide();
                               }catch(Exception e) {
                                    e.printStackTrace();
                               }
                       });
                       root.setEffect(null);
                   }else{
                       msg.setText("Pseudo / Mot de passe Incorrect");
                       mot_de_pass.setText("");
                   }

                   //res.close();
                   //state.close();

                 } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR ! ", JOptionPane.ERROR_MESSAGE);
                 }   
            }else if(secretairerb.isSelected()){
                query = "select * from utilisateur WHERE pseudo_util ='" +pseudo.getText()
                    +"' and mot_de_passe_util='"+hash+"' and acces_util='"+2+"'";
                    try {                
                       Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                       ResultSet res = state.executeQuery(query);

                       if(res.first()){ 
                           BoxBlur blur = new BoxBlur(3,3,3);
                           JFXDialogLayout dialogLayout = new JFXDialogLayout();
                           JFXButton button = new JFXButton("Okay");
                           JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                           button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                                   dialog.close();
                           });
                           button.getStyleClass().add("dialog-button");

                           dialogLayout.setHeading(new Label("Login success"));
                           dialogLayout.setBody(new Text("Vous etes connecté "+res.getString("pseudo_util")));
                           dialogLayout.setActions(button);
                           dialog.show();
                           dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                                   String title = "Connexion Réussie";
                                   String message = null;
                                   try {
                                       message = "Bienvenue "+res.getString("pseudo_util").toUpperCase();
                                   } catch (SQLException e1) {
                                   }
                                   Image img = new Image(getClass().getResource("images/Circled.png").toString(),true);
                                   TrayNotification tray = new TrayNotification();
                                   tray.setTitle(title);
                                   tray.setMessage(message);
                                   //tray.setNotificationType(notification);
                                   //tray.setRectangleFill(Paint.valueOf("#2A9A84"));
                                   tray.setAnimationType(AnimationType.FADE);
                                   tray.setImage(img);
                                   tray.showAndDismiss(Duration.seconds(2));
                                   root.setEffect(blur);
                                   try {
                                       pseudo.setText("");
                                       mot_de_pass.setText("");
                                       Stage load = new Stage();
                                       FXMLLoader loader = new FXMLLoader(getClass().getResource("Accueil.fxml"));
                                       Parent root = loader.load();

                                       AccueilController controller = (AccueilController) loader.getController();
                                       controller.idUtilConnecter=res.getString("id_utilisateur");
                                       controller.fonctionUtilConnecter = 2;
                                       controller.pageDashboard();
                                       controller.verifierAccesMenu();
                                        
                                        load.setScene(new Scene(root));
                                        load.show();
                                        connecter.getScene().getWindow().hide();
                                   }catch(Exception e) {
                                           e.printStackTrace();
                                   }
                           });
                           root.setEffect(null);
                       }else{
                           msg.setText("Pseudo / Mdp du secrétaire Incorrect");
                           mot_de_pass.setText("");
                       }

                       //res.close();
                       //state.close();

                     } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR ! ", JOptionPane.ERROR_MESSAGE);
                     }   
            }else if(adminrb.isSelected()){
                query = "select * from utilisateur WHERE pseudo_util ='" +pseudo.getText()
                    +"' and mot_de_passe_util='"+hash+"' and acces_util='"+1+"'";
                    try {                
                       Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                       ResultSet res = state.executeQuery(query);

                       if(res.first()){ 
                           BoxBlur blur = new BoxBlur(3,3,3);
                           JFXDialogLayout dialogLayout = new JFXDialogLayout();
                           JFXButton button = new JFXButton("Okay");
                           JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                           button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                                   dialog.close();
                           });
                           button.getStyleClass().add("dialog-button");

                           dialogLayout.setHeading(new Label("Login success"));
                           dialogLayout.setBody(new Text("Vous etes connecté "+res.getString("pseudo_util")));
                           dialogLayout.setActions(button);
                           dialog.show();
                           dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                                   String title = "Connexion Réussie";
                                   String message = null;
                                   try {
                                       message = "Bienvenue "+res.getString("pseudo_util").toUpperCase();
                                   } catch (SQLException e1) {
                                   }
                                   Image img = new Image(getClass().getResource("images/Circled.png").toString(),true);
                                   TrayNotification tray = new TrayNotification();
                                   tray.setTitle(title);
                                   tray.setMessage(message);
                                   //tray.setNotificationType(notification);
                                   //tray.setRectangleFill(Paint.valueOf("#2A9A84"));
                                   tray.setAnimationType(AnimationType.POPUP);
                                   tray.setImage(img);
                                   tray.showAndDismiss(Duration.seconds(2));
                                   root.setEffect(blur);
                                   try {
                                       pseudo.setText("");
                                       mot_de_pass.setText("");
                                       Stage load = new Stage();
                                       FXMLLoader loader = new FXMLLoader(getClass().getResource("Accueil.fxml"));
                                       Parent root = loader.load();

                                        AccueilController controller = (AccueilController) loader.getController();
                                        controller.idUtilConnecter=res.getString("id_utilisateur");
                                        controller.fonctionUtilConnecter = 1;
                                        controller.pageDashboard();
                                        controller.verifierAccesMenu();
                                        
                                        load.setScene(new Scene(root));
                                        load.show();
                                           
                                        connecter.getScene().getWindow().hide();
                                   }catch(Exception e) {
                                           e.printStackTrace();
                                   }
                           });
                           root.setEffect(null);
                       }else{
                           msg.setText("Pseudo / Mdp d'Admin Incorrect");
                           mot_de_pass.setText("");
                       }

                       //res.close();
                       //state.close();

                     } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR ! ", JOptionPane.ERROR_MESSAGE);
                     }   
            }
            
     }else {
                JFXDialogLayout dialogLayout = new JFXDialogLayout();
                JFXButton button = new JFXButton("Okay");
                JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                       root.setEffect(blur);
                       dialog.close();
                });
                button.getStyleClass().add("dialog-button");

                dialogLayout.setHeading(new Label("Warning"));
                dialogLayout.setBody(new Text("Veuillez remplir tous les champs"));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                       root.setEffect(null);
                });
            }
	}
    
}
