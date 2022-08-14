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
public class Propriete {
    
//Attributs---------------------------------------------------------| 
    private int Surface;
    private String Categorie;
    private int NombreChambre;
    private int NombreToilette;
    private int NombreCuisine;
    private int NombreSalon;
    private float Prix;
    private boolean Disponibilite;
    private String Type;
    private int ID;
    
    
//Constructeur(s)---------------------------------------------------------|
    //Constructeur par défaut
    public Propriete(){
        this.Surface = 0;
        this.Categorie = null;
        this.NombreChambre = 0;
        this.NombreToilette = 0;
        this.NombreCuisine = 0;
        this.NombreSalon = 0;
        this.Prix = 0;
        this.Disponibilite = false;
        this.Type = null;
        this.ID = 0;
    }
    
    //Constructeur qui initialise une propriété
    public Propriete(int surface,String categorie,int chambre,int Toilette,int cuisine,int salon,int prix,boolean dispo,String type,int ID){
        this.Surface = surface;
        this.Categorie = categorie;
        this.NombreChambre = chambre;
        this.NombreToilette = Toilette;
        this.NombreCuisine = cuisine;
        this.NombreSalon = salon;
        this.Prix = prix;
        this.Disponibilite = dispo;
        this.Type = type;
        this.ID = ID;
    }
    
//Methodes Privées---------------------------------------------------------|
    //Méthode qui permet de définir la disponibilité de la propriété
    private boolean DefinirDisponibilite(boolean Disponibilite){
        this.Disponibilite = Disponibilite;
        return true;
    }
    
    //Méthode qui permet de définir le prix de la propriété
    private boolean DefinirPrix(float Prix){
        this.Prix = Prix;
        return true;
    }
    
    //Méthode qui permet de définir la catégorie de la propriété
    private boolean DefinirCategorie(String Categorie){
        if (Categorie.contentEquals("Vente") == true) {
            this.Categorie = "Vente";
            return true;
        } else {
            this.Categorie = "Location";
            return true;
        }  
    }
    
    //Méthode qui retourne l'ID d'une zone donné en paramètre
    private int getID_Zone(String zone){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int ID_Zone = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement(); 
            rs = st.executeQuery("SELECT Zone_id FROM zone where zone_name ='"+zone+"'");
            while(rs.next()){
               ID_Zone = rs.getInt("Zone_ID");
            } 
        }catch (Exception e) {  
        }
        return ID_Zone;
    }
    
    //Méthode qui retourne TRUE si une propriété est présente dans une zone donné 
    private boolean VerifierPropriete(String zone){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int Nombre_Ligne = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();  
            rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM propriete where Propriete_ID = "+ID+" AND Zone_ID = "+getID_Zone(zone)+"");           
            while(rs.next()){
               Nombre_Ligne = rs.getInt("rowcount");
            }
        }catch (Exception e) {           
            return false;
        }
        return Nombre_Ligne == 1;
    }
    
//Méthodes Publique---------------------------------------------------------|
    //Méthode qui permet d'ajouter une propriété dans la base de donnée
    public boolean AjouterPropriete(String zone){
        String requete = null;
        Connection co = null;
        Statement st = null;
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();                
            requete = "INSERT INTO Propriete(Propriete_ID, Categorie,Zone_ID,"
                    + "Surface,Nbre_Chambre,"
                    + "Nbre_Toilette,Nbre_Cuisine,Nbre_Salon,Type,Prix,Disponibilite_Propriete) "
                    + "VALUES ("+ID+",'"+Categorie+"',"+getID_Zone(zone)+","+Surface+","+NombreChambre+","+NombreToilette+","
                    + ""+NombreCuisine+","+NombreSalon+",'"+Type+"',"+Prix+","+Disponibilite+")";             
            st.executeUpdate(requete);         
        }catch(Exception e){          
            return false;
        }
        return true;        
    }
    
    //Méthode qui permet de modifier une propriété
    public boolean ModifierPropriete(String zone,int ID_Propriete,float Prix,String Categorie, boolean Dispo,int ID_EVENT){
        String requete = null;
        Connection co = null;
        Statement st = null;
        ID = ID_Propriete;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement(); 
            
            if(VerifierPropriete(zone) == true)
            {
                int choix = 0;
                                 
                choix = ID_EVENT;
                switch(choix)
                {
                    case 1:
                        if(this.DefinirPrix(Prix) == true){
                            requete = "Update propriete set prix = "+this.Prix+" where Propriete_ID ="+ID+"";             
                            st.executeUpdate(requete);
                            return true;
                        }else{                           
                            return false;
                        }
                    case 2:
                        if(this.DefinirDisponibilite(Dispo)== true){
                            requete = "Update propriete set Disponibilite_propriete = "+this.Disponibilite+" where "
                                    + "Propriete_ID ="+ID+"";             
                            st.executeUpdate(requete);
                            return true;
                        }else{
                            return false;
                        }
                    case 3:
                        if(this.DefinirCategorie(Categorie)== true){
                            requete = "Update propriete set Categorie = '"+this.Categorie+"' where "
                                    + "Propriete_ID ="+ID+"";             
                            st.executeUpdate(requete);
                            return true;
                        }else{
                            return false;
                        }
                    case 4:
                        return false;
                }
                }else{
                return false;
            }        
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    //Méthode qui retire une propriété de la base de donnée
    public boolean RetirerPropriete(String zone,int ID_Propriete){
        String requete = null;
        Connection co = null;
        Statement st = null;
        ID = ID_Propriete;
        try
        {
            if(VerifierPropriete(zone) == true)
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
                st = co.createStatement(); 
                requete = "DELETE from propriete where Propriete_ID ="+ID+"";             
                st.executeUpdate(requete);                
                return true;
            }else{
                return false;
            }        
        }catch(Exception e){
            return false;
        }
    }
}
