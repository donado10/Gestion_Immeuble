/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Immeuble.Class;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author LBC
 */
public class Region {
    
//Attributs---------------------------------------------------------| 
    private String Region_name;
      
//Constructeur(s)---------------------------------------------------------| 
    //Constructeur par défaut
    public Region(){
        this.Region_name = null;
    }
    
//Méthodes Privées---------------------------------------------------------|  
    //Méthode qui renvoie l'identifiant le plus grand d'une région dans la base de donnée
    private int get_Highest_RegionID(){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int Nombre_Ligne = 0;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            rs = st.executeQuery("select region_id from region order by region_id desc LIMIT 1");
            while(rs.next()){
               Nombre_Ligne = rs.getInt("region_id");
            } 
        }
            catch (Exception e) {
        }
            return Nombre_Ligne;
    }
    
    //Méthode qui renvoie TRUE si une région est absente de la base de donnée
    private boolean VerifierRegion(){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int Nombre_Ligne = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM region where region_name ='"+Region_name+"'");
            while(rs.next()){
            Nombre_Ligne = rs.getInt("rowcount");
        }
        }catch (Exception e) {   
        }
        return Nombre_Ligne == 1;
    }
    
    //Méthode qui renvoie le nombre de total de région présente dans la base de donnée
    private int get_Regions_Number(){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int Nombre_Region = 0;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();                 
            rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM region");
            while(rs.next()){
               Nombre_Region = rs.getInt("rowcount");
            }             
        }catch(Exception e){
        }
    return Nombre_Region;
}
            
//Méthodes Publiques---------------------------------------------------------|
    //Méthode qui permet d'ajouter une région dans la base de donnée
    public boolean AjouterRegion(String Nom_Region){
        String requete = null;
        Connection co = null;
        Statement st = null;
        Region_name = Nom_Region;
        
        if (VerifierRegion() == false) {
            int ID_Region = get_Highest_RegionID()+ 1;
            try
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
                st = co.createStatement();               
                requete = "INSERT INTO region(Region_ID,Region_name) VALUES ("+ ID_Region +",'"+Region_name+"')";             
                st.executeUpdate(requete);         
            }catch(Exception e){
                return false;
            }
        return true;
        } else {
            return false;
        }
}
    
    //Méthode qui renvoie le nom des régions présent dans la base de donnée
    public String[] get_Regions_Names(){
        int number = get_Regions_Number();
        String[] Name_Region = new String[number];
        
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            for (int i = 0; i < number; i++) {
               rs = st.executeQuery("select region_name from region LIMIT 1 OFFSET "+i+"");              
            while(rs.next()){
               Name_Region[i] = rs.getString("region_name");
             }   
            }       
        }catch(Exception e){
        }      
        return Name_Region;
}

    //Méthode qui permet de supprimer une région de la base de donnée
    public boolean SupprimerRegion(String Nom_Region){
        String requete = null;
        Connection co = null;
        Statement st = null;
        Region_name = Nom_Region;
        if (VerifierRegion() == true) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
                st = co.createStatement();                                              
                requete = "Delete from region where region_name = '"+Region_name+"'";             
                st.executeUpdate(requete);         
            }catch(Exception e){
                return false;
            }
            return true;
        } else {
            return false;
        }        
    }
}
