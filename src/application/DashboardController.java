/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author DevMan
 */
public class DashboardController implements Initializable {

    String query;
    
    @FXML
    private Label nbrCourrier;
    
    @FXML
    private Label nbrDirection;
    
    @FXML
    private Label nbrUtilisateur;
    
    @FXML
    private Label nbrCompte;
    
    @FXML
    private PieChart pie;
    
    @FXML
    private PieChart pie2;
    
    String idUtilConnecter;
    int fonctionUtilConnecter;
    
    @FXML
    private StackPane rootPane;
    @FXML
    private BorderPane root;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nbrCompte.setText("03");
        chargerComposant();
    } 
    
    @FXML
    public void pageDirection() throws Exception {
 	Parent root = FXMLLoader.load(getClass().getResource("Direction.fxml"));
        rootPane.getChildren().setAll(root);
    }
    
    @FXML
    public void pageUtilisateur() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Utilisateur.fxml"));
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
    
    public void totalCourrier() {
            query = "SELECT count(*) as courier FROM courrier";
           
            try {

                Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet res = state.executeQuery(query);
                res.first();
                
                if(res.getInt("courier") >= 10){
                    nbrCourrier.setText(res.getString("courier"));
                }else{
                    nbrCourrier.setText(0+res.getString("courier"));
                }

               res.close();
               state.close();

             } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR ! ", JOptionPane.ERROR_MESSAGE);
             } 
	}
    
    
        public void totalDirection() {
            query = "SELECT count(*) as direct FROM direction";
           
            try {

                Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet res = state.executeQuery(query);
                res.first();

                if(res.getInt("direct") >= 10){
                    nbrDirection.setText(res.getString("direct"));
                }else{
                    nbrDirection.setText(0+res.getString("direct"));
                }
                
               res.close();
               state.close();

             } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR ! ", JOptionPane.ERROR_MESSAGE);
             } 
	}
        
        public void totalUtilisateur() {
            query = "SELECT count(*) as util FROM utilisateur";
           
            try {

                Statement state = DevsConnexion.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet res = state.executeQuery(query);
                res.first();

                if(res.getInt("util") >= 10){
                   nbrUtilisateur.setText(res.getString("util"));
                }else{
                    nbrUtilisateur.setText(0+res.getString("util"));
                }

               res.close();
               state.close();

             } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR ! ", JOptionPane.ERROR_MESSAGE);
             } 
	}
        
        public void afficherPieChart(){
           ObservableList<PieChart.Data> details = FXCollections.observableArrayList();
           
	   details.addAll(new PieChart.Data("COURRIERS", Integer.parseInt(nbrCourrier.getText())));
	   details.addAll(new PieChart.Data("UTILISATEURS", Integer.parseInt(nbrUtilisateur.getText())));
	   details.addAll(new PieChart.Data("DIRECTIONS", Integer.parseInt(nbrDirection.getText())));
	   
		pie.setLegendSide(Side.BOTTOM);
		pie.setLabelsVisible(true);
		pie.setStartAngle(90);
		//pie.setClockwise(false);
		pie.getData().setAll(details);	
        }
        
        public void afficherPie(){
           ObservableList<PieChart.Data> details = FXCollections.observableArrayList();
           
	   details.addAll(new PieChart.Data("COMPTES", Integer.parseInt(nbrCompte.getText())));
	   details.addAll(new PieChart.Data("DIRECTIONS", Integer.parseInt(nbrDirection.getText())));
           details.addAll(new PieChart.Data("UTILISATEURS", Integer.parseInt(nbrUtilisateur.getText())));
	   details.addAll(new PieChart.Data("COURRIERS", Integer.parseInt(nbrCourrier.getText())));
	   
		pie2.setLegendSide(Side.BOTTOM);
		pie2.setLabelsVisible(true);
		//pie.setStartAngle(90);
		pie2.setClockwise(false);
		pie2.getData().setAll(details);	
        }

        @FXML
        private void chargerComposant() {
            totalCourrier();
            totalDirection();
            totalUtilisateur();
            afficherPieChart();
            afficherPie();
        }
    
}
