/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 *
 * @author DevMan
 */
public class DirectionController implements Initializable{

    private String query;
    double xOffset,yOffset;
	
    @FXML
    private JFXTextField txtSearch;
	
    @FXML
    private TableView<DirectionController.Direction> tab;
    @FXML
    private TableColumn<DirectionController.Direction, String> id_directionColumn;
    @FXML
    private TableColumn<DirectionController.Direction, String> nom_directionColumn;
   
    @FXML
    private StackPane rootPane;
    @FXML
    private BorderPane root;
	
    BoxBlur blur = new BoxBlur(3,3,3);
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chargerColonne();
        chargerDirection();
    }
    
    public void chargerDirection() {
            query = "SELECT * FROM direction";
            ObservableList<DirectionController.Direction> list = FXCollections.observableArrayList();
            try {
            
                Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet res = state.executeQuery(query);
                ResultSetMetaData meta = (ResultSetMetaData) res.getMetaData();

                for(int i = 1 ; i <= meta.getColumnCount(); i++){
                    tab.getColumns().setAll(id_directionColumn,nom_directionColumn);
                }

                while(res.next()){	

                    String id_direction = res.getString("id_direction");
                    String nom_direction = res.getString("nom_direction");

                    list.add(new DirectionController.Direction(id_direction,nom_direction));     
                }
                res.close();
                state.close();

             } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR ! ", JOptionPane.ERROR_MESSAGE);
             } 
                tab.getItems().setAll(list);
	}
	
	@FXML
	public void actualiserDonner() {
            chargerDirection();
	}

	private void chargerColonne() {
            id_directionColumn.setCellValueFactory(new PropertyValueFactory<>("id_direction"));
            nom_directionColumn.setCellValueFactory(new PropertyValueFactory<>("nom_direction"));
	}
        
        @FXML
        public void supprimerDirection(ActionEvent event) throws Exception {

            ObservableList<DirectionController.Direction> select;
            select = tab.getSelectionModel().getSelectedItems();

            if(tab.getItems().size() != 0){
                 if(tab.getSelectionModel().getSelectedIndex() != -1){
                      if(JOptionPane.showConfirmDialog(null, "attention!!! etes-vous sure de"
                    + " vouloir supprimer cette direction ?","Supprimer une direction",JOptionPane.YES_NO_OPTION)
                    == JOptionPane.OK_OPTION){
                        String id = select.get(0).getId_direction();
                        query = "DELETE FROM direction WHERE id_direction=? LIMIT 1";
                        PreparedStatement prepare;
                        prepare = DevsConnexion.getInstance().prepareStatement(query);
                        prepare.setString(1, id);
                        prepare.executeUpdate();
                        JFXDialogLayout dialogLayout = new JFXDialogLayout();
                        JFXButton button = new JFXButton("Okay");
                        JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                           root.setEffect(blur);
                           dialog.close();
                        });
                        button.getStyleClass().add("dialog-button");
                        dialogLayout.setBody(new Text("Suppression avec success"));
                        dialogLayout.setActions(button);
                        dialog.show();
                        dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                           root.setEffect(null);
                           chargerDirection();
                        });              
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
                     dialogLayout.setBody(new Text("Aucune ligne selectionne"));
                     dialogLayout.setActions(button);
                     dialog.show();
                     dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                           root.setEffect(null);
                     });
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
                dialogLayout.setBody(new Text("Aucune donnee"));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                   root.setEffect(null);
                });
            }
        }
        
        @FXML
        public void chercherDirection() {
            try { 
                query = "SELECT * FROM direction WHERE id_direction LIKE '%"+txtSearch.getText()+"%' OR nom_direction LIKE '%"+txtSearch.getText()+"%'";
                Statement st = DevsConnexion.getInstance().createStatement();
                ResultSet res = st.executeQuery(query);

                ResultSetMetaData meta = res.getMetaData();

                for(int i = 1 ; i <= meta.getColumnCount(); i++){
                    tab.getColumns().setAll(id_directionColumn,nom_directionColumn);
                }

                ObservableList<DirectionController.Direction> list = FXCollections.observableArrayList();
                 while(res.next()){

                    String id = res.getString("id_direction");
                    String nom = res.getString("nom_direction");

                    list.add(new DirectionController.Direction(id,nom));        
                 }

                 tab.getItems().setAll(list);

                if (tab.getItems().size() == 0){
                    JFXDialogLayout dialogLayout = new JFXDialogLayout();
                    JFXButton button = new JFXButton("Okay");
                    JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                    button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
                           root.setEffect(blur);
                           dialog.close();
                    });
                    button.getStyleClass().add("dialog-button");

                    dialogLayout.setHeading(new Label("Information"));
                    dialogLayout.setBody(new Text("Aucune direction trouvee"));
                    dialogLayout.setActions(button);
                    dialog.show();
                    dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                           root.setEffect(null);
                           //loadCourrier();
                    });
                }

                tab.refresh();

            }catch (Exception m){
                   JOptionPane.showMessageDialog(null, ""+m.getMessage());
                   System.err.println(m);}
        }
        
        @FXML
        public void AjouterDirection(ActionEvent event) throws Exception {

            Parent root = FXMLLoader.load(getClass().getResource("Ajouter_direction.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            root.setOnMousePressed((MouseEvent e) -> {
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent e) -> {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nouvelle direction");
            //stage.getIcons().add(new Image("/images/logo.png"));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        }
        
        @FXML
        public void editerDirection() throws Exception {
            if(tab.getItems().size() != 0){
                 if(tab.getSelectionModel().getSelectedIndex() != -1){
                    String id = tab.getSelectionModel().getSelectedItems().get(0).getId_direction();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Editer_direction.fxml"));
                    Parent root = loader.load();
                    EditerDirectionController controller = (EditerDirectionController) loader.getController();
                    controller.getInfoDirection(id);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    root.setOnMousePressed((MouseEvent e) -> {
                        xOffset = e.getSceneX();
                        yOffset = e.getSceneY();
                    });
                    root.setOnMouseDragged((MouseEvent e) -> {
                        stage.setX(e.getScreenX() - xOffset);
                        stage.setY(e.getScreenY() - yOffset);
                    });
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Editer une direction");
                    //stage.getIcons().add(new Image("/images/logo.png"));
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    stage.show();
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
                     dialogLayout.setBody(new Text("Aucune ligne selectionne"));
                     dialogLayout.setActions(button);
                     dialog.show();
                     dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                           root.setEffect(null);
                     });
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
                dialogLayout.setBody(new Text("Aucune donnee"));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event1)->{
                   root.setEffect(null);
                });
            }
        }
        
        public class Direction{
		
		private final SimpleStringProperty id_direction;
		private final SimpleStringProperty nom_direction;

                public Direction(String id_direction, String nom_direction) {
                    this.id_direction = new SimpleStringProperty(id_direction);
                    this.nom_direction = new SimpleStringProperty(nom_direction);
                }

                public String getId_direction() {
                    return id_direction.get();
                }

                public String getNom_direction() {
                    return nom_direction.get();
                }
	}
    
}
