package com.example.deleguesapp.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;

import com.example.deleguesapp.model.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table USER.
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Forname = new Property(2, String.class, "forname", false, "FORNAME");
        public final static Property Phone = new Property(3, String.class, "phone", false, "PHONE");
        public final static Property Email = new Property(4, String.class, "email", false, "EMAIL");
        public final static Property MoyG = new Property(5, Float.class, "moyG", false, "MOY_G");
        public final static Property Appreciation = new Property(6, String.class, "appreciation", false, "APPRECIATION");
        public final static Property Remarques = new Property(7, String.class, "remarques", false, "REMARQUES");
        public final static Property Etat = new Property(8, Integer.class, "etat", false, "ETAT");
    };


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'USER' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'FORNAME' TEXT," + // 2: forname
                "'PHONE' TEXT," + // 3: phone
                "'EMAIL' TEXT," + // 4: email
                "'MOY_G' REAL," + // 5: moyG
                "'APPRECIATION' TEXT," + // 6: appreciation
                "'REMARQUES' TEXT," + // 7: remarques
                "'ETAT' INTEGER);"); // 8: etat
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'USER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String forname = entity.getForname();
        if (forname != null) {
            stmt.bindString(3, forname);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(4, phone);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(5, email);
        }
 
        Float moyG = entity.getMoyG();
        if (moyG != null) {
            stmt.bindDouble(6, moyG);
        }
 
        String appreciation = entity.getAppreciation();
        if (appreciation != null) {
            stmt.bindString(7, appreciation);
        }
 
        String remarques = entity.getRemarques();
        if (remarques != null) {
            stmt.bindString(8, remarques);
        }
 
        Integer etat = entity.getEtat();
        if (etat != null) {
            stmt.bindLong(9, etat);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // forname
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // phone
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // email
            cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5), // moyG
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // appreciation
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // remarques
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8) // etat
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setForname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPhone(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEmail(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMoyG(cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5));
        entity.setAppreciation(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRemarques(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setEtat(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
