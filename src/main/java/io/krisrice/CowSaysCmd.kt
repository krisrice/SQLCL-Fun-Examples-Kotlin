package io.krisrice

import java.sql.Connection

import com.github.lalyos.jfiglet.FigletFont

import oracle.dbtools.extension.SQLCLService
import oracle.dbtools.raptor.format.FormatRegistry
import oracle.dbtools.raptor.newscriptrunner.CommandListener
import oracle.dbtools.raptor.newscriptrunner.IHelp
import oracle.dbtools.raptor.newscriptrunner.ISQLCommand
import oracle.dbtools.raptor.newscriptrunner.ScriptRunnerContext
import com.github.ricksbrown.cowsay.Cowsay



class CowSaysCmd : CommandListener(), IHelp, SQLCLService {
    override fun handleEvent(conn: Connection?, ctx: ScriptRunnerContext, cmd: ISQLCommand): Boolean {

        /*
          Cause why not....

         */

        if (CommandListener.matches("cowsays", cmd.sqlOrig)) {
            var sql = cmd.sqlOrig
                    .replace("\n", " ")
                    .replace("cowsays ", " ").trim()
            var parts = sql.split(" ");
            val args:Array<String>;

            if (parts[0] == "-f"){
                val saying = parts.subList(2,parts.size)
                args = arrayOf("-f", parts[1], saying.joinToString ( " " ))
            } else {
                args = arrayOf("-f", "cow", sql)
            }

            val result = Cowsay.say(args)

            ctx.write(result)
            return true
        }

            return false
    }

    override fun beginEvent(conn: Connection?, ctx: ScriptRunnerContext, cmd: ISQLCommand) {}

    override fun endEvent(conn: Connection?, ctx: ScriptRunnerContext, cmd: ISQLCommand) {}

    override fun getCommandListener(): Class<out CommandListener> {
        return CowSaysCmd::class.java
    }

    override fun getCommand(): String {
        return "cowsays"
    }

    override fun getHelp(): String {
        return "Cows Says"
    }

    override fun isSqlPlus(): Boolean {
        return false
    }
}
