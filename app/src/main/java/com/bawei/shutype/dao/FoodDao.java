package com.bawei.shutype.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FOOD".
*/
public class FoodDao extends AbstractDao<Food, Long> {

    public static final String TABLENAME = "FOOD";

    /**
     * Properties of entity Food.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Img = new Property(2, String.class, "img", false, "IMG");
        public final static Property Price = new Property(3, int.class, "price", false, "PRICE");
        public final static Property Checked = new Property(4, boolean.class, "checked", false, "CHECKED");
        public final static Property Num = new Property(5, int.class, "num", false, "NUM");
    }


    public FoodDao(DaoConfig config) {
        super(config);
    }
    
    public FoodDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FOOD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"IMG\" TEXT," + // 2: img
                "\"PRICE\" INTEGER NOT NULL ," + // 3: price
                "\"CHECKED\" INTEGER NOT NULL ," + // 4: checked
                "\"NUM\" INTEGER NOT NULL );"); // 5: num
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FOOD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Food entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String img = entity.getImg();
        if (img != null) {
            stmt.bindString(3, img);
        }
        stmt.bindLong(4, entity.getPrice());
        stmt.bindLong(5, entity.getChecked() ? 1L: 0L);
        stmt.bindLong(6, entity.getNum());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Food entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String img = entity.getImg();
        if (img != null) {
            stmt.bindString(3, img);
        }
        stmt.bindLong(4, entity.getPrice());
        stmt.bindLong(5, entity.getChecked() ? 1L: 0L);
        stmt.bindLong(6, entity.getNum());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Food readEntity(Cursor cursor, int offset) {
        Food entity = new Food( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // img
            cursor.getInt(offset + 3), // price
            cursor.getShort(offset + 4) != 0, // checked
            cursor.getInt(offset + 5) // num
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Food entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setImg(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPrice(cursor.getInt(offset + 3));
        entity.setChecked(cursor.getShort(offset + 4) != 0);
        entity.setNum(cursor.getInt(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Food entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Food entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Food entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
