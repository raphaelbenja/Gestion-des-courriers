/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;
import sun.misc.BASE64Encoder;

/**
 *
 * @author DevMan
 */
public class PasswordController implements Initializable{

    @FXML
    private JFXPasswordField ancien;
    @FXML
    private JFXPasswordField nouveau;
    @FXML
    private JFXPasswordField confirmation;
    
    @FXML
    private Label ancienErr;
    @FXML
    private Label nouveauErr;
    
    @FXML
    private StackPane rootPane;
    
    @FXML
    private BorderPane root;
    
    BoxBlur blur = new BoxBlur(3,3,3);
    
    String idUtilConnecter;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     
    }
    
    private String verifierAncienMotdePasse() throws SQLException{
        ResultSet res = null; 
        String query = "SELECT mot_de_passe_util FROM utilisateur WHERE id_utilisateur='"+idUtilConnecter+"'";
        Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        res = state.executeQuery(query);
        res.first();
         
        return res.getString("mot_de_passe_util");
    }
    
    @FXML
    private void btnMettreaJourUtilisateur() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // Modifier mot de pase
        try{
            if(!"".equals(ancien.getText()) && !"".equals(nouveau.getText()) && !"".equals(confirmation.getText())){
                
                if(nouveau.getText().equals(confirmation.getText())){
                    
                    String encoding="UTF-8",hashNouveau,hashAncien,plaintext=nouveau.getText(),plt=ancien.getText();
            
                    MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
                    msgDigest.update(plt.getBytes(encoding));
                    byte rawByte[] = msgDigest.digest();
                    hashAncien = (new BASE64Encoder().encode(rawByte));
                    
                    if(verifierAncienMotdePasse().equals(hashAncien)){
                        
                        msgDigest.update(plaintext.getBytes(encoding));
                        byte rawBytes[] = msgDigest.digest();
                        hashNouveau = (new BASE64Encoder().encode(rawBytes));
                        
                         String query = "UPDATE utilisateur SET mot_de_passe_util=? WHERE id_utilisateur =? ";

                        PreparedStatement prepare = DevsConnexion.getInstance().prepareStatement(query);
                        prepare.setString(1, hashNouveau);
                        prepare.setString(2, idUtilConnecter);

                        prepare.executeUpdate();
                        JFXDialogLayout dialogLayout = new JFXDialogLayout();
                        JFXButton button = new JFXButton("Okay");
                        JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                            root.setEffect(blur);
                            dialog.close();
                        });
                        button.getStyleClass().add("dialog-button");

                        dialogLayout.setBody(new Text("Le mot de passe a été changé avec succès"));
                        dialogLayout.setActions(button);
                        dialog.show();
                        dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                            root.setEffect(null);
                            ancien.setText("");
                            ancienErr.setText("");
                            nouveau.setText("");
                            nouveauErr.setText("");
                            confirmation.setText("");
                        });
                    }else{
                        ancienErr.setText("Ancien mot de passe invalide");
                    }
                   
                }else{
                    nouveauErr.setText("Les 2 mot de passe ne concordent pas");
                }
                    
            }else{
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
        }catch(SQLException e){
        	e.printStackTrace();
        }
    }
    
}
