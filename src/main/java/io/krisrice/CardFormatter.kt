package io.krisrice

import com.github.lalyos.jfiglet.FigletFont
import oracle.dbtools.raptor.format.CopyFormatter
import oracle.dbtools.raptor.format.ResultsFormatter
import oracle.dbtools.raptor.utils.DataTypesUtil
import java.io.IOException
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;

class CardFormatter : ResultsFormatter("CARD", "card format", "card") {

    override fun setTableName(p0: String?) {
        // noop
    }

    // used for set sqlfomat card
    val TYPE = "CARD"

    val EXT = "card"

    var longestColName=0;

    @Throws(IOException::class)
    override fun start() {
        // runs at the start of any print
        val colCount = getColumnCount();
        // get widest name to get alignment
        for (i in 0..colCount){
            longestColName = if ( getColumnName(i).length> longestColName) {getColumnName(i).length } else {longestColName};
        }
    }

    @Throws(IOException::class)
    override fun startRow() {
        // runs at the start of a row
    }

    @Throws(IOException::class)
    override fun printColumn(col: Any?, viewIndex: Int, modelIndex: Int) {
        // runs for every col.

        // get current col name
        val name =   getColumnName(viewIndex).toLowerCase()
        // get current value as a string
        val value = if ( col != null ) DataTypesUtil.stringValue(col, scriptContext.currentConnection) else " "
        // print it
        write(String.format("%-"+longestColName+"s : %s\n",name,value));
    }

    @Throws(IOException::class)
    override fun endRow() {
        // runs at the end of every row
        write("\n")
    }


    @Throws(IOException::class)
    override fun end() {
        // runs at the end of everything
    }
}
