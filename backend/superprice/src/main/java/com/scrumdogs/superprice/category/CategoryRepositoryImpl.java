package com.scrumdogs.superprice.category;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final DataSource source;

    @Override
    public Optional<List<Category>> findFullCategoryTree() {
        Connection connection;
        try {
            connection = this.source.getConnection();
            PreparedStatement stm = getFullCategoryTreeSQLStatement(connection);
            List<Category> categories = new ArrayList<>();
            ResultSet rs = stm.executeQuery();

            Map<Long, Category> categoryMap = new HashMap<>();

            if (!rs.next()) {
                return Optional.empty();
            } else {
                do {
                    Long id = rs.getLong("ID");
                    String name = rs.getString("Name");
                    Long parentId = rs.getLong("ParentCategoryId");
                    Category category = new Category(id, name, parentId, new ArrayList<>());

                    // Add the category to the map
                    categoryMap.put(id, category);

                    // If it's not the root category, add it as a child to its parent
                    if (parentId != 0) {
                        Category parentCategory = categoryMap.get(parentId);
                        if (parentCategory != null) {
                            parentCategory.children().add(category);
                        }
                    }

                    // If it's the root category, add it to the result list
                    else {
                        categories.add(category);
                    }
                } while (rs.next());
            }
            connection.close();
            return Optional.of(categories);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findCategoryTreeFromParent", e);
        }
    }

    @Override
    public Optional<List<Category>> findCategoryTreeFromParent(Long parentCategoryID) {
        if (parentCategoryID == null) return findFullCategoryTree();

        Connection connection;

        try {
            connection = this.source.getConnection();
            PreparedStatement stm = getFindCategoryTreeFromParentSQLStatement(connection, parentCategoryID);
            List<Category> categories = new ArrayList<>();
            ResultSet rs = stm.executeQuery();

            Map<Long, Category> categoryMap = new HashMap<>();

            if (!rs.next()) {
                return Optional.empty();
            } else {
                //  we use a do loop here because we need to check rs.next() to exit early
                //  if there are no results, but doing so advances the ResultSet cursor...
                //  so using a while loop would skip the first result.
                //  I don't think checking if rs == null works.
                do {
                    Long id = rs.getLong("ID");
                    String name = rs.getString("Name");
                    Long parentId = rs.getLong("ParentCategoryId");
                    Category category = new Category(id, name, parentId, new ArrayList<>());

                    // Add the category to the map
                    categoryMap.put(id, category);

                    // If it's not the root category, add it as a child to its parent
                    if (!id.equals(parentCategoryID)) {
                        System.out.println("add as child: " + name + "(" + id + ") to " + parentCategoryID);
                        Category parentCategory = categoryMap.get(parentId);
                        if (parentCategory != null) {
                            parentCategory.children().add(category);
                        }
                    }
                    // If it's the root category, add it to the result list
                    else {
                        System.out.println("add as root: " + name + "(" + id + ") to " + parentCategoryID);
                        categories.add(category);
                    }
                } while (rs.next());
            }
            connection.close();
            return Optional.of(categories);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error in findCategoryTreeFromParent", e);
        }
    }

    private static PreparedStatement getFullCategoryTreeSQLStatement(Connection connection) {
        String sql = """
            WITH RECURSIVE category_hierarchy (ID, Name, ParentCategoryID) AS (
              SELECT ID,
                        Name,
                        ParentCategoryID
              FROM Category
              WHERE ParentCategoryID IS NULL
              UNION ALL
            SELECT
                c.ID,
                c.Name,
                c.ParentCategoryID
              FROM Category c, category_hierarchy
              WHERE c.ParentCategoryID = category_hierarchy.ID
            )
            SELECT * FROM category_hierarchy ORDER BY ParentCategoryID;
        """;

        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error producing SQL statement for findChange", e);
        }
        return stm;
    }

    private static PreparedStatement getFindCategoryTreeFromParentSQLStatement(Connection connection, Long parentCategoryID) {
        String sql = """
            WITH RECURSIVE category_hierarchy (ID, Name, ParentCategoryID) AS (
                SELECT ID,
                       Name,
                       ParentCategoryID
                FROM Category
                WHERE ParentCategoryID = ?
                UNION ALL
                SELECT
                    c.ID,
                    c.Name,
                    c.ParentCategoryID
                FROM Category c, category_hierarchy
                WHERE c.ParentCategoryID = category_hierarchy.ID
            )
            SELECT ID, Name, ParentCategoryID
            FROM Category
            WHERE ID = ?
            UNION ALL
            SELECT * FROM category_hierarchy ORDER BY ParentCategoryID;
        """;

        PreparedStatement stm;
        try {
            stm = connection.prepareStatement(sql);
            stm.setLong(1, parentCategoryID);
            stm.setLong(2, parentCategoryID);
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error producing SQL statement for findCategoryTreeFromParent", e);
        }
        return stm;
    }
}