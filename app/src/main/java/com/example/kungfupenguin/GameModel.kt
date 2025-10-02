package com.example.kungfupenguin

class GameModel(val player1: Player, val player2: Player) {

    private val winScore = 3
    private var round = 1

    private var winner : String? = null
    private var loser: String? = null



    fun playRound(index1: Int, index2: Int): Int {
        var card1: Card? = player1.playCard(index1)
        var card2: Card? = player2.playCard(index2)
        round++

        if (card2 == null || card1 == null) {
            return -2
        }

        val result = card2.beats(card1)

         if (result == 1) {
                player2.incrementScore()
         } else if(result == -1 ) {
                player1.incrementScore()
         }

        refillHands()
        return result
    }

    private fun refillHands() {
        while (player1.getHand().size < 5) player1.drawCard()
        while (player2.getHand().size < 5) player2.drawCard()
    }

    fun isGameOver(): Boolean {
        val gameover : Boolean = player1.getScore() == winScore || player2.getScore() == winScore

        if (gameover) {
            if (player1.getScore() == winScore) {
                this.winner =  player1.getName()
                this.loser =  player2.getName()

            } else {
                this.winner =  player2.getName()
                this.loser =  player1.getName()
            }

        }
        return gameover
    }

    fun getWinner(): String?  {
        return this.winner
    }

    fun getLoser(): String?  {
        return this.loser
    }

    fun getRound(): Int = this.round
}
