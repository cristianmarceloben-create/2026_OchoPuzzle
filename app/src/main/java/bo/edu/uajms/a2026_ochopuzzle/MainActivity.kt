package bo.edu.uajms.a2026_ochopuzzle

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var BTNTablero: Array<Button>
    private lateinit var TXVMessage: TextView
    private lateinit var BTNRestart: Button
    private lateinit var BTNShuffle: Button
    private lateinit var BTNVerify: Button
    private lateinit var Tablero: Array<Array<Int>>

    private val rows = 4
    private val cols = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BTNTablero = arrayOf(
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
        BTNShuffle = findViewById(R.id.BTNShuffle)
        BTNVerify = findViewById(R.id.BTNVerify)

        createSolvedBoard()
        updateBoard()

        for (i in BTNTablero.indices) {
            val row = i / cols
            val col = i % cols

            BTNTablero[i].setOnClickListener {
                moveTile(row, col)
            }
        }

        BTNRestart.setOnClickListener {
            restartGame()
        }

        BTNShuffle.setOnClickListener {
            shuffleBoard()
        }

        BTNVerify.setOnClickListener {
            verifyPuzzle()
        }
    }

    private fun createSolvedBoard() {
        Tablero = Array(rows) {
            Array(cols) { 0 }
        }

        var number = 1

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (i == rows - 1 && j == cols - 1) {
                    Tablero[i][j] = 0
                } else {
                    Tablero[i][j] = number
                    number++
                }
            }
        }
    }

    private fun updateBoard() {
        for (i in BTNTablero.indices) {
            val row = i / cols
            val col = i % cols
            val value = Tablero[row][col]

            if (value == 0) {
                BTNTablero[i].text = ""
            } else {
                BTNTablero[i].text = value.toString()
            }
        }
    }

    private fun moveTile(row: Int, col: Int) {
        val blankPosition = findBlank()
        val blankRow = blankPosition.first
        val blankCol = blankPosition.second

        val distance = abs(row - blankRow) + abs(col - blankCol)

        if (distance == 1) {
            swapTiles(row, col, blankRow, blankCol)
            updateBoard()
            TXVMessage.text = getString(R.string.message)
        }
    }

    private fun findBlank(): Pair<Int, Int> {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (Tablero[i][j] == 0) {
                    return Pair(i, j)
                }
            }
        }

        return Pair(rows - 1, cols - 1)
    }

    private fun swapTiles(
        firstRow: Int,
        firstCol: Int,
        secondRow: Int,
        secondCol: Int
    ) {
        val value = Tablero[firstRow][firstCol]
        Tablero[firstRow][firstCol] = Tablero[secondRow][secondCol]
        Tablero[secondRow][secondCol] = value
    }

    private fun restartGame() {
        createSolvedBoard()
        updateBoard()
        TXVMessage.text = getString(R.string.message)
    }

    private fun shuffleBoard() {
        do {
            createSolvedBoard()

            repeat(200) {
                moveBlankRandomly()
            }
        } while (isSolved())

        updateBoard()
        TXVMessage.text = getString(R.string.message)
    }

    private fun moveBlankRandomly() {
        val blankPosition = findBlank()
        val blankRow = blankPosition.first
        val blankCol = blankPosition.second
        val possibleMoves = mutableListOf<Pair<Int, Int>>()

        if (blankRow > 0) {
            possibleMoves.add(Pair(blankRow - 1, blankCol))
        }

        if (blankRow < rows - 1) {
            possibleMoves.add(Pair(blankRow + 1, blankCol))
        }

        if (blankCol > 0) {
            possibleMoves.add(Pair(blankRow, blankCol - 1))
        }

        if (blankCol < cols - 1) {
            possibleMoves.add(Pair(blankRow, blankCol + 1))
        }

        val selectedMove = possibleMoves[Random.nextInt(possibleMoves.size)]

        swapTiles(
            selectedMove.first,
            selectedMove.second,
            blankRow,
            blankCol
        )
    }
    private fun verifyPuzzle() {
        if (isSolved()) {
            TXVMessage.text = getString(R.string.correctMessage)
        } else {
            TXVMessage.text = getString(R.string.incorrectMessage)
        }
    }
    private fun isSolved(): Boolean {
        var expectedNumber = 1

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (i == rows - 1 && j == cols - 1) {
                    return Tablero[i][j] == 0
                }

                if (Tablero[i][j] != expectedNumber) {
                    return false
                }

                expectedNumber++
            }
        }

        return true
    }
}