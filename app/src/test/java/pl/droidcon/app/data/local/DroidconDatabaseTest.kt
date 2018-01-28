package pl.droidcon.app.data.local

import android.arch.core.executor.ArchTaskExecutor
import android.arch.core.executor.TaskExecutor
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.db.SupportSQLiteStatement
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteTransactionListener
import android.os.CancellationSignal
import android.util.Pair
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.sqlite.SQLiteDataSource
import java.io.File
import java.util.*


@RunWith(JUnit4::class)
class DroidconDatabaseTest {

    @Rule
    val folder = TemporaryFolder()

    private val context: Context = mock()
    private val speaker: SpeakerLocal = mock()


    @Test
    fun dbTesting() {


        ArchTaskExecutor.getInstance().setDelegate(TestTest())

        whenever(context.getDatabasePath("droidcon")).thenReturn(File("abc"))

        DroidconDatabase.initInMemory(context, JVMFactory(folder))

        DroidconDatabase.get().speakerDao().put(arrayOf(speaker).toList())

    }

    class JVMFactory(val folder: TemporaryFolder) : SupportSQLiteOpenHelper.Factory {
        override fun create(configuration: SupportSQLiteOpenHelper.Configuration): SupportSQLiteOpenHelper {
            return JvmSQLiteOpenHelper(folder)
        }
    }

    class JvmSQLiteOpenHelper(folder: TemporaryFolder) : SupportSQLiteOpenHelper {

        val dataSource: SQLiteDataSource

        init {
            dataSource = SQLiteDataSource()
            dataSource.setUrl("jdbc:sqlite:" + folder.newFile().absolutePath);

            dataSource.connection.
        }

        override fun getDatabaseName(): String {
            return dataSource.databaseName
        }

        override fun getWritableDatabase(): SupportSQLiteDatabase {

        }

        override fun getReadableDatabase(): SupportSQLiteDatabase {

        }

        override fun close() {

        }

        override fun setWriteAheadLoggingEnabled(enabled: Boolean) {

        }
    }

    class JvmSQLiteDatabase: SupportSQLiteDatabase {
        override fun setMaximumSize(numBytes: Long): Long {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun insert(table: String?, conflictAlgorithm: Int, values: ContentValues?): Long {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun enableWriteAheadLogging(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isDatabaseIntegrityOk(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isWriteAheadLoggingEnabled(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun disableWriteAheadLogging() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun compileStatement(sql: String?): SupportSQLiteStatement {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun beginTransactionWithListenerNonExclusive(transactionListener: SQLiteTransactionListener?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isDbLockedByCurrentThread(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun setPageSize(numBytes: Long) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun query(query: String?): Cursor {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun query(query: String?, bindArgs: Array<out Any>?): Cursor {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun query(query: SupportSQLiteQuery?): Cursor {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun query(query: SupportSQLiteQuery?, cancellationSignal: CancellationSignal?): Cursor {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun endTransaction() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getMaximumSize(): Long {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun setLocale(locale: Locale?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun beginTransaction() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun update(table: String?, conflictAlgorithm: Int, values: ContentValues?, whereClause: String?, whereArgs: Array<out Any>?): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isOpen(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getAttachedDbs(): MutableList<Pair<String, String>> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getVersion(): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun execSQL(sql: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun execSQL(sql: String?, bindArgs: Array<out Any>?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun yieldIfContendedSafely(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun yieldIfContendedSafely(sleepAfterYieldDelay: Long): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun close() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun delete(table: String?, whereClause: String?, whereArgs: Array<out Any>?): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun needUpgrade(newVersion: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun setMaxSqlCacheSize(cacheSize: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun setForeignKeyConstraintsEnabled(enable: Boolean) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun beginTransactionNonExclusive() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun setTransactionSuccessful() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun setVersion(version: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun beginTransactionWithListener(transactionListener: SQLiteTransactionListener?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun inTransaction(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isReadOnly(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getPath(): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getPageSize(): Long {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    class TestTest : TaskExecutor() {
        override fun executeOnDiskIO(runnable: Runnable?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun executeOnMainThread(runnable: Runnable?) {
            super.executeOnMainThread(runnable)
        }

        override fun isMainThread(): Boolean {
            return false
        }

        override fun postToMainThread(runnable: Runnable?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}