package com.epf.rentmanager.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;

public class ClientDao {
	
	private static ClientDao instance = null;
	private ClientDao() {}
	public static ClientDao getInstance() {
		if(instance == null) {
			instance = new ClientDao();
		}
		return instance;
	}
	
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String COUNT_CLIENTS_QUERY = "SELECT COUNT(id) AS count FROM Client;";
	
	public long create(Client client) throws DaoException {

		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement statement = connection.prepareStatement(CREATE_CLIENT_QUERY,Statement.RETURN_GENERATED_KEYS);

			statement.setString(1,client.getNom());
			statement.setString(2,client.getPrenom());
			statement.setString(3,client.getEmail());
			statement.setDate(4, Date.valueOf(client.getNaissance()));
			statement.execute();

			ResultSet resultSet = statement.getGeneratedKeys();

			if (resultSet.next()) {int id = resultSet.getInt(1); return id;}

		}catch (SQLException e){
			e.printStackTrace();
			throw new DaoException();
		}
		return 0;
	}
	
	public long delete(Client client) throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement statement = connection.prepareStatement(DELETE_CLIENT_QUERY);
			statement.setLong(1, client.getClient_id());
			statement.executeUpdate();


		}catch (SQLException e){
			e.printStackTrace();
			throw new DaoException();
		}

		return 0;

	}

	public Client findById(long id) throws DaoException {
		Client client=null;
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement statement = connection.prepareStatement(FIND_CLIENT_QUERY);
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();

while(rs.next()){
			String nom = rs.getString("nom");
			String prenom = rs.getString("prenom");
			String email = rs.getString("email");
			LocalDate naissance = rs.getDate("naissance").toLocalDate();

			client = new Client(id, nom, prenom, email, naissance);

		}

		}catch (SQLException e){
			e.printStackTrace();
			throw new DaoException();
		}

		return client;
	}

	public List<Client> findAll() throws DaoException {

		List<Client>clients= new ArrayList<>();
		try{
			Connection connection = ConnectionManager.getConnection();
			Statement statement= connection.createStatement();

			ResultSet rs= statement.executeQuery(FIND_CLIENTS_QUERY);

			while (rs.next()){
				int id= rs.getInt("id");
				String nom=rs.getString("nom");
				String prenom=rs.getString("prenom");
				String email=rs.getString("email");
				LocalDate naissance=rs.getDate("naissance").toLocalDate();

				clients.add(new Client(id,nom,prenom,email,naissance));


			}
		}catch (SQLException e){
			e.printStackTrace();
			throw new DaoException();
		}

		return clients;



	}

	public static int CountClient(){
		int n = 0;
		try {

			Connection connection = ConnectionManager.getConnection();
			PreparedStatement statement = connection.prepareStatement(COUNT_CLIENTS_QUERY);

			ResultSet rs = statement.executeQuery();
			rs.next();
			n = rs.getInt("count");

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return n;

	}

}
