package nl.entreco.data.db.bot

import androidx.annotation.WorkerThread
import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.model.players.Bot
import nl.entreco.domain.repository.BotRepository
import java.util.Locale

class LocalBotRepository(
        db: DscDatabase,
        private val mapper: BotMapper
) : BotRepository {

    private val botDao: BotDao = db.botDao()

    @WorkerThread
    override fun create(name: String, level: Float): Long {
        val bot = BotTable()
        bot.name = name.toLowerCase(Locale.getDefault())
        bot.level = if(level == 0F) 1F else level
        return botDao.create(bot)
    }

    @WorkerThread
    override fun fetchByName(name: String): Bot? {
        val table = botDao.fetchByName(name) ?: return null
        return mapper.to(table)
    }

    @WorkerThread
    override fun fetchById(id: Long): Bot? {
        val table = botDao.fetchById(id) ?: return null
        return mapper.to(table)
    }

    @WorkerThread
    override fun fetchAll(): List<Bot> {
        val table = botDao.fetchAll()
        return mapper.to(table)
    }
}