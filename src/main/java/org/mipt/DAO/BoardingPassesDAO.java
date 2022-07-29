package org.mipt.DAO;

import org.mipt.DTO.BoardingPass;
import org.mipt.DbConnectionFactory;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class BoardingPassesDAO {
    public Set<BoardingPass> getAllBoardingPasses() throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM boarding_passes");
            Set<BoardingPass> boardingPasses = new HashSet<>();
            while (rs.next()) {
                BoardingPass boardingPass = extractBoardingPassFromResultSet(rs);
                boardingPasses.add(boardingPass);
            }
            return boardingPasses;
        }
    }

    public boolean insertBoardingPass(BoardingPass boardingPass) throws SQLException {
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO boarding_passes VALUES (?, ?, ?, ?)")) {
            ps.setString(1, boardingPass.getTicketNo());
            ps.setInt(2, boardingPass.getFlightId());
            ps.setInt(3, boardingPass.getBoardingNo());
            ps.setString(4, boardingPass.getSeatNo());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    private BoardingPass extractBoardingPassFromResultSet(ResultSet rs) throws SQLException {
        BoardingPass boardingPass = new BoardingPass();
        boardingPass.setTicketNo(rs.getString("ticket_no"));
        boardingPass.setFlightId(rs.getInt("flight_id"));
        boardingPass.setBoardingNo(rs.getInt("boarding_no"));
        boardingPass.setSeatNo(rs.getString("seat_no"));
        return boardingPass;
    }
}
