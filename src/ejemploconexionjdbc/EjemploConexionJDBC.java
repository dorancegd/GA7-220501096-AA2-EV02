/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ejemploconexionjdbc;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EjemploConexionJDBC {

    private static final String URL = "jdbc:mysql://localhost:3306/prueba";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);

            while (!salir) {
                System.out.println("\n=== MENÚ ===");
                System.out.println("1. Insertar usuario");
                System.out.println("2. Consultar usuarios");
                System.out.println("3. Actualizar usuario");
                System.out.println("4. Eliminar usuario");
                System.out.println("5. Salir");
                System.out.print("Selecciona una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir salto de línea

                switch (opcion) {
                    case 1:
                        insertarUsuario(conexion, scanner);
                        break;
                    case 2:
                        consultarUsuarios(conexion);
                        break;
                    case 3:
                        actualizarUsuario(conexion, scanner);
                        break;
                    case 4:
                        eliminarUsuario(conexion, scanner);
                        break;
                    case 5:
                        salir = true;
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida, intenta nuevamente.");
                }
            }

            conexion.close();
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(EjemploConexionJDBC.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static void insertarUsuario(Connection conexion, Scanner scanner) throws SQLException {
        System.out.print("Ingrese el nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Ingrese la contraseña: ");
        String password = scanner.nextLine();

        String sql = "INSERT INTO USUARIO (USERNAME, USERPASSWORD) VALUES (?, ?)";
        PreparedStatement pstmt = conexion.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.executeUpdate();

        System.out.println("Usuario insertado correctamente.");
    }

    private static void consultarUsuarios(Connection conexion) throws SQLException {
        String sql = "SELECT * FROM USUARIO";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\n=== LISTA DE USUARIOS ===");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("userid") + 
                               ", Nombre: " + rs.getString("username") + 
                               ", Contraseña: " + rs.getString("userpassword"));
        }

        rs.close();
        stmt.close();
    }

    private static void actualizarUsuario(Connection conexion, Scanner scanner) throws SQLException {
        System.out.print("Ingrese el ID del usuario a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea

        System.out.print("Ingrese el nuevo nombre de usuario: ");
        String newUsername = scanner.nextLine();
        System.out.print("Ingrese la nueva contraseña: ");
        String newPassword = scanner.nextLine();

        String sql = "UPDATE USUARIO SET USERNAME = ?, USERPASSWORD = ? WHERE USERID = ?";
        PreparedStatement pstmt = conexion.prepareStatement(sql);
        pstmt.setString(1, newUsername);
        pstmt.setString(2, newPassword);
        pstmt.setInt(3, id);
        int filasActualizadas = pstmt.executeUpdate();

        if (filasActualizadas > 0) {
            System.out.println("Usuario actualizado correctamente.");
        } else {
            System.out.println("No se encontró un usuario con ese ID.");
        }
    }

    private static void eliminarUsuario(Connection conexion, Scanner scanner) throws SQLException {
        System.out.print("Ingrese el ID del usuario a eliminar: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM USUARIO WHERE USERID = ?";
        PreparedStatement pstmt = conexion.prepareStatement(sql);
        pstmt.setInt(1, id);
        int filasEliminadas = pstmt.executeUpdate();

        if (filasEliminadas > 0) {
            System.out.println("Usuario eliminado correctamente.");
        } else {
            System.out.println("No se encontró un usuario con ese ID.");
        }
    }
}