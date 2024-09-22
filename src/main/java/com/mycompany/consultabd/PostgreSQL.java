/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.consultabd;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Usuario
 */
public class PostgreSQL {
    private String Servidor="";
    private String user="";
    private String pass="";
    private String BD="";
    private String tabla="";
    String Comando="";
    Connection conexion;
    Statement st;
    ResultSet rs;
    private final int puerto=5432;
    private String url;
    private boolean conecionSQL;
    private int cantConsulta; 
    private String cadena="";
    SMTP co = new SMTP(); //para enviar la respuesta de la consulta a la DB
    
    
    
    
    public PostgreSQL(){
        this.conecionSQL = false;
        this.cantConsulta = 0;
        //mail.tecnoweb.org.bo = "37.187.122.232"; 
        this.Servidor = "mail.tecnoweb.org.bo";
        this.user = "agenda";
        this.pass = "agendaagenda";
        this.BD = "db_agenda";
        this.tabla = "persona";
        this.url="jdbc:postgresql://" + this.Servidor + ":"+ Integer.toString(puerto)+"/"+BD;
        try { 
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver PostgreSQL registrado con éxito.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
        }
        this.conexion = null;  
    }
    
    public void conectar(String host, String us, String pw, String db, String tb){
        this.Servidor = host.length()>0?host:this.Servidor;
        this.user = us.length()>0?us:this.user;
        this.pass = pw.length()>0?pw:this.pass;
        this.BD = db.length()>0?db:this.BD;
        this.tabla = tb.length()>0?tb:this.tabla;
        System.out.println(" C : telnet "+this.Servidor+" 5432");
        try{
            this.url="jdbc:postgresql://"+this.Servidor+":"+Integer.toString(puerto)+"/"+BD;
            conexion = DriverManager.getConnection(this.url,this.user,this.pass);
            this.conecionSQL = conexion.isValid(50000);
            System.out.println(" S: "+(this.conecionSQL?"TEST OK":"TEST FAIL"));
        } catch (Exception e){
            System.out.println(" C : "+ e.getMessage()+"\nError no se ha podido establecer una coneccion a la Base de datos");
            this.Servidor = "mail.tecnoweb.org.bo"; 
        }
    }   
    
    public void Desconectar(){
        try{
            conexion.close();
            this.conecionSQL = conexion.isValid(50000);
            System.out.println(" S: Se desconecto con la base de datos");
        } catch (Exception e){
            System.out.println(" C : "+ e.getMessage());
        }
    }
    
    public boolean getConeccionSQL(){
        return this.conecionSQL;
    }
    
    public String[][] Consultar (String c, String em, String re){
        String [][] arrDatos = new String[20][13];
            for(int i=0;i<20;i++) {
                for(int j=0;j<13;j++){
                    arrDatos[i][j]="";
                }
            }
        try{
            String consulta = "SELECT * "        
                    + "FROM persona WHERE per_cod LIKE '%" + c + "%' OR per_nom LIKE '%" + c + "%' OR per_appm LIKE '%" + c 
                    + "%' OR per_prof LIKE '%" + c + "%' OR per_telf LIKE '%" + c + "%' OR per_cel LIKE '%" + c 
                    + "%' OR per_email LIKE '%" + c + "%' OR per_dir LIKE '%" + c + "%' OR per_lnac LIKE '%" + c + "%'";
            
            cadena = "";        
            int i = 0;
            st = conexion.createStatement();
            rs = st.executeQuery(consulta);                
            while (rs.next()){
                String per_cod = rs.getString("per_cod");
                per_cod = per_cod.replaceAll(" +", " ");
                arrDatos[i][0] = per_cod;
                System.out.print(arrDatos[i][0]+"\t ");
                cadena = cadena + arrDatos[i][0]+"\t";
                
                String per_nom = rs.getString("per_nom");
                per_nom = per_nom.replaceAll(" +", " ");
                arrDatos[i][1] = per_nom;
                System.out.print(arrDatos[i][1]+"\t ");
                cadena = cadena + arrDatos[i][1]+"\t";
                
                String per_appm = rs.getString("per_appm");
                per_appm = per_appm.replaceAll(" +", " ");
                arrDatos[i][2] = per_appm;
                System.out.print(arrDatos[i][2]+"\t ");
                cadena = cadena + arrDatos[i][2]+"\t";
                
                String per_prof = rs.getString("per_prof");
                per_prof = per_prof.replaceAll(" +", " ");
                arrDatos[i][3] = per_prof;
                System.out.print(arrDatos[i][3]+"\t ");
                cadena = cadena + arrDatos[i][3]+"\t";
                
                String per_telf = rs.getString("per_telf");
                per_telf = per_telf.replaceAll(" +", " ");
                arrDatos[i][4] = per_telf;
                System.out.print(arrDatos[i][4]+"\t ");
                cadena = cadena + arrDatos[i][4]+"\t";
                
                String per_cel = rs.getString("per_cel");
                per_cel = per_cel.replaceAll(" +", " ");
                arrDatos[i][5] = per_cel;
                System.out.print(arrDatos[i][5]+"\t ");
                cadena = cadena + arrDatos[i][5]+"\t";
                
                String per_email = rs.getString("per_email");
                per_email = per_email.replaceAll(" +", " ");
                arrDatos[i][6] = per_email;
                System.out.print(arrDatos[i][6]+"\t ");
                cadena = cadena + arrDatos[i][6]+"\t";
                
                String per_dir = rs.getString("per_dir");
                per_dir = per_dir.replaceAll(" +", " ");
                arrDatos[i][7] = per_dir;
                System.out.print(arrDatos[i][7]+"\t ");
                cadena = cadena + arrDatos[i][7]+"\t";
                
                String per_fnac = rs.getString("per_fnac"); 
                per_fnac = per_fnac.replaceAll(" +", " ");
                arrDatos[i][8] = per_fnac;
                System.out.print(arrDatos[i][8]+"\t ");
                cadena = cadena + arrDatos[i][8]+"\t";
                
                String per_lnac = rs.getString("per_lnac");
                per_lnac = per_lnac.replaceAll(" +", " ");
                arrDatos[i][9] = per_lnac;
                System.out.print(arrDatos[i][9]+"\t ");
                cadena = cadena + arrDatos[i][9]+"\t";
                
                String per_est = rs.getString ("per_est");
                per_est = per_est.replaceAll(" +", " ");
                arrDatos[i][10] = per_est;
                System.out.println(arrDatos[i][10]+"\t ");
                cadena = cadena + arrDatos[i][10]+"\t";
                 
                String per_create = rs.getString ("per_create");
                per_create = per_create.replaceAll(" +", " ");
                arrDatos[i][11] = per_create;
                System.out.println(arrDatos[i][11]+"\t ");
                cadena = cadena + arrDatos[i][11]+"\t";
                
                String per_update = rs.getString ("per_update");
                per_update = per_update.replaceAll(" +", " ");
                arrDatos[i][12] = per_update;
                System.out.println(arrDatos[i][12]+"\t ");
                cadena = cadena + arrDatos[i][12]+"\r\n";
                i++;
            }
            this.cantConsulta = i+1;
        if (em!="" && re!=""){
            co.conectar("mail.tecnoweb.org.bo");
            co.IniciarSesion();
            co.EnviarCorreo(em, re, "Patrón de busqueda: " + c , cadena);
            co.Desconectar();
        }
        st.close();
        rs.close();
        } catch (Exception e){
            System.out.println(" C : "+ e.getMessage());
        }
        return arrDatos;
    }  
    public int getCantConsultas(){
        return this.cantConsulta;
    }   

}
