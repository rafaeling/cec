/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cec;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author rafaeling
 */
public class CEC {

    private String fecha;
    
    private String cambio;
    
    static String resultado;
    
    PrintWriter writer = null;
    
    public CEC()
    {
        
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
    }
    
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
          BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
          String jsonText = readAll(rd);
          JSONObject json = new JSONObject(jsonText);
          return json;
        } finally {
          is.close();
        }
    }
    
    public String getFecha()
    {
        return fecha;
    }

    public String getCambio()
    {
        return cambio;
    }

    
    public void descargarMoneda()
    {
        JSONObject json = null;
        
        try {
            json = readJsonFromUrl("http://api.fixer.io/latest");

            String date = null;
            
            date = (String) json.get("date");
            
            fecha = date.replace('-', '/');
            
            cambio = json.getJSONObject("rates").get("USD").toString();
            
        } catch (IOException ex) {
            //Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex);
            
            resultado = "Error " + ex.getMessage() + " " + ex.toString();
            
            System.out.println(resultado);
           
            try {
                writer = new PrintWriter("logFile.txt", "UTF-8");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (UnsupportedEncodingException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            }
            writer.println(resultado);
            writer.close();

        } catch (JSONException ex) {
            //Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex);
            
            resultado = "Error " + ex.getMessage() + " " + ex.toString();
            
            System.out.println(resultado);
           
            try {
                writer = new PrintWriter("logFile.txt", "UTF-8");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (UnsupportedEncodingException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            }
            writer.println(resultado);
            writer.close();
            
        }
    
    }
    
    public static void main(String[] args){
      
        CEC dollar = new CEC();
      
        PrintWriter writer2 = null;
        
        dollar.descargarMoneda();
        
        System.out.println(dollar.getCambio());
        
        System.out.println("-------- SQL JDBC Connection Testing ------------");

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {

            resultado = "Where is your SQL JDBC Driver?";
            
            System.out.println(resultado);
            
            try {
                writer2 = new PrintWriter("logFile.txt", "UTF-8");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (UnsupportedEncodingException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            }
            writer2.println(resultado);
            writer2.close();
            e.printStackTrace();
            return;
        }  
        
        resultado = "SQL JDBC Driver Registered!";
        
        System.out.println(resultado);
            
        try {
            writer2 = new PrintWriter("logFile.txt", "UTF-8");
        } catch (FileNotFoundException ex1) {
            Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
        } catch (UnsupportedEncodingException ex1) {
            Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
        }
        writer2.println(resultado);
        writer2.close();
        
        
        Connection connection = null; 
        
        String host = "*******";
    
        String username = "*******";

        String password = "*******";

        String namedatabase = "************";

        String url = "jdbc:sqlserver://"+host+";databaseName="+namedatabase+"";

        try {
       
            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            
            
            resultado = "Connection Failed! Check logFile " + e.getMessage() + "\n" + e.toString();
        
            System.out.println(resultado);

            try {
                writer2 = new PrintWriter("logFile.txt", "UTF-8");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (UnsupportedEncodingException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            }
            writer2.println(resultado);
            writer2.close();


            e.printStackTrace();
            //System.out.println(e.getMessage());
            return;
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
        
        
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
        } catch (SQLException ex) {
            
            resultado = "Error " + ex.getMessage() + " " + ex.toString();
            
            System.out.println(resultado);
           
            try {
                writer2 = new PrintWriter("logFile.txt", "UTF-8");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (UnsupportedEncodingException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            }
            writer2.println(resultado);
            writer2.close();
            
        }
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        
        String con="INSERT INTO ORTT (RateDate, Currency, Rate, DataSource, UserSign) VALUES ('"+dateFormat.format(date)+"', 'USD', "+dollar.getCambio()+", 'I', 34);";
        
        System.out.println(con);
        
        try {
            ResultSet executeQuery = stmt.executeQuery(con);

        } catch (SQLException ex) {
            
            resultado = "Error " + ex.getMessage() + " " + ex.toString();
            
            System.out.println(resultado);
           
            try {
                writer2 = new PrintWriter("logFile.txt", "UTF-8");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (UnsupportedEncodingException ex1) {
                Logger.getLogger(CEC.class.getName()).log(Level.SEVERE, null, ex1);
            }
            writer2.println(resultado);
            writer2.close();
        }
        
    
    }
    
}
