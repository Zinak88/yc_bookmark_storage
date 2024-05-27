package org.smartup.dao;

import org.smartup.dbc.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartup.exception.ApiException;
import org.smartup.exception.BookmarkException;
import org.smartup.exception.DatabaseException;
import org.smartup.exception.ServerErrorCode;
import org.smartup.model.Folder;
import yandex.cloud.sdk.functions.Context;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FolderDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(FolderDao.class);
    private Connection connection;

    public Folder addNewFolder(Context context, Folder folder) throws ApiException{
        LOGGER.info(" # Trying to add folder: "+folder.getTitle());
        initConnection(context);

        String query = "INSERT INTO folder(title, description, parent_folder) VALUES (?, ?, ?) RETURNING folder_id, added_date;";
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
            if ( !(rs==null) && rs.next() ) {
                folder.setId(rs.getLong("folder_id"));
                LOGGER.info("# id: " + folder.getId());
                folder.setAddedDate(rs.getDate("added_date").toLocalDate());
                LOGGER.info(" # Add folder. ID: " + folder.getId() + ". Title: " + folder.getTitle());
            }
            return folder;
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            if (folder.getParentFolderId()==null) {
                throw new BookmarkException(ServerErrorCode.FAIL_EXECUTE_QUERY);
            } else {
                throw new BookmarkException(ServerErrorCode.FOLDER_ID_NOT_FOUND);
            }
        }
    }

    public Folder deleteFolder(Context context, Long folderId) throws ApiException {
        LOGGER.info(" # Trying to delete folder. Id: "+folderId);
        initConnection(context);

        String query = "DELETE FROM folder WHERE folder_id = ? RETURNING * ;";
        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, folderId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            LOGGER.info(" # ResultSet received");
            Folder folder = null;
            if ( !(rs==null) && rs.next() ) {
                folder = new Folder();
                folder.setId(rs.getLong("folder_id"));
                folder.setTitle(rs.getString("title"));
                folder.setDescription(rs.getString("description"));
                long parentFolderId = rs.getLong("parent_folder");
                folder.setParentFolderId(parentFolderId == 0 ? null : parentFolderId);
                folder.setAddedDate(rs.getDate("added_date").toLocalDate());
                LOGGER.info(" # Delete folder. ID: " + folder.getId());
            }
            return folder;
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new BookmarkException(ServerErrorCode.FAIL_EXECUTE_QUERY);
        }
    }


    public  List<Folder> getAllFolders(Context context) throws ApiException{
        LOGGER.info(" # Trying to get all folders");
        initConnection(context);
            String query = "SELECT * FROM folder;" ;
            try (var statement = connection.prepareStatement(query)) {
                List<Folder> folders = new ArrayList<>();
                statement.execute();
                ResultSet rs = statement.getResultSet();
                LOGGER.info(" # ResultSet received");
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
            } catch (SQLException e) {
                LOGGER.error(" # Fail execute query", e);
                throw new DatabaseException(ServerErrorCode.FAIL_EXECUTE_QUERY);
            }
    }
    public Folder getFolder(Context context, Long folderId) throws ApiException{
        LOGGER.info(" # Trying to get folder. ID: "+folderId);
        initConnection(context);

        String query = "SELECT * FROM folder WHERE folder_id=? ;" ;
        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, folderId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            LOGGER.info(" # ResultSet received");
            Folder folder = null;
            if ( !(rs==null) && rs.next() ) {
                folder = new Folder();
                folder.setId(rs.getLong("folder_id"));
                folder.setTitle(rs.getString("title"));
                folder.setDescription(rs.getString("description"));
                folder.setAddedDate(rs.getDate("added_date").toLocalDate());
                long parentFolderId = rs.getLong("parent_folder");
                folder.setParentFolderId(parentFolderId == 0 ? null : parentFolderId);
            }
            return folder;

        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new DatabaseException(ServerErrorCode.FAIL_EXECUTE_QUERY);
        }
    }
    public void deleteAllFolders(Context context) throws ApiException{
        LOGGER.info(" # Trying to delete all folders");
        initConnection(context);

        String query = "DELETE FROM folder;";
        try (var statement = connection.prepareStatement(query)) {
            statement.execute();
            LOGGER.info(" # All folders deleted");
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new BookmarkException(ServerErrorCode.FAIL_EXECUTE_QUERY);
        }
    }
    private void initConnection (Context context) throws DatabaseException{
        if (connection==null){
            try {
                connection = new DatabaseConnector().get(context);
            } catch (RuntimeException exc) {
                throw new DatabaseException(ServerErrorCode.FAIL_DATABASE_CONNECTION);
            }
        }
    }

}
