/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Immeuble.Class;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author LBC
 */
public abstract class Utilisateur {
    
//Attributs---------------------------------------------------------| 
    protected String firstname;
    protected String lastname;
    protected String pseudo;
    protected int id;
    protected String region;
    protected String password;
    protected long numero;

//Méthodes publiques et abstraites---------------------------------------------------------|
    //Méthode pour créer un compte utilisateur
    public abstract boolean CreerCompte();
    
    //Méthode pour se connecter en tant qu'utilisateur
    public abstract boolean connect(String user, String password);
    
    //Méthode pour valider un rendez-vous
    public abstract boolean Valider_RDV(int valider);
    
//Méthodes Protected---------------------------------------------------------|
    //Méthode qui retourne TRUE si un pseudonyme existe dans la base de donnée
    protected boolean VerifierPseudo(String pseudo){
        int Nombre_Ligne = 0;
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement(); 
            rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM utilisateur where pseudo ='"+pseudo+"'");
        while(rs.next()){
            Nombre_Ligne = rs.getInt("rowcount");
        }
        } catch (Exception e) {
            return false;
        }
        return Nombre_Ligne == 1;
    }
    
    //Méthode pour hacher un mot de passe
    protected String HashMotDePasse(String password) throws NoSuchAlgorithmException{
	  
      MessageDigest md = MessageDigest.getInstance("SHA-256");

      md.update(password.getBytes());
      
      byte[] digest = md.digest();
     
      StringBuffer hexString = new StringBuffer();
      
      for (int i = 0;i<digest.length;i++) {
         hexString.append(Integer.toHexString(0xFF & digest[i]));
      }
      password = hexString.toString();
      return password;
    }
    
    //Méthode static qui renvoie TRUE si l'identifiant d'un rendez-vous est présent dans la base de donnée
    protected static boolean Verifier_RDV(int id_RDV){
        String requete = null;
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;    
        int Nombre_Ligne = 0;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();                
            requete = "select COUNT(*) as rowcount from rendez_vous where RDV_id = "+id_RDV+"";   
            rs = st.executeQuery(requete);
            while(rs.next()){
               Nombre_Ligne = rs.getInt("rowcount");
            }            
        }catch(Exception e){
            return false;
        }
        return Nombre_Ligne == 1;
    }
       
    //Méthode static qui retourne le plus grand idendifiant d'un utilisateur présent dans la base de donnée
    protected static int get_Highest_UserID(){
        int highest_id = 0;
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement(); 
            rs = st.executeQuery("select User_id from utilisateur order by User_id desc LIMIT 1");
        while(rs.next()){
            highest_id = rs.getInt("User_id");
        }
        } catch (Exception e) {
            return -1;
        }
        return highest_id;
    }

//Méthodes Publiques---------------------------------------------------------| 
    //Méthode qui permet à un utilisateur d'annuler un RDV
    public boolean Annuler_RDV(int annuler){
        String requete = null;
        Connection co = null;
        Statement st = null;
        boolean verification = Utilisateur.Verifier_RDV(annuler);
        if (verification == true) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
                st = co.createStatement();                
                requete = "delete from rendez_vous where RDV_ID = "+annuler+"";           
                st.executeUpdate(requete);         
            }catch(Exception e){
                return false;
            }
            return true;
        } else {
            return false;
        }            
    }
    
    //Méthode qui retourne les informations d'un utilisateur
    public String[] get_User_info(){
        String[] information = new String[]{""+this.id+"",this.firstname,this.lastname};
        return information;
    }
}
