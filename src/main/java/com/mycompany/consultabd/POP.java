/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.consultabd;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Usuario
 */
public class POP {

    private boolean conecionPOP;
    private boolean conecionInicioSecion;
    private String Servidor = "";
    private final int puerto = 110;
    private String Comando = "";
    private String user = "";
    private String pass = "";
    private Socket Conexion;
    private BufferedReader entrada;
    private DataOutputStream salida;

    public POP() {
        this.Servidor = "mail.tecnoweb.org.bo";
        this.Conexion = null;
        this.entrada = null;
        this.salida = null;
        this.conecionPOP = this.conecionInicioSecion = false;
        this.user = "grupo20sc";
        this.pass = "grup020grup020*";
    }

    public boolean getConeccionPOP() {
        return this.conecionPOP;
    }

    public boolean getConeccionInicioSeccion() {
        return this.conecionInicioSecion;
    }

    public void conectar(String host) {
        this.Servidor = host.length() > 0 ? host : this.Servidor;
        System.out.println(" C : telnet " + this.Servidor + " 110");
        try {
            this.Conexion = new Socket(this.Servidor, this.puerto);
            this.entrada = new BufferedReader(new InputStreamReader(Conexion.getInputStream()));
            this.salida = new DataOutputStream(Conexion.getOutputStream());
            System.out.println(" S : " + this.entrada.readLine());
            this.conecionPOP = true;
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage() + "\n!!!Error no se ha podido establecer una coneccion");
            this.Servidor = "mail.tecnoweb.org.bo";
        }
    }

    public void Desconectar() { //comando quit
        try {
            this.Comando = "QUIT \r\n";
            System.out.print(" C : " + this.Comando);
            salida.writeBytes(this.Comando);
            System.out.println(" S : " + entrada.readLine());
            this.conecionPOP = false;
            entrada.close();
            salida.close();
            Conexion.close();
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage());
        }
    }

    public void IniciarSesion(String usuario, String password) {
        try {
            this.user = usuario.length() > 0 ? usuario : this.user;
            this.pass = password.length() > 0 ? password : this.pass;

            //USER
            this.Comando = "USER " + user + "\r\n";
            System.out.print(" C : " + this.Comando);
            salida.writeBytes(this.Comando);
            System.out.println(" S : " + entrada.readLine() + "\r\n");
            //PASS
            this.Comando = "PASS " + this.pass + "\r\n";
            System.out.print(" C : " + this.Comando);
            salida.writeBytes(this.Comando);
            String s = entrada.readLine();
            if (s.equalsIgnoreCase("+OK Logged in.")) {
                this.conecionInicioSecion = true;
                System.out.println(" S : " + s + "\r\n");
            } else {
                System.out.println(" S : !!!Error de inicio de seccion\r\n");
                this.conecionInicioSecion = false;
            }

        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage());
        }
    }

    static protected String getMultiline(BufferedReader in) throws IOException {
        String lines = "";
        while (true) {
            String line = in.readLine();
            if (line == null) {
                // Server closed connection
                throw new IOException(" S : Server unawares closed the connection.");
            }
            if (line.equals(".")) {
                // No more lines in the server response
                break;
            }
            if ((line.length() > 0) && (line.charAt(0) == '.')) {
                // The line starts with a "." - strip it off.
                line = line.substring(1);
            }
            // Add read line to the list of lines
            lines = lines + "\n" + line;
        }
        return lines;
    }

    public String Listar() {
        String S = null;
        try {
            this.Comando = "LIST \r\n";
            System.out.print(" C : " + this.Comando);
            if (getConeccionPOP() && getConeccionInicioSeccion()) {
                salida.writeBytes(this.Comando);
                S = getMultiline(entrada);
            }
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage());
        }
        return S;
    }

    public String cantMensajes() { //comando stat
        String cant = "0";
        try {
            this.Comando = "stat\n";
            this.salida.writeBytes(Comando);
            cant = this.entrada.readLine();
            if (cant.length() > 0) {
                cant = cant.substring(4, cant.length() - 4);
                int i = 1;
                while (cant.charAt(i) != ' ') {
                    i++;
                }
                cant = cant.substring(0, i);
            }
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage());
        }
        return cant;
    }

    public String getMensaje(int id) { //introduce el Id y devuelve el mensaje
        String sms = "El ID " + Integer.toString(id) + " introducido no existe";
        try {
            if (id > 0 && id <= Integer.parseInt(cantMensajes())) {
                this.Comando = "RETR " + Integer.toString(id) + "\n";
                System.out.print(" C : " + this.Comando);
                salida.writeBytes(this.Comando);
                sms = getMultiline(entrada);
            }
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage());
        }
        return sms;
    }

    public String getData(int id) { //introduce el Id y devuelve el Data
        String data = "El mensaje no contiene datos de mensajeria";
        try {
            if (id > 0 && id <= Integer.parseInt(cantMensajes())) {
                this.Comando = "RETR " + Integer.toString(id) + "\n";
                System.out.print(" C : " + this.Comando);
                salida.writeBytes(this.Comando);
                boolean sw = false;
                data = "";
                while (true) {
                    String line = entrada.readLine();
                    if (sw) {
                        data = data + "\n" + line;
                    }
                    if (line == null) {
                        // Server closed connection
                        throw new IOException(" S : Server unawares closed the connection.");
                    }
                    if (line.equals(".")) {
                        // No more lines in the server response
                        break;
                    }
                    if (line.length() > 10) {
                        String x = "";
                        for (int i = 0; i < 10; i++) {
                            x = x + line.charAt(i);
                        }
                        if (x.compareToIgnoreCase("Message-Id") == 0) {
                            sw = true;
                        }
                    }
                    if ((line.length() > 0) && (line.charAt(0) == '.')) {
                        // The line starts with a "." - strip it off.
                        line = line.substring(1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage());
        }
        return data;
    }

    public String getAsunto(int id) { //introduce el Id y devuelve el mensaje
        String lines = "El mensaje no contiene asunto";
        try {
            if (id > 0 && id <= Integer.parseInt(cantMensajes())) {
                this.Comando = "RETR " + Integer.toString(id) + "\n";
                System.out.print(" C : " + this.Comando);
                salida.writeBytes(this.Comando);
                while (true) {
                    String line = entrada.readLine();
                    if (line == null) {
                        // Server closed connection
                        throw new IOException(" S : Server unawares closed the connection.");
                    }
                    if (line.equals(".")) {
                        // No more lines in the server response
                        break;
                    }
                    if (line.length() > 9) {
                        String x = "";
                        for (int i = 0; i < 7; i++) {
                            x = x + line.charAt(i);
                        }
                        if (x.compareToIgnoreCase("Subject") == 0) {
                            lines = line.substring(8);
                            lines = lines.trim();
                        }
                    }
                    if ((line.length() > 0) && (line.charAt(0) == '.')) {
                        // The line starts with a "." - strip it off.
                        line = line.substring(1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage());
        }
        return lines;
    }

    public void EliminarMensaje(int id) {
        try {
            if (id > 0 && id <= Integer.parseInt(cantMensajes())) {
                this.Comando = "dele " + id + "\n";
                System.out.print(" C : " + this.Comando);
                this.salida.writeBytes(this.Comando);
                System.out.println(" S : " + entrada.readLine());
            }
        } catch (Exception e) {
            System.out.println(" C : " + e.getMessage());
        }
    }
}
