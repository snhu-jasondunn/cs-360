package com.snhu.inventoryplus

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InventoryDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL UNIQUE,
                $COLUMN_PASSWORD_HASH TEXT NOT NULL,
                $COLUMN_USER_CREATED_AT TEXT NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_INVENTORY (
                $COLUMN_ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ITEM_NAME TEXT NOT NULL,
                $COLUMN_QUANTITY INTEGER NOT NULL,
                $COLUMN_LOW_ALERT INTEGER NOT NULL,
                $COLUMN_UPDATED_AT TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INVENTORY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun createUser(username: String, password: String): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD_HASH, hashPassword(username, password))
            put(COLUMN_USER_CREATED_AT, currentDate())
        }

        return try {
            writableDatabase.insertOrThrow(TABLE_USERS, null, values) > 0
        } catch (_: SQLiteConstraintException) {
            false
        }
    }

    fun authenticateUser(username: String, password: String): Boolean {
        val cursor = readableDatabase.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD_HASH = ?",
            arrayOf(username, hashPassword(username, password)),
            null,
            null,
            null
        )

        cursor.use {
            return it.moveToFirst()
        }
    }

    fun insertInventoryItem(name: String, quantity: Int, lowAlert: Int): Long {
        val values = ContentValues().apply {
            put(COLUMN_ITEM_NAME, name)
            put(COLUMN_QUANTITY, quantity)
            put(COLUMN_LOW_ALERT, lowAlert)
            put(COLUMN_UPDATED_AT, currentDate())
        }

        return writableDatabase.insert(TABLE_INVENTORY, null, values)
    }

    fun getAllInventoryItems(): List<InventoryItem> {
        val cursor = readableDatabase.query(
            TABLE_INVENTORY,
            arrayOf(
                COLUMN_ITEM_ID,
                COLUMN_ITEM_NAME,
                COLUMN_QUANTITY,
                COLUMN_LOW_ALERT,
                COLUMN_UPDATED_AT
            ),
            null,
            null,
            null,
            null,
            "$COLUMN_ITEM_NAME COLLATE NOCASE ASC"
        )

        cursor.use {
            val items = mutableListOf<InventoryItem>()
            while (it.moveToNext()) {
                items += InventoryItem(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ITEM_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_ITEM_NAME)),
                    quantity = it.getInt(it.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                    lowAlert = it.getInt(it.getColumnIndexOrThrow(COLUMN_LOW_ALERT)),
                    updatedAt = it.getString(it.getColumnIndexOrThrow(COLUMN_UPDATED_AT))
                )
            }
            return items
        }
    }

    fun getInventoryItem(itemId: Long): InventoryItem? {
        val cursor = readableDatabase.query(
            TABLE_INVENTORY,
            arrayOf(
                COLUMN_ITEM_ID,
                COLUMN_ITEM_NAME,
                COLUMN_QUANTITY,
                COLUMN_LOW_ALERT,
                COLUMN_UPDATED_AT
            ),
            "$COLUMN_ITEM_ID = ?",
            arrayOf(itemId.toString()),
            null,
            null,
            null
        )

        cursor.use {
            if (!it.moveToFirst()) {
                return null
            }

            return InventoryItem(
                id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ITEM_ID)),
                name = it.getString(it.getColumnIndexOrThrow(COLUMN_ITEM_NAME)),
                quantity = it.getInt(it.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                lowAlert = it.getInt(it.getColumnIndexOrThrow(COLUMN_LOW_ALERT)),
                updatedAt = it.getString(it.getColumnIndexOrThrow(COLUMN_UPDATED_AT))
            )
        }
    }

    fun updateInventoryItem(itemId: Long, name: String, quantity: Int, lowAlert: Int): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_ITEM_NAME, name)
            put(COLUMN_QUANTITY, quantity)
            put(COLUMN_LOW_ALERT, lowAlert)
            put(COLUMN_UPDATED_AT, currentDate())
        }

        return writableDatabase.update(
            TABLE_INVENTORY,
            values,
            "$COLUMN_ITEM_ID = ?",
            arrayOf(itemId.toString())
        ) > 0
    }

    fun deleteInventoryItem(itemId: Long): Boolean {
        return writableDatabase.delete(
            TABLE_INVENTORY,
            "$COLUMN_ITEM_ID = ?",
            arrayOf(itemId.toString())
        ) > 0
    }

    private fun currentDate(): String {
        return SimpleDateFormat("MM/dd/yy", Locale.US).format(Date())
    }

    private fun hashPassword(username: String, password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest("$username:$password".toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val DATABASE_NAME = "inventory_plus.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD_HASH = "password_hash"
        private const val COLUMN_USER_CREATED_AT = "created_at"

        private const val TABLE_INVENTORY = "inventory_items"
        private const val COLUMN_ITEM_ID = "id"
        private const val COLUMN_ITEM_NAME = "item_name"
        private const val COLUMN_QUANTITY = "quantity"
        private const val COLUMN_LOW_ALERT = "low_alert"
        private const val COLUMN_UPDATED_AT = "updated_at"
    }
}
