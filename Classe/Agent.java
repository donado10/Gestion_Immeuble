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
public class Agent {
    
//Attributs---------------------------------------------------------|   
    private String FirstName_agent;
    private String LastName_agent;
    private int Agent_ID;
    private boolean Disponibilite_agent;
    
//Constructeur(s)---------------------------------------------------------| 
    //Constructeur par défaut
    public Agent(){
        this.FirstName_agent = null;
        this.LastName_agent = null;
        this.Agent_ID = 0;
        this.Disponibilite_agent = false;
    }
    
    //Constructeur qui initialise un agent
    public Agent(String firstname,String lastname,boolean Disponibilite){
        this.FirstName_agent = firstname;
        this.LastName_agent = lastname;
        this.Agent_ID = 0;
        this.Disponibilite_agent = Disponibilite;
    }
    
//Methodes Privées---------------------------------------------------------| 
    //Méthode qui permet de définir la disponibilité d'un agent
    private boolean DefinirDisponibilite(boolean Disponibilite){       
        this.Disponibilite_agent = Disponibilite;
        return true;   
    }
    
    //Méthode qui renvoie l'identifiant d'une zone
    private int ID_Zone(String zone){
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
    
    //Méthode qui retourne TRUE si une zone est présente dans la base de donnée
    private boolean VerifierZone(String Zone_name){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int nombre_zone = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM zone where zone_name ='"+Zone_name+"'");
            while(rs.next()){
            nombre_zone = rs.getInt("rowcount");
        }
        }catch (Exception e) {           
            return false;
        }
        return nombre_zone == 1;
    } 
    
    //Méthode qui retourne TRUE si un agent est présent dans la base de donnée
    private boolean VerifierAgent(int ID_Agent){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int verifier_agent = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM Agent where Agent_ID = "+ID_Agent+"");           
            while(rs.next()){
               verifier_agent = rs.getInt("rowcount");
            }
        }catch (Exception e) {           
            return false;
        }
        return verifier_agent == 1;
    } 
    
    //Méthode qui retourne TRUE si un agent est affecté à une zone donnée
    private boolean VerifierAgent_Zone(int ID_Agent, String zone){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int verifier_agent = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM agent_zone where Agent_ID = "+ID_Agent+"  AND zone_id = "+ID_Zone(zone)+"");           
            while(rs.next()){
               verifier_agent = rs.getInt("rowcount");
            }
        }catch (Exception e) {           
            return false;
        }
        return verifier_agent == 1;
    } 
    
    //Méthode qui permet d'affecter une zone à un agent
    private boolean Affecter_Zone(String Add_zone){
        String requete = null;
        Connection co = null;
        Statement st = null;
        
        if (VerifierZone(Add_zone)) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
                st = co.createStatement();
                requete = "INSERT INTO Agent_zone(agent_id,zone_id) VALUES ("+this.Agent_ID+","+ ID_Zone(Add_zone) +")";
                st.executeUpdate(requete); 
            } catch (Exception e) {
                return false;
            }               
        } else {
            return false;
        }
        
        return true;
    }
    
    //Méthode qui permet de supprimer une zone affectée à un agent
    private boolean Retirer_Affectation(String Delete_zone){
        String requete = null;
        Connection co = null;
        Statement st = null;
        
        if (VerifierAgent_Zone(Agent_ID, Delete_zone) == true) {
            try { 
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
                st = co.createStatement();
                requete = "DELETE from Agent_zone where agent_id ="+Agent_ID+" AND zone_id = "+ID_Zone(Delete_zone)+"";
                st.executeUpdate(requete);           
                return true;
            } catch (Exception e) {
                return false;
            }               
        } else {
            return false;
        }        
    }
    
    //Méthode qui renvoie l'identifiant le plus grand d'un agent dans la base de donnée 
    private int get_Highest_AgentID(){
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;
        int Nombre_Ligne = 0;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            rs = st.executeQuery("select agent_id from agent order by agent_id desc LIMIT 1");
            while(rs.next()){
               Nombre_Ligne = rs.getInt("agent_id");
            } 

        } catch (Exception e) {
        }       
        return Nombre_Ligne;
    }
    
 //Méthodes Publique---------------------------------------------------------| 
    //Méthode qui permet d'ajouter un agent dans la base de donnée
    public boolean AjouterAgent(){
        String requete = null;
        Connection co = null;
        Statement st = null;   
        int ID_Agent = get_Highest_AgentID()+ 1;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();                                                                    
            requete = "INSERT INTO agent(Agent_ID, firstname,lastname,Disponibilite_Agent) "
                    + "VALUES ("+ ID_Agent +",'"+FirstName_agent+"','"+LastName_agent+"',"+Disponibilite_agent+")";             
            st.executeUpdate(requete);         
        }catch(Exception e){
            return false;
        }
        return true;     
    }
    
    //Méthode qui permet de modifier un agent
    public boolean ModifierAgent(int ID_Agent,boolean Disponibilite,String Add_zone,String Delete_zone,int ID_EVENT){
        String requete = null;
        Connection co = null;
        Statement st = null;
        Agent_ID = ID_Agent;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement();
            if(VerifierAgent(Agent_ID) == true)
            {
                int choix = ID_EVENT;              
                switch(choix)
                {
                    case 1:
                        if(this.DefinirDisponibilite(Disponibilite)== true){
                            requete = "Update Agent set Disponibilite_Agent = "+this.Disponibilite_agent+" where Agent_ID ="+Agent_ID+"";             
                            st.executeUpdate(requete);
                            return true;
                        }else{
                            return false;
                        }
                    case 2:
                        if(this.Affecter_Zone(Add_zone)== true){
                            return true;
                        }else{
                            return false;
                        }
                    case 3:
                        if (this.Retirer_Affectation(Delete_zone) == true) {
                            return true;
                        } else {
                            return false;
                        }                       
                }
            }else{
                return false;
            }        
        }catch(Exception e){
            return false;
        }
        return false;
    }
    
    //Méthode qui permet de supprimer un agent de la base de donnée
    public boolean SupprimerAgent(int ID){
        String requete = null;
        Connection co = null;
        Statement st = null;
        Agent_ID = ID;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            co = DriverManager.getConnection("jdbc:mysql://localhost:3306/immeuble?characterEncoding=latin1&useConfigs=maxPerformance","root","root");
            st = co.createStatement(); 
            if(VerifierAgent(ID) == true)
            {
                requete = "DELETE from agent_zone where Agent_ID ="+Agent_ID+"";             
                st.executeUpdate(requete);   
                requete = "DELETE from agent where Agent_ID ="+Agent_ID+"";             
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
