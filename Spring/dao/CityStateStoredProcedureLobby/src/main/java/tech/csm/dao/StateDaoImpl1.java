package tech.csm.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import tech.csm.entity.State1;
import tech.csm.util.DBUtil;

public class StateDaoImpl1 implements StateDao1 {

	private static Connection con;

	public StateDaoImpl1() {
		try {
			con = DBUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<State1> getAllStates() {
//		Create a List object to hold States data
		List<State1> stateList = new ArrayList<>();
		try {
			// do not write semicolon in-front of the } when invoking the stored procedure
			// i.e.,"{call pos_schema.p_productscreen(?, ?,?,?,?,?);}" as you may encounter
			// errors at run-time
			CallableStatement cs = con
					.prepareCall("{call city_state_storedprocedure_lobby.p_city_state_screen(?, ?, ?, ?,?, ?)}");

//			Set in parameters with values
			cs.setString(1, "getAllStates");
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
					State1 state = new State1();
					
//					Set the things
					state.setStateId(rs.getInt(1));
					state.setStateName(rs.getString(2));
					
//					Add the State to a List of States
					stateList.add(state);
					
				} while (rs.next());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stateList;
	}

	@Override
	public State1 getStateById(Integer id) {
//		Create a List object to hold States data
		State1 state = null;
		try {
			// do not write semicolon in-front of the } when invoking the stored procedure
			// i.e.,"{call pos_schema.p_productscreen(?, ?,?,?,?,?);}" as you may encounter
			// errors at run-time
			CallableStatement cs = con
					.prepareCall("{call city_state_storedprocedure_lobby.p_city_state_screen(?, ?, ?, ?,?, ?)}");

//			Set in parameters with values
			cs.setString(1, "getStateById");
			cs.setString(2, null);
			cs.setInt(3, id);
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
					state = new State1();
					
//					Set the things
					state.setStateId(rs.getInt(1));
					state.setStateName(rs.getString(2));
					
				} while (rs.next());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}

}
