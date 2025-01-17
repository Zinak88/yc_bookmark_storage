package org.smartup.dao;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartup.exception.*;
import org.smartup.model.Bookmark;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookmarkDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookmarkDao.class);
    public Bookmark addNewBookmark(Connection connection, Bookmark bookmark) throws BookmarkException {
        LOGGER.info(" # Trying to add bookmark: " + bookmark.getUrl());

        String query = "INSERT INTO bookmark(url, description, parent_folder) VALUES (?, ?, ?) RETURNING *;";
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, bookmark.getUrl());
            statement.setString(2, bookmark.getDescription());
            if (bookmark.getParentFolderId() == null) {
                statement.setNull(3, Types.BIGINT);
            } else {
                statement.setLong(3, bookmark.getParentFolderId());
            }
            statement.execute();
            ResultSet rs = statement.getResultSet();
            List<Bookmark> bookmarks = getBookmarksFromResultSet(rs);
            return bookmarks.isEmpty() ? null : bookmarks.get(0);
        } catch (PSQLException e) {
            LOGGER.error("# Fail execute query "+ e.getServerErrorMessage());
            if (e.getSQLState().equals("23503")) {
                LOGGER.error("# SQL State 23503: foreign key violation");
                throw new BookmarkException(BookmarkErrorCode.FOLDER_ID_NOT_FOUND);
            }
            LOGGER.error("# SQL State: " + e.getSQLState());
            throw new BookmarkException(BookmarkErrorCode.FAIL_EXECUTE_QUERY);
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query ", e);
            throw new BookmarkException(BookmarkErrorCode.FAIL_EXECUTE_QUERY);
        }
    }

    public Bookmark deleteBookmark(Connection connection, Long bookmarkId) throws BookmarkException{
        LOGGER.info(" # Trying to delete bookmark. Id: " + bookmarkId);

        String query = "DELETE FROM bookmark WHERE bookmark_id = ? RETURNING * ;";
        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, bookmarkId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            List<Bookmark> bookmarks = getBookmarksFromResultSet(rs);
            return bookmarks.isEmpty() ? null : bookmarks.get(0);
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new BookmarkException(BookmarkErrorCode.FAIL_EXECUTE_QUERY);
        }
    }

    public Bookmark getBookmark(Connection connection, Long bookmarkId) throws BookmarkException{
        LOGGER.info(" # Trying to get bookmark. ID: "+bookmarkId);

        String query = "SELECT * FROM bookmark WHERE bookmark_id = ? ;";
        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, bookmarkId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            LOGGER.info(" # ResultSet received");
            List<Bookmark> bookmarks = getBookmarksFromResultSet(rs);
            return bookmarks.isEmpty() ? null : bookmarks.get(0);
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new BookmarkException(BookmarkErrorCode.FAIL_EXECUTE_QUERY);
        }
    }
    public List<Bookmark> getAllBookmarks(Connection connection) throws BookmarkException{
        LOGGER.info(" # Trying to get all bookmarks");

        String query = "SELECT * FROM bookmark;";
        try (var statement = connection.prepareStatement(query)) {
            statement.execute();
            ResultSet rs = statement.getResultSet();
            LOGGER.info(" # ResultSet received");
            return getBookmarksFromResultSet(rs);
        } catch (SQLException e) {
            LOGGER.error(" # Fail execute query", e);
            throw new BookmarkException(BookmarkErrorCode.FAIL_EXECUTE_QUERY);
        }
    }

    public void deleteAllBookmarks(Connection connection) throws BookmarkException{
        LOGGER.info(" # Trying to delete all bookmarks");

        String query = "DELETE FROM bookmark;";
        try (var statement = connection.prepareStatement(query)) {
            statement.execute();
            LOGGER.info(" # All bookmarks deleted");
        } catch (SQLException e) {
            LOGGER.error(" # Cannot execute query", e);
            throw new BookmarkException(BookmarkErrorCode.FAIL_EXECUTE_QUERY);
        }
    }

    private List<Bookmark> getBookmarksFromResultSet (ResultSet rs) throws SQLException {
        List<Bookmark> bookmarks = new ArrayList<>();
        if (!(rs == null)) {
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
        return bookmarks;
    }

}
