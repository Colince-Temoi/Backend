package tech.csm.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import tech.csm.entity.City1;
import tech.csm.entity.State1;
import tech.csm.util.DBUtil;

public class CityDaoImpl1 implements CityDao1 {
	private static Connection con;

	public CityDaoImpl1() {
		try {
			con = DBUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String createCity(City1 city) {
		
		String msg = null;

			try {
				// do not write semicolon in-front of the } when invoking the stored procedure
				// i.e.,"{call pos_schema.p_productscreen(?, ?,?,?,?,?);}" as you may encounter
				// errors at run-time
				CallableStatement cs = con
						.prepareCall("{call city_state_storedprocedure_lobby.p_city_state_screen(?, ?, ?, ?,?, ?)}");

//				Set in parameters with values
				cs.setString(1, "insCity");
				cs.setString(2, null);
				cs.setInt(3, city.getState().getStateId());
				cs.setString(4, city.getCityName());
				cs.setInt(5, Types.NULL);

//				register out parameters
				cs.registerOutParameter(6, Types.VARCHAR);

//				Execute the things
				int count = cs.executeUpdate();

//				Access out values
//				System.out.println(count+" "+cs.getString(6));
				msg = count+" "+cs.getString(6);

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return msg;
	}

	@Override
	public List<City1> getAllCities() {
//		Create a List object to hold States data
		List<City1> cityList = new ArrayList<>();
		try {
			// do not write semicolon in-front of the } when invoking the stored procedure
			// i.e.,"{call pos_schema.p_productscreen(?, ?,?,?,?,?);}" as you may encounter
			// errors at run-time
			CallableStatement cs = con
					.prepareCall("{call city_state_storedprocedure_lobby.p_city_state_screen(?, ?, ?, ?,?, ?)}");

//			Set in parameters with values
			cs.setString(1, "selectAllCities");
			cs.setString(2, null);
			cs.setInt(3, Types.NULL);
			cs.setString(4, null);
			cs.setInt(5, Types.NULL);

//			register out parameters
			cs.registerOutParameter(6, Types.VARCHAR);

//			Execute the things
			ResultSet rs = cs.executeQuery();

//			Access out values
//			System.out.println(cs.getString(6));

			if (rs.next()) {
				do {
//					System.out.println("|| " + rs.getInt(1) + " || " + rs.getString(2) + " || ");
//					Create State object to hold State date
					City1 city = new City1();
					
//					Set the things
					city.setCityId(rs.getInt(1));
					city.setCityName(rs.getString(2));
					
					State1 state = new State1();
					state.setStateId(rs.getInt(3));
					state.setStateName(rs.getString(4));
					
					city.setState(state);
					
					
//					Add the State to a List of States
					cityList.add(city);
					
				} while (rs.next());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cityList;
	}

}
