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
public class Zone {

//Attributs---------------------------------------------------------| 
    private String Zone_name;
    
//Constructeur(s)---------------------------------------------------------|    
    //Constructeur par défaut
    public Zone(){
        this.Zone_name = null;
    }
    
    //Constructeur qui initialise une zone
    public Zone(String zone){
        this.Zone_name = zone;
    }
//Méthodes Privées---------------------------------------------------------|
    //Méthode pour obtenir la zone ayant l'identifiant le plus grand dans la base de donnée
    private int get_Highest_ZoneID(){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int Nombre_Ligne = 0;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            rs = st.executeQuery("select zone_id from zone order by zone_id desc LIMIT 1");           
            while(rs.next()){
               Nombre_Ligne = rs.getInt("zone_id");
            }
        }
            catch (Exception e) { 
        }
        return Nombre_Ligne;
    }
    
    //Méthode qui renvoie l'identifiant d'une région
    private int ID_Region(String name_region){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int ID_Region = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            rs = st.executeQuery("SELECT region_id from region where region_name ='"+name_region+"'");
            while(rs.next()){
            ID_Region = rs.getInt("region_id");
        }
        }catch (Exception e) {           
            System.out.println("Erreur"+e.getMessage());
            e.printStackTrace();
        }
        return ID_Region;
    }
    
    //Méthode qui renvoie TRUE si une zone est absente dans une région donnée
    private boolean VerifierZone(int id_region){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int nombre_zone = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM zone where zone_name ='"+Zone_name+"' AND region_id ="+id_region+"");
            while(rs.next()){
            nombre_zone = rs.getInt("rowcount");
        }
        }catch (Exception e) {           
            return false;
        }
        return nombre_zone == 0;
    }  
    
    //Méthode qui renvoie le nombre total de zone dans une région donnée
    private int get_Zones_Number(String region_name){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int Nombre_Ligne = 0;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();                 
            rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM zone inner join region on zone.region_id = region.region_id where region_name = '"+region_name+"'");
            while(rs.next()){
               Nombre_Ligne = rs.getInt("rowcount");
            }             
        }catch(Exception e){
        }
        return Nombre_Ligne;
}
    
//Methodes Publiques---------------------------------------------------------|
    //Méthode qui permet d'ajouter une zone dans la base de donnée
    public boolean AjouterZone(String name_region,String Nom_Zone){
        String requete = null;
        Connection co = null;
        Statement st = null;
        Zone_name = Nom_Zone;
        int id_region = ID_Region(name_region);
               
        if (VerifierZone(id_region) == true) {
            int ID_Zone = get_Highest_ZoneID()+ 1;
            try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            requete = "INSERT INTO zone(zone_id,zone_name,region_id) VALUES("+ ID_Zone +",'"+Zone_name+"',"+id_region+")";
            st.executeUpdate(requete);
            }catch (Exception e) { 
                return false;
            }
            return true;
        }else{
            return false;
        }
  }
   
    //Méthode qui permet de supprimer une zone dans une région donné
    public boolean SupprimerZone(String name_region,String Nom_Zone){
        String requete = null;
        Connection co = null;
        Statement st = null;
        Zone_name = Nom_Zone;
        int id_region = ID_Region(name_region);
        
        if (VerifierZone(id_region) == false) {
            try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            requete = "DELETE from zone where zone_name ='"+Zone_name+"' AND region_id ="+id_region+"";
            st.executeUpdate(requete);
            }catch (Exception e) {  
                return false;
            }
            return true;
        }else{
            return false;
        }
    }
    
    //Méthode qui renvoie le nom des zones présent dans une région donnée
    public String[] get_Zones_Names(String Region_name){
        int number = get_Zones_Number(Region_name);
        String[] name_zone;
        name_zone = new String[number];
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            for (int i = 0; i < number; i++) {
               rs = st.executeQuery("select zone_name from zone inner join region on zone.region_id = region.region_id where region_name = '"+Region_name+"' "
                       + "LIMIT 1 OFFSET "+i+"");              
            while(rs.next()){
               name_zone[i] = rs.getString("zone_name");
             }   
            }                      
        }catch(Exception e){
        }
        
        return name_zone;
}
         
}
