package models.data.bid.mapper;

import exceptions.ProjectNotFound;
import exceptions.UserNotFound;
import models.data.bid.Bid;
import models.data.mapper.IMapper;

import java.sql.SQLException;

public interface IBidMapper extends IMapper<Bid, String> {
    boolean hasAlreadyBid(Bid newBid) throws SQLException;
    boolean hasBidOnProject(String projectId, String userId) throws SQLException;
    boolean isValidToAdd(Bid newBid) throws UserNotFound, ProjectNotFound;
}
