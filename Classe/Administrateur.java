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
public class Administrateur extends Utilisateur{

    
//Constructeur(s)---------------------------------------------------------| 
    //Constructeur par défaut
    public Administrateur(){
        super.firstname = null;
        super.lastname = null;
        super.pseudo = null;
        super.password = null;
        super.numero = 0;
        super.region = null;
        super.id = 0;
    }
    
    //Constructeur qui initialise un administrateur
    @SuppressWarnings("empty-statement")
    public Administrateur(String first_name, String last_name, String pseudonyme,String password,int numero,String region){
        super.firstname = first_name;
        super.lastname = last_name;
        super.pseudo = pseudonyme;
        try {
            super.password = HashMotDePasse(password);
        } catch (Exception e) {;
        }
        super.numero = numero;
        super.region = region;
    }
    
//Methodes Privées---------------------------------------------------------| 
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
            rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM utilisateur where pseudo = '"+pseudo+"' AND password = '"+password+"'  AND type = 'Administrateur'");
            while(rs.next()){
                Nombre_Ligne = rs.getInt("rowcount");
            }
        } catch (Exception e) {
            return false;
        }
        return Nombre_Ligne == 1;
    }
    
    //Méthode qui retourne TRUE si le pseudo de l'utilisateur contient le code pour accèder au panel de l'administrateur
    private boolean Check_AdminCode(String user){
        String CompanyName = "@FSAcompany";
        CompanyName = CompanyName.toLowerCase();
        user = user.toLowerCase();
        return user.substring(user.length() - CompanyName.length()).contentEquals(CompanyName) == true;
    }
    
    //Méthode qui retourne le pseudo de l'utilisateur sans le code 
    private String Cut_AdminCode(String user){
       String CompanyName = "@FSAcompany";
       CompanyName = CompanyName.toLowerCase(); 
       return user.substring(0,user.length() - CompanyName.length());    
    }
    
//Methodes polymorphiques---------------------------------------------------------|
    //Méthode pour créer un compte administrateur
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
                requete = "INSERT INTO Utilisateur(User_ID,Firstname,Lastname,Pseudo,Password,Telephone,Region,Type) VALUES ("+id+",'"+this.firstname+"', '"+this.lastname+"','"+this.pseudo+"','"+this.password+"',"+this.numero+",'"+this.region+"', 'Administrateur')";             
                st.executeUpdate(requete); 
            }
        }catch(Exception e){
            System.out.println("Erreur"+e.getMessage());
            e.printStackTrace();
            return false;
        }      
        return true;
    }

    //Méthode pour se connecter en tant qu'administrateur
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
        
        if(Check_AdminCode(user) == true){                   
            user = Cut_AdminCode(user);
            try 
            {
                if (Information_Connexion(user, passwordUser) == true) {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
                    st = co.createStatement(); 
                    rs = st.executeQuery("SELECT * from utilisateur where pseudo = '"+user+"' AND password = '"+passwordUser+"' AND type = 'Administrateur'");               
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
        }else{
            return false;
        }
        return true;
    }
    
    //Méthode pour valider la prise de rendez-vous d'un client
    @Override
    public boolean Valider_RDV(int valider){
        String requete = null;
        Connection co = null;
        Statement st = null; 
        boolean verification = Verifier_RDV(valider) ;
        if(verification == true){
           try
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
                st = co.createStatement();                
                requete = "Update rendez_vous set Etat = 'Valide' where RDV_id = "+valider+"";           
                st.executeUpdate(requete);         
            }catch(Exception e){
                return false;
            }
            return true; 
        }else{
            return false;
        }        
    }
   }

