package com.mb.kibeti.sms_filter

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE timestamp >= :sixMonthsAgo ORDER BY timestamp DESC")
    fun getRecentTransactions(sixMonthsAgo: Long): LiveData<List<TransactionEntity>>

    @Query("""
        SELECT recipient, SUM(amount) AS totalAmount 
        FROM transactions 
        WHERE timestamp >= :sixMonthsAgo 
        GROUP BY recipient 
        ORDER BY totalAmount DESC 
        LIMIT 6
    """)
    fun getTopRecipients(sixMonthsAgo: Long): LiveData<List<RecipientSummary>>

    @Query("SELECT smsId FROM transactions WHERE smsId IS NOT NULL")
    suspend fun getAllProcessedSmsIds(): List<Long>

    @Query("SELECT COUNT(*) FROM transactions WHERE smsId = :smsId")
    suspend fun isSmsProcessed(smsId: Long): Int

    @Query("DELETE FROM transactions")
    suspend fun clearTransactions()

    @Query("SELECT SUM(amount) FROM transactions WHERE timestamp >= :sixMonthsAgo")
    suspend fun getTotalSpending(sixMonthsAgo: Long): Double

    @Query("SELECT * FROM transactions WHERE timestamp >= :sixMonthsAgo ORDER BY timestamp DESC LIMIT 10")
    fun getLast10Transactions(sixMonthsAgo: Long): LiveData<List<TransactionEntity>>

    @Query("""
        WITH supermarket_patterns AS (
            SELECT '%Nakumatt%' AS pattern UNION ALL
            SELECT '%Naivas%' UNION ALL
            SELECT '%Carrefour%' UNION ALL
            SELECT '%Chandarana%' UNION ALL
            SELECT '%Quickmart%' UNION ALL
            SELECT '%Cleanshelf%' UNION ALL
            SELECT '%Magunas%' UNION ALL
            SELECT '%Uchumi%' UNION ALL
            SELECT '%Tuskys%' UNION ALL
            SELECT '%Eastmatt%' UNION ALL
            SELECT '%Shoprite%' UNION ALL
            SELECT '%Game%' UNION ALL
            SELECT '%Tumaini%' UNION ALL
            SELECT '%Choppies%' UNION ALL
            SELECT '%Zuch%' UNION ALL
            SELECT '%Mulleys%' UNION ALL
            SELECT '%Khetias%' UNION ALL
            SELECT '%Galitos%' UNION ALL
            SELECT '%Foodplus%' UNION ALL
            SELECT '%PURPLEMART%' UNION ALL
            SELECT '%PURPLE MART%' UNION ALL
            SELECT '%KWA MUKHWANA%' UNION ALL
            SELECT '%KWAMUKHWANA%' UNION ALL
            SELECT '%KWA HARRIET%' UNION ALL
            SELECT '%KWAHARRIET%' UNION ALL
            SELECT '%KWA MUMO%' UNION ALL
            SELECT '%KWAMUMO%' UNION ALL
            SELECT '%KWA NJERI%' UNION ALL
            SELECT '%KWA NJOKI%' UNION ALL
            SELECT '%KWA WANJIRU%'
        )
        SELECT t.recipient, SUM(t.amount) AS totalAmount 
        FROM transactions t
        JOIN supermarket_patterns p ON t.recipient LIKE p.pattern
        WHERE t.timestamp >= :sixMonthsAgo
        GROUP BY t.recipient
        ORDER BY totalAmount DESC
        LIMIT 10
    """)
    suspend fun getSupermarketTransactions(sixMonthsAgo: Long): List<RecipientSummary>
}