///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package conexaoSql;
//
//import static conexaoSql.Consulta.connection;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
///**
// * Conecta.java
// *
// * @version 1.0 12/03/2017
// * @author Rubens Júnior
// */
//public class Conecta {
//
//    
//    // Nome e URL do banco de dados.
//    private static final String BD_NAME = "bdprograma";
//    private static final String BD_URL = "jdbc:mysql://192.168.1.12:3306/"+BD_NAME;
//    
//    // Acesso ao servidor: usuário e senha.
//    private static final String USERNAME = "mesor";
//    private static final String PASSWORD = "mesorufca1506";
//        
//    // gerencia a conexão.
//    public static Connection connection;
//    
//    connection = DriverManager.getConnection(
//                        BD_URL, USERNAME, PASSWORD);
//    
//    /**
//     * Construtores.
//     */
//    public Conecta() {}
//    
//    // -------------------------------------------------------------------------
//    // Métodos.
//    // -------------------------------------------------------------------------
//    
//}