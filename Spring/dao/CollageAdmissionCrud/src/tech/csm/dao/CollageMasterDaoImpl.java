package tech.csm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tech.csm.domain.Collage;
import tech.csm.util.DbUtil;

public class CollageMasterDaoImpl implements CollageMasterDao {

//	Primitive dependencies

//	Secondary dependencies
	private Connection con;

	public CollageMasterDaoImpl() {
		con = DbUtil.getConnection();
	}

	@Override
	public List<Collage> getAllCollages() {
//		Logic to get all collages
		
//		Query String
		final String selectAllQuery = "SELECT collage_id,collage_name,collage_address,no_of_seats FROM collage_admission_crud_schema.t_collage_master;";
		
		List<Collage> collages=null;

//		PreparedStatement
		try {
			PreparedStatement ps = con.prepareStatement(selectAllQuery);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
//				Prepare a place to store the received list of College details
				collages = new ArrayList<>();
				do {
//					Prepare College object to receive each College details.
					Collage collage = new Collage();

					collage.setCollageId(resultSet.getInt(1));
					collage.setCollageAddress(resultSet.getString(3));
					collage.setCollageName(resultSet.getString(2));
					collage.setNoOfSeats(resultSet.getInt(4));

					collages.add(collage);

				} while (resultSet.next());
			}
//			No need to explicitly write the else part here.

			ps.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return collages;
	}

	@Override
	public Collage getCollageById(Integer id) {
	    // Query String
	    final String selectByIdQuery = "SELECT collage_id, collage_name, collage_address, no_of_seats " +
	                                   "FROM collage_admission_crud_schema.t_collage_master " +
	                                   "WHERE collage_id = ?;";

	    Collage collage = null;

	    // PreparedStatement
	    try {
	        PreparedStatement ps = con.prepareStatement(selectByIdQuery);
	        ps.setInt(1, id);

	        ResultSet resultSet = ps.executeQuery();

	        if (resultSet.next()) {
	            collage = new Collage();
	            collage.setCollageId(resultSet.getInt(1));
	            collage.setCollageAddress(resultSet.getString(3));
	            collage.setCollageName(resultSet.getString(2));
	            collage.setNoOfSeats(resultSet.getInt(4));
	        }

	        ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return collage;
	}

}
