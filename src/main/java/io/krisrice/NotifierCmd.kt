package io.krisrice

import java.sql.Connection
import java.sql.SQLException

import fr.jcgay.notification.Application
import fr.jcgay.notification.Icon
import fr.jcgay.notification.Notification
import fr.jcgay.notification.Notification.Level
import fr.jcgay.notification.Notifier
import fr.jcgay.notification.SendNotification
import oracle.dbtools.extension.SQLCLService
import oracle.dbtools.raptor.newscriptrunner.CommandListener
import oracle.dbtools.raptor.newscriptrunner.IHelp
import oracle.dbtools.raptor.newscriptrunner.ISQLCommand
import oracle.dbtools.raptor.newscriptrunner.ScriptRunnerContext

class NotifierCmd : CommandListener(), IHelp, SQLCLService {
    private var notifier: Notifier? = null
    private var sqlclIcon: Icon? = null
    private var rockOnIcon: Icon? = null
    private var failedIcon: Icon? = null

    override fun handleEvent(conn: Connection?, ctx: ScriptRunnerContext, cmd: ISQLCommand): Boolean {
        return false
    }

    override fun beginEvent(conn: Connection?, ctx: ScriptRunnerContext, cmd: ISQLCommand) {
        ctx.putProperty("notifier.start", System.currentTimeMillis())
    }

    override fun beginScript(conn: Connection?, ctx: ScriptRunnerContext?) {}

    override fun endScript(conn: Connection?, ctx: ScriptRunnerContext?) {}


    override fun endEvent(conn: Connection?, ctx: ScriptRunnerContext, cmd: ISQLCommand) {
        if (notifier == null) {
            // initizlize the notifier

            val sqlclUrl = NotifierCmd::class.java!!.getResource("images/sql-command-line-200.png")

            val rockOnUrl = NotifierCmd::class.java!!.getResource("images/rock_on.png")

            val failedUrl = NotifierCmd::class.java!!.getResource("images/failed_emoji.png")

            sqlclIcon = Icon.create(sqlclUrl, "sqlcl.icon")

            rockOnIcon = Icon.create(rockOnUrl, "rockon.icon")

            failedIcon = Icon.create(failedUrl, "failed.icon")

            val app = Application.builder("sqlcl", "SQLcl", sqlclIcon).build()

            notifier = SendNotification().setApplication(app).initNotifier()
        }

            // send it
             notifier?.let {
                val username = conn?.metaData?.userName ?:""

                val elap = ((System.currentTimeMillis() - ctx.getProperty("notifier.start") as Long) / 1000).toString() + "s"

                val err = ctx.getProperty("sqldev.error") as Boolean

                // grab first word.. select..delete...
                val type = cmd.sqlOrig.split(" ").toTypedArray()[0]

                if (err) {
                    notifier!!.send(Notification.builder("not used", username + " :" + type + "\n" + ctx.getProperty("sqldev.last.err.message.forsqlcode"), failedIcon).level(Level.ERROR).build())
                } else {
                    notifier!!.send(Notification.builder("not used", "$username :$type Completed.\nElapsed $elap", rockOnIcon).build())
                }

            } ?:let {
                ctx.write("Could not send notification\n")
            }


    }

    override fun getCommandListener(): Class<out CommandListener> {
        return NotifierCmd::class.java
    }

    override fun getCommand(): String {
        return "notifier"
    }

    override fun getHelp(): String {
        return "not an actual command just listening"
    }

    override fun isSqlPlus(): Boolean {
        return false
    }

}
