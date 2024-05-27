package org.smartup.dao;

import org.smartup.dbc.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartup.exception.ApiException;
import org.smartup.exception.BookmarkException;
import org.smartup.exception.DatabaseException;
import org.smartup.exception.ServerErrorCode;
import org.smartup.model.Bookmark;
import yandex.cloud.sdk.functions.Context;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookmarkDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookmarkDao.class);
    private Connection connection;

    public Bookmark addNewBookmark(Context context, Bookmark bookmark) throws ApiException {
        LOGGER.info(" # Trying to add bookmark: "+bookmark.getUrl());
        initConnection(context);

        String query = "INSERT INTO bookmark(url, description, parent_folder) VALUES (?, ?, ?) RETURNING bookmark_id, added_date;";
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, bookmark.getUrl());
            statement.setString(2, bookmark.getDescription());
            if (bookmark.getParentFolderId()==null) {
                statement.setNull(3, Types.BIGINT);
            } else {
                statement.setLong(3, bookmark.getParentFolderId());
            }
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if ( !(rs==null) && rs.next() ) {
                bookmark.setId(rs.getLong("bookmark_id"));
                LOGGER.info("# id: " + bookmark.getId());
                bookmark.setAddedDate(rs.getDate("added_date").toLocalDate());
                LOGGER.info(" # Add bookmark. ID: " + bookmark.getId() + ". Url: " + bookmark.getUrl());
            }
            return bookmark;
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            if (bookmark.getParentFolderId()==null) {
                throw new BookmarkException(ServerErrorCode.FAIL_EXECUTE_QUERY);
            } else {
                throw new BookmarkException(ServerErrorCode.FOLDER_ID_NOT_FOUND);
            }

        }
    }

    public Bookmark deleteBookmark(Context context, Long bookmarkId) throws ApiException{
        LOGGER.info(" # Trying to delete bookmark. Id: " + bookmarkId);
        initConnection(context);

        String query = "DELETE FROM bookmark WHERE bookmark_id = ? RETURNING * ;";
        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, bookmarkId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            Bookmark bookmark = null;
            if ( !(rs==null) && rs.next() ) {
                bookmark = new Bookmark();
                bookmark.setId(rs.getLong("bookmark_id"));
                bookmark.setUrl(rs.getString("url"));
                bookmark.setDescription(rs.getString("description"));
                long parentFolderId = rs.getLong("parent_folder");
                bookmark.setParentFolderId(parentFolderId == 0 ? null : parentFolderId);
                bookmark.setAddedDate(rs.getDate("added_date").toLocalDate());
                LOGGER.info(" # Delete bookmark. ID: " + bookmark.getId());
            }
            return bookmark;
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new BookmarkException(ServerErrorCode.FAIL_EXECUTE_QUERY);
        }
    }

    public Bookmark getBookmark(Context context, Long bookmarkId) throws ApiException{
        LOGGER.info(" # Trying to get bookmark. ID: "+bookmarkId);
        initConnection(context);

        String query = "SELECT * FROM bookmark WHERE bookmark_id = ? ;";
        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, bookmarkId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            LOGGER.info(" # ResultSet received");
            Bookmark bookmark = null;
            if ( !(rs == null) &&  rs.next() ) {
                    bookmark = new Bookmark();
                    bookmark.setId(rs.getLong("bookmark_id"));
                    bookmark.setUrl(rs.getString("url"));
                    bookmark.setDescription(rs.getString("description"));
                    long parentFolderId = rs.getLong("parent_folder");
                    bookmark.setParentFolderId(parentFolderId == 0 ? null : parentFolderId);
                    bookmark.setAddedDate(rs.getDate("added_date").toLocalDate());
                    LOGGER.info(" # Bookmark received. ID: "+ bookmark.getId()+ ". Url: "+bookmark.getUrl());
            }
            return bookmark;
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new BookmarkException(ServerErrorCode.FAIL_EXECUTE_QUERY);
        }
    }
    public List<Bookmark> getAllBookmarks(Context context) throws ApiException{
        LOGGER.info(" # Trying to get all bookmarks");
        initConnection(context);

        String query = "SELECT * FROM bookmark;";
        List<Bookmark> bookmarks = new ArrayList<>();
        try (var statement = connection.prepareStatement(query)) {
            if (statement.execute()) {
                LOGGER.info(" # ResultSet received");
                ResultSet rs = statement.getResultSet();
                while (rs.next()) {
                    Bookmark bookmark = new Bookmark();
                    bookmark.setId(rs.getLong("bookmark_id"));
                    bookmark.setUrl(rs.getString("url"));
                    bookmark.setDescription(rs.getString("description"));
                    long parentFolderId = rs.getLong("parent_folder");
                    bookmark.setParentFolderId(parentFolderId == 0 ? null : parentFolderId);
                    bookmark.setAddedDate(rs.getDate("added_date").toLocalDate());
                    bookmarks.add(bookmark);
                }
            }
            LOGGER.info(" # Receive "+ bookmarks.size() + " bookmarks");
            return bookmarks;
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new DatabaseException(ServerErrorCode.FAIL_EXECUTE_QUERY);
        }
    }

    public void deleteAllBookmarks(Context context) throws ApiException{
        LOGGER.info(" # Trying to delete all bookmarks");
        initConnection(context);

        String query = "DELETE FROM bookmark;";
        try (var statement = connection.prepareStatement(query)) {
            statement.execute();
            LOGGER.info(" # All bookmarks deleted");
        } catch (SQLException e) {
            LOGGER.error(" # Cannot execute query", e);
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
