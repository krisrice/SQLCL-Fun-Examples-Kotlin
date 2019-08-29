package io.krisrice

import com.github.lalyos.jfiglet.FigletFont
import oracle.dbtools.raptor.format.CopyFormatter
import oracle.dbtools.raptor.format.ResultsFormatter
import oracle.dbtools.raptor.utils.DataTypesUtil
import java.io.IOException
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.Ansi.Attribute
import oracle.dbtools.raptor.newscriptrunner.ScriptRunnerContext



class FigletFormatter : ResultsFormatter("FIGLET", "Figlet format", "figlet") {

    override fun setTableName(p0: String?) {
        // noop
    }

    val TYPE = "FIGLET"
    val EXT = "figlet"

    val list: kotlin.collections.ArrayList<String> = java.util.ArrayList()

    var figlet:String?= null

    @Throws(IOException::class)
    override fun start() {
        val formatterOptions = scriptContext.getProperty(ScriptRunnerContext.SQL_FORMAT_FULL) as Array<String>

        // [0] = set
        // [1] = sqlformat
        // [2] = "figlet"
        // [3] = extra params

        // optionally get the specified figlet
        // Example: set figlet /path/to/lcd.flf
        figlet = formatterOptions.getOrNull(3)

        write("Using:" + (if (figlet != null) figlet else "Standard"))
    }

    @Throws(IOException::class)
    override fun startRow() {
    }

    @Throws(IOException::class)
    override fun printColumn(col: Any?, viewIndex: Int, modelIndex: Int) {
        val name =   getColumnName(viewIndex).toLowerCase()
        val value = if ( col != null ) DataTypesUtil.stringValue(col, scriptContext.currentConnection) else " "

        list.add(value)
    }

    @Throws(IOException::class)
    override fun endRow() {
        var all = list.joinToString(" ")

        if ( figlet==null ) {
            write(FigletFont.convertOneLine(all))
        } else {
            write(FigletFont.convertOneLine(figlet,all))
        }
        write("\n")
        list.clear()
    }

    @Throws(IOException::class)
    override fun end() {
    }
}
