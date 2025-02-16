package ecomerce_pi.ecomerce.repository;

import ecomerce_pi.ecomerce.config.DatabaseConfig;
import ecomerce_pi.ecomerce.model.Usuario;
import java.sql.*;

public class UsuarioRepository {

    public static void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                     "email TEXT PRIMARY KEY, " +
                     "senha TEXT NOT NULL)";
        try (Connection conn = DatabaseConfig.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean autenticarUsuario(String email, String senha) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, senha);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Retorna true se encontrou o usuário
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void cadastrarUsuario(String email, String senha) {
        String sql = "INSERT INTO usuarios (email, senha) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, senha);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

