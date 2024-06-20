package org.smartup.dao;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartup.exception.FolderErrorCode;
import org.smartup.exception.FolderException;
import org.smartup.model.Folder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FolderDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderDao.class);

    public Folder addNewFolder(Connection connection, Folder folder) throws FolderException {
        LOGGER.info(" # Trying to add folder: "+folder.getTitle());

        String query = "INSERT INTO folder(title, description, parent_folder) VALUES (?, ?, ?) RETURNING *;";
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, folder.getTitle());
            statement.setString(2, folder.getDescription());
            if (folder.getParentFolderId() == null) {
                statement.setNull(3, Types.BIGINT);
            } else {
                statement.setLong(3, folder.getParentFolderId());
            }
            statement.execute();
            ResultSet rs = statement.getResultSet();
            LOGGER.info(" # ResultSet received");
            List<Folder> folders = getFoldersFromResultSet(rs);
            return folders.isEmpty() ? null : folders.get(0);
        } catch (PSQLException e) {
            LOGGER.error("# Fail execute query "+ e.getServerErrorMessage());
            if (e.getSQLState().equals("23503")) {
                throw new FolderException(FolderErrorCode.FOLDER_ID_NOT_FOUND);
            }
            throw new FolderException(FolderErrorCode.FAIL_EXECUTE_QUERY);
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new FolderException(FolderErrorCode.FAIL_EXECUTE_QUERY);
        }
    }

    public Folder deleteFolder(Connection connection, Long folderId) throws FolderException {
        LOGGER.info(" # Trying to delete folder. Id: "+folderId);

        String query = "DELETE FROM folder WHERE folder_id = ? RETURNING * ;";
        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, folderId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            LOGGER.info(" # ResultSet received");
            List<Folder> folders = getFoldersFromResultSet(rs);
            return folders.isEmpty() ? null : folders.get(0);
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new FolderException(FolderErrorCode.FAIL_EXECUTE_QUERY);
        }
    }


    public  List<Folder> getAllFolders(Connection connection) throws FolderException{
        LOGGER.info(" # Trying to get all folders");
            String query = "SELECT * FROM folder;" ;
            try (var statement = connection.prepareStatement(query)) {
                statement.execute();
                ResultSet rs = statement.getResultSet();
                LOGGER.info(" # ResultSet received");
                return getFoldersFromResultSet(rs);
            } catch (SQLException e) {
                LOGGER.error(" # Fail execute query", e);
                throw new FolderException(FolderErrorCode.FAIL_EXECUTE_QUERY);
            }
    }
    public Folder getFolder(Connection connection, Long folderId) throws FolderException{
        LOGGER.info(" # Trying to get folder. ID: "+folderId);

        String query = "SELECT * FROM folder WHERE folder_id=? ;" ;
        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, folderId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            LOGGER.info(" # ResultSet received");
            List<Folder> folders = getFoldersFromResultSet(rs);
            return folders.isEmpty() ? null : folders.get(0);

        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new FolderException(FolderErrorCode.FAIL_EXECUTE_QUERY);
        }
    }
    public void deleteAllFolders(Connection connection) throws FolderException{
        LOGGER.info(" # Trying to delete all folders");

        String query = "DELETE FROM folder;";
        try (var statement = connection.prepareStatement(query)) {
            statement.execute();
            LOGGER.info(" # All folders deleted");
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new FolderException(FolderErrorCode.FAIL_EXECUTE_QUERY);
        }
    }

    private List<Folder> getFoldersFromResultSet (ResultSet rs) throws SQLException{
        List<Folder> folders = new ArrayList<>();
        if (!(rs == null)) {
            while (rs.next()) {
                Folder folder = new Folder();
                folder.setId(rs.getLong("folder_id"));
                folder.setTitle(rs.getString("title"));
                folder.setDescription(rs.getString("description"));
                folder.setAddedDate(rs.getDate("added_date").toLocalDate());
                long parentFolderId = rs.getLong("parent_folder");
                folder.setParentFolderId(parentFolderId == 0 ? null : parentFolderId);
                folders.add(folder);
            }
        }
        return folders;
    }

}
