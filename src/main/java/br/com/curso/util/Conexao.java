/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {
    
    private static Connection con = null;
    
    public static Connection getConection(){
        if(con == null){
            try {
                String driver = "org.postgresql.Driver";
                String url = "jdbc:postgresql://localhost:5432/novadelli";
                String usuario = "postgres";
                String senha = "postgres";
                Class.forName(driver);
                con = DriverManager.getConnection(url,usuario,senha);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return con;
    }
    
    public static void closeConection(){
        if(con != null){
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
