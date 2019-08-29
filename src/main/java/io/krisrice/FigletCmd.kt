package io.krisrice

import java.sql.Connection

import com.github.lalyos.jfiglet.FigletFont

import oracle.dbtools.extension.SQLCLService
import oracle.dbtools.raptor.format.FormatRegistry
import oracle.dbtools.raptor.newscriptrunner.CommandListener
import oracle.dbtools.raptor.newscriptrunner.IHelp
import oracle.dbtools.raptor.newscriptrunner.ISQLCommand
import oracle.dbtools.raptor.newscriptrunner.ScriptRunnerContext
import java.io.IOException

class FigletCmd : CommandListener(), IHelp, SQLCLService {

    var figletFont:String="";

    override fun handleEvent(conn: Connection?, ctx: ScriptRunnerContext, cmd: ISQLCommand): Boolean {

        // register new formatters of not registered

        if ( ! FormatRegistry.getTypes().contains("figlet") ) {
            FormatRegistry.registerFormater( FigletFormatter())
        }
        if ( ! FormatRegistry.getTypes().contains("card") ) {
            FormatRegistry.registerFormater( CardFormatter())
        }

        // now do the banner command

        if (CommandListener.matches("banner", cmd.sqlOrig)) {
            var sql = cmd.sqlOrig
                    .replace("\n", " ")
                    .replace("banner ", " ").trim()

            var asciiArt:String? = null;

            if ( figletFont != "" ) {
                asciiArt = FigletFont.convertOneLine(figletFont, sql)
            } else {
                asciiArt = FigletFont.convertOneLine(sql)
            }

            ctx.write(asciiArt)
            return true
        } else  if (CommandListener.matches("figlet ", cmd.sqlOrig)) {
            // change the figlet to other than standard
            var test =cmd.sqlOrig.replace("figlet ","").trim();

            try {
                var msg = FigletFont.convertOneLine(test, "Banner Figlet Font Changed")
                ctx.write(msg + "\n")
                figletFont = test;

            } catch (e: Exception){
                ctx.write("Font Not Found:" + test + "\n" + e.localizedMessage + "\n")
                e.printStackTrace()
            }

            return true;

        }

            return false
    }

    override fun beginEvent(conn: Connection?, ctx: ScriptRunnerContext, cmd: ISQLCommand) {}

    override fun endEvent(conn: Connection?, ctx: ScriptRunnerContext, cmd: ISQLCommand) {}

    override fun getCommandListener(): Class<out CommandListener> {
        return FigletCmd::class.java
    }

    override fun getCommand(): String {
        return "banner"
    }

    override fun getHelp(): String {
        return "something helpful"
    }

    override fun isSqlPlus(): Boolean {
        return false
    }
}
