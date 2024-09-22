/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.consultabd;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Usuario
 */
public class SMTP {

    private boolean conexionSMTP;
    private String Servidor = "";
    private final int puerto = 25;
    private String mail_from = ""; //emisor
    private String rcpt_to = "";   //receptor
    private String Comando = "";
    private Socket Conexion;
    private BufferedReader entrada;
    private DataOutputStream salida;

    public SMTP() {
        this.Servidor = "mail.tecnoweb.org.bo";
        this.mail_from = "jhoeldebrayquispe@gmail.com";
        this.rcpt_to = "grupo20sc@tecnoweb.org.bo";
        this.Conexion = null;
        this.entrada = null;
        this.salida = null;
        this.conexionSMTP = false;
    }

    public boolean getConexionSMTP() {
        return this.conexionSMTP;
    }

    public void conectar(String host) {
        this.Servidor = (host.length() > 0) ? host : this.Servidor;
        System.out.println(" C : telnet " + this.Servidor + " 25");
        try {
            this.Conexion = new Socket(this.Servidor, this.puerto);
            this.entrada = new BufferedReader(new InputStreamReader(Conexion.getInputStream()));
            this.salida = new DataOutputStream(Conexion.getOutputStream());
            
            System.out.println(" S : " + this.entrada.readLine());
            this.conexionSMTP = true;
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage() + "\n!!!Error no se ha podido establecer una coneccion");
            this.Servidor = "mail.tecnoweb.org.bo";
        }
    }

    public void Desconectar() { //comando QUIT
        try {
            this.Comando = "QUIT \r\n";
            System.out.print(" C : " + this.Comando);
            salida.writeBytes(this.Comando);
            System.out.println(" S : " + entrada.readLine());
            this.conexionSMTP = false;
            entrada.close();
            salida.close();
            Conexion.close();
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage());
        }
    }

    public void IniciarSesion() { //comando helo
        try {
            //HELO
            this.Comando = "HELO " + this.Servidor + " \r\n";
            System.out.print(" C : " + this.Comando);
            salida.writeBytes(this.Comando);
            System.out.println(" S : " + entrada.readLine());
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage());
        }
    }

    public void EnviarCorreo(String emisor, String receptor, String subject, String data) {
        try {
            this.mail_from = emisor.length() > 0 ? emisor : this.mail_from;
            this.rcpt_to = receptor.length() > 0 ? receptor : this.rcpt_to;

            //MAIL FROM
            this.Comando = "MAIL FROM: " + this.mail_from + " \r\n";
            System.out.print(" C : " + this.Comando);
            salida.writeBytes(this.Comando);
            System.out.println(" S : " + entrada.readLine());

            //RCPT TO
            this.Comando = "RCPT TO: " + this.rcpt_to + " \r\n";
            System.out.print(" C : " + this.Comando);
            salida.writeBytes(this.Comando);
            System.out.println(" S : " + entrada.readLine());

            //DATA
            this.Comando = "DATA \n";
            System.out.print(" C : " + this.Comando);
            salida.writeBytes(this.Comando);
            System.out.println(" S : " + entrada.readLine());
            //\r\n es un salto de carro y de linea

            this.Comando = "SUBJECT: " + subject + "\r\n" + data + "\r\n.\r\n";
            System.out.print(" C : " + this.Comando);
            salida.writeBytes(this.Comando);
            System.out.println(" S : " + entrada.readLine());
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage());
        }
    }
}
