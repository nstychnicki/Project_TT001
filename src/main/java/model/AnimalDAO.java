
package model;

/**
 *
 * @author Naty
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static model.DAO.getConnection;

public class AnimalDAO extends DAO {
    private static AnimalDAO instance;

    private AnimalDAO() {
        getConnection();
        createTable();
    }

    // Singleton
    public static AnimalDAO getInstance() {
        return (instance==null?(instance = new AnimalDAO()):instance);
    }

// CRUD    
    public Animal create(String nome, int anoNasc, String sexo, int id_especie, int id_cliente) {
        try {
            PreparedStatement stmt;
            stmt = DAO.getConnection().prepareStatement("INSERT INTO animal (nome, anoNasc, sexo, id_especie, id_cliente) VALUES (?,?,?,?,?)");
            stmt.setString(1, nome);
            stmt.setInt(2, anoNasc);
            stmt.setString(3, sexo);
            stmt.setInt(4, id_especie);
            stmt.setInt(5, id_cliente);
            executeUpdate(stmt);
        } catch (SQLException ex) {
            Logger.getLogger(AnimalDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.retrieveById(lastId("animal","id"));
    }
    

    private Animal buildObject(ResultSet rs) {
        Animal animal = null;
        try {
            animal = new Animal(rs.getInt("id"), rs.getString("nome"), rs.getInt("anoNasc"), rs.getString("sexo"), rs.getInt("id_especie"), rs.getInt("id_cliente"));
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return animal;
    }

    // Generic Retriever
    public List retrieve(String query) {
        List<Animal> animals = new ArrayList();
        ResultSet rs = getResultSet(query);
        try {
            while (rs.next()) {
                animals.add(buildObject(rs));
            }
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return animals;
    }
    
    // RetrieveAll
    public List retrieveAll() {
        return this.retrieve("SELECT * FROM animal");
    }
    
    // RetrieveLast
    public List retrieveLast(){
        return this.retrieve("SELECT * FROM animal WHERE id = " + lastId("animal","id"));
    }

    // RetrieveById
    public Animal retrieveById(int id) {
        List<Animal> animals = this.retrieve("SELECT * FROM animal WHERE id = " + id);
        return (animals.isEmpty()?null:animals.get(0));
    }

    // RetrieveBySimilarName
    public List retrieveBySimilarName(String nome) {
        return this.retrieve("SELECT * FROM animal WHERE nome LIKE '%" + nome + "%'");
    }    
        
    // Updade
    public void update(Animal animal) {
        try {
            PreparedStatement stmt;
            stmt = DAO.getConnection().prepareStatement("UPDATE animal SET nome=?, anoNasc=?, sexo=?, id_especie=?, id_cliente=? WHERE id=?");
            stmt.setString(1, animal.getNome());
            stmt.setInt(2, animal.getAnoNasc());
            stmt.setString(3, animal.getSexo());
            stmt.setInt(4, animal.getId_especie());
            stmt.setInt(5, animal.getId_cliente());
            stmt.setInt(6, animal.getId());
            executeUpdate(stmt);
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }
    
    // Delete   
    public void delete(Animal animal) {
        PreparedStatement stmt;
        try {
            stmt = DAO.getConnection().prepareStatement("DELETE FROM animal WHERE id = ?");
            stmt.setInt(1, animal.getId());
            executeUpdate(stmt);
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

}
