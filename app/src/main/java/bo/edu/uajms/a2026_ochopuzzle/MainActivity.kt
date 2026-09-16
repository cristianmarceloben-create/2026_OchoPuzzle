package bo.edu.uajms.a2026_ochopuzzle

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var BTNButtons: Array<Button>
    private lateinit var TXVMessage: TextView
    private lateinit var BTNRestart: Button
    private lateinit var BTNDisorder: Button
    private lateinit var BTNVerify: Button

    private lateinit var Tablero: Array<Array<String>>

    private val rows = 4
    private val cols = 4

    private val TableroOrdenado = arrayOf(
        arrayOf("1", "2", "3", "4"),
        arrayOf("12", "13", "14", "5"),
        arrayOf("11", "0", "15", "6"),
        arrayOf("10", "9", "8", "7")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BTNButtons = arrayOf(
            findViewById(R.id.BTN00),
            findViewById(R.id.BTN01),
            findViewById(R.id.BTN02),
            findViewById(R.id.BTN03),

            findViewById(R.id.BTN10),
            findViewById(R.id.BTN11),
            findViewById(R.id.BTN12),
            findViewById(R.id.BTN13),

            findViewById(R.id.BTN20),
            findViewById(R.id.BTN21),
            findViewById(R.id.BTN22),
            findViewById(R.id.BTN23),

            findViewById(R.id.BTN30),
            findViewById(R.id.BTN31),
            findViewById(R.id.BTN32),
            findViewById(R.id.BTN33)
        )

        TXVMessage = findViewById(R.id.TXVMessage)
        BTNRestart = findViewById(R.id.BTNRestart)
        BTNDisorder = findViewById(R.id.BTNShuffle)
        BTNVerify = findViewById(R.id.BTNVerify)

        createOrderedBoard()
        updateBoard()

        for (i in BTNButtons.indices) {
            val row = i / cols
            val col = i % cols

            BTNButtons[i].setOnClickListener {
                moveTile(row, col)
            }
        }

        BTNRestart.setOnClickListener {
            restartGame()
        }

        BTNDisorder.setOnClickListener {
            disorderBoard()
        }

        BTNVerify.setOnClickListener {
            verifyBoard()
        }
    }

    private fun createOrderedBoard() {
        Tablero = Array(rows) { row ->
            Array(cols) { col ->
                TableroOrdenado[row][col]
            }
        }
    }

    private fun updateBoard() {
        for (i in BTNButtons.indices) {
            val row = i / cols
            val col = i % cols
            val value = Tablero[row][col]

            if (value == "0") {
                BTNButtons[i].text = ""
            } else {
                BTNButtons[i].text = value
            }
        }
    }

    private fun moveTile(row: Int, col: Int) {
        val blankPosition = findBlank()
        val blankRow = blankPosition.first
        val blankCol = blankPosition.second

        val distance =
            abs(row - blankRow) + abs(col - blankCol)

        if (distance == 1) {
            swapTiles(
                row,
                col,
                blankRow,
                blankCol
            )

            updateBoard()
        }
    }

    private fun findBlank(): Pair<Int, Int> {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (Tablero[i][j] == "0") {
                    return Pair(i, j)
                }
            }
        }

        return Pair(2, 1)
    }

    private fun swapTiles(
        firstRow: Int,
        firstCol: Int,
        secondRow: Int,
        secondCol: Int
    ) {
        val value = Tablero[firstRow][firstCol]

        Tablero[firstRow][firstCol] =
            Tablero[secondRow][secondCol]

        Tablero[secondRow][secondCol] = value
    }

    private fun restartGame() {
        createOrderedBoard()
        updateBoard()
        TXVMessage.text = "Juego Reiniciado"
    }

    private fun disorderBoard() {
        val values = mutableListOf<String>()

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                values.add(Tablero[i][j])
            }
        }

        values.shuffle()

        var position = 0

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                Tablero[i][j] = values[position]
                position++
            }
        }

        updateBoard()
        TXVMessage.text = "Completado"
    }

    private fun verifyBoard() {
        if (isOrdered()) {
            TXVMessage.text = "Juego Ordenado"
        } else {
            TXVMessage.text = "Juego Desordenado"
        }
    }

    private fun isOrdered(): Boolean {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (Tablero[i][j] != TableroOrdenado[i][j]) {
                    return false
                }
            }
        }

        return true
    }
}