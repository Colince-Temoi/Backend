package tech.csm.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tech.csm.domain.AdmissionDetails;
import tech.csm.domain.Collage;
import tech.csm.util.DbUtil;

public class AdmissionDaoImpl implements AdmissionDao {
//	Primitive dependencies
	private Integer count;
	private String message;

//	Secondary dependencies
	private Connection con;

	public AdmissionDaoImpl() {
		con = DbUtil.getConnection();
	}

	@Override
	public String saveAdmissionDetails(AdmissionDetails admissionDetails) {

//		Prepare the 2 query strings
		final String insertQuery = "INSERT INTO collage_admission_crud_schema.t_admission_details (candidate_name, candidate_phone, candidate_address,collage_id,admission_date) VALUES (?, ?, ?,?,?);";
		final String updateQuery = "UPDATE collage_admission_crud_schema.t_collage_master SET no_of_seats =no_of_seats-1 WHERE collage_id = ?";

		try {

			PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

//			DML - Insert record(s)

			Integer numOfInsertedRecords = 0;

//				1. Set the values
			ps.setString(1, admissionDetails.getCandidateName());
			ps.setString(2, admissionDetails.getCandidatePhone());
			ps.setString(3, admissionDetails.getCandidateAddress());
			ps.setInt(4, admissionDetails.getCollage().getCollageId());
			ps.setDate(5, new Date(admissionDetails.getAdmission_date().getTime()));

//				2. Fire the executeUpdate method of PS
			count = ps.executeUpdate();
			numOfInsertedRecords += count;

//				Get the generated key after the insert is successful
			ResultSet key = ps.getGeneratedKeys();

			key.next();
			message = "\nCandidate admitted sucessfully with the id " + key.getInt(1);

			try {
				ps = con.prepareStatement(updateQuery);
				ps.setInt(1, admissionDetails.getCollage().getCollageId());

				int count = ps.executeUpdate();
				if (count > 0) {
					message += "\nCollage table also updated successfully.";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			key.close();
			ps.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Something went wrong!");
			e.printStackTrace();
		}
		return message;
	}



	@Override
	public String modifyAdmissionById(int admissionId, AdmissionDetails modifiedAdmission) {
	    final String updateQuery = "UPDATE collage_admission_crud_schema.t_admission_details SET candidate_name = ?, candidate_phone = ?, candidate_address = ? WHERE admission_id = ?";

	    try {
	        PreparedStatement ps = con.prepareStatement(updateQuery);
	        ps.setString(1, modifiedAdmission.getCandidateName());
	        ps.setString(2, modifiedAdmission.getCandidatePhone());
	        ps.setString(3, modifiedAdmission.getCandidateAddress());
	        ps.setInt(4, admissionId);

	        int updatedRows = ps.executeUpdate();

	        if (updatedRows > 0) {
	            return "Admission details updated successfully.";
	        } else {
	            return "Error updating admission details with ID: " + admissionId;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error updating admission details.";
	    }
	}


	@Override
	public List<AdmissionDetails> getAdmissionDetails() {
	    final String selectQuery = "SELECT * FROM collage_admission_crud_schema.t_admission_details";

	    List<AdmissionDetails> admissionDetailsList = new ArrayList<>();

	    try (PreparedStatement ps = con.prepareStatement(selectQuery);
	         ResultSet resultSet = ps.executeQuery()) {

	        while (resultSet.next()) {
	            AdmissionDetails admissionDetails = new AdmissionDetails();

	            admissionDetails.setAdmissionId(resultSet.getInt("admission_id"));
	            admissionDetails.setCandidateName(resultSet.getString("candidate_name"));
	            admissionDetails.setCandidatePhone(resultSet.getString("candidate_phone"));
	            admissionDetails.setCandidateAddress(resultSet.getString("candidate_address"));

	            // Handle the date conversion from java.sql.Date to java.util.Date
	            java.sql.Date sqlDate = resultSet.getDate("admission_date");
	            if (sqlDate != null) {
	                admissionDetails.setAdmission_date(new java.util.Date(sqlDate.getTime()));
	            }

	            // Create Collage object and set its properties
	            Collage collage = new Collage();
	            collage.setCollageId(resultSet.getInt("collage_id"));

	            admissionDetails.setCollage(collage);

	            admissionDetailsList.add(admissionDetails);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle the exception appropriately, log or throw a custom exception
	    }

	    return admissionDetailsList;
	}

	@Override
	public String cancelAdmissionById(int admissionId) {
		// TODO Auto-generated method stub
		return null;
	}






}
