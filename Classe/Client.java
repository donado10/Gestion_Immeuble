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
public class Client extends Utilisateur {

    
//Constructeur(s)---------------------------------------------------------| 
    //Constructeur par défaut
    public Client(){
        super.firstname = null;
        super.lastname = null;
        super.pseudo = null;
        super.password = null;
        super.numero = 0;
        super.region = null;
        super.id = 0;
    }
    
    //Constructeur qui initialise un client
    public Client(String first_name, String last_name, String pseudonyme,String password,int numero,String region){
        super.firstname = first_name;
        super.lastname = last_name;
        super.pseudo = pseudonyme;
        try {
            super.password = HashMotDePasse(password);
        } catch (Exception e) {
        }
        super.numero = numero;
        super.region = region;
    }
    
//Méthodes Privées---------------------------------------------------------|   
    //Méthode qui retourne TRUE si les informations de connexion correspond à ce qui est présent dant la base de donnée
    private boolean Information_Connexion(String pseudo,String password){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int Nombre_Ligne = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();             
            rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM utilisateur where pseudo = '"+pseudo+"' AND password = '"+password+"'  AND type = 'Client'");
            while(rs.next()){
                Nombre_Ligne = rs.getInt("rowcount");
            }
        } catch (Exception e) {
            return false;
        }
        return Nombre_Ligne == 1;
    }
    
    //Méthode qui retourne l'identifiant du rendez-vous le plus grand présent dans la base de donnée
    private int getHighest_RDV_Id(){       
        int Nombre_Ligne = 0;
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;

        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();

            rs = st.executeQuery("SELECT rdv_id FROM rendez_vous order by rdv_id desc LIMIT 1");
            while(rs.next()){
                Nombre_Ligne = rs.getInt("rdv_id");
            }

        } catch (Exception e) {
        }       
        return Nombre_Ligne;
    }
    
//Methodes polymorphiques---------------------------------------------------------|
    //Méthode pour créer un compte Client
    @Override
    public boolean CreerCompte(){   
        String requete = null;
        Connection co = null;
        Statement st = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();             
            
            id = get_Highest_UserID() + 1;
            
            if (VerifierPseudo(pseudo) == true) {
                return false;
            }else{
                requete = "INSERT INTO Utilisateur(User_ID,Firstname,Lastname,Pseudo,Password,Telephone,Region,Type) VALUES ("+id+",'"+this.firstname+"', '"+this.lastname+"','"+this.pseudo+"','"+this.password+"',"+this.numero+",'"+this.region+"', 'Client')";             
                st.executeUpdate(requete);
            }                        
        }catch(Exception e){
            return false;
        }      
        return true;
    }
    
    //Méthode pour se connecter en tant que client
    @Override
    public boolean connect(String user, String passwordUser) {
        //requete SQL
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;       
        try {
            passwordUser = HashMotDePasse(passwordUser);
        } catch (Exception e) {
            return false;
        }
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            if (Information_Connexion(user, passwordUser) == true) {
                rs = st.executeQuery("SELECT * from utilisateur where pseudo = '"+user+"' AND password = '"+passwordUser+"' AND type = 'Client'");               
                while(rs.next()){
                    id = rs.getInt("User_ID");
                    firstname = rs.getString("Firstname");
                    lastname = rs.getString("Lastname");
                    pseudo = rs.getString("Pseudo");
                    region = rs.getString("Region");
                    password = rs.getString("Password");           
                }
            }else{
                return false;
            }           
        } catch (Exception e) {
        } 
        return true;
    }
   
    //Méthode pour faire une demande de rendez-vous rendez-vous
    @Override
    public boolean Valider_RDV(int get_ProprieteID){
        Connection co = null;
        Statement st = null;
        
        int RDV_ID = getHighest_RDV_Id() + 1;                       
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement(); 
            String requete = "INSERT INTO rendez_vous(rdv_id, user_id, propriete_id, etat) "
                        + "VALUES ("+ RDV_ID +","+this.id+",'"+get_ProprieteID+"', 'Attente')";         
            st.executeUpdate(requete);
        }catch(Exception e){
            return false;
        }
        return true;
    } 
}
