# KungFuPenguin

A Card Game built to demostrate understanding of Android Studio and Kotlin

Group Project of Nolawe Temketem, Felix Ogordi, Rhea Ovungal, and Lilli Terry

Model-View-Controller architecture is used in GameModel.kt(Player.kt and Cards.kt) GameView.kt and GameActivity.kt

Icon for the App is a Penguin

3 Views activtiy_main.xml, activtiy_instructions.xml, GameView.kt, activity_gameover.xml

Local persistent data:  store color and name of penguin. used to determining the player/penguin  in  game (MainActivity.kt)
Remote persistent data:  firebase to have a leaderboard with total wins and losses for each player (GameOverActivity.kt)

Requires meaningful use of app or google service : Use of a Timer when playing (GameActivity.kt and GameView.kt)

Two New GUI components
Spinner for users to choose penguin color(Uses Listener)
FrameLayout used in activtiy_instructions.xml and activtiy_main.xml

Advertising:
Interstitial Ad after  the GameOver screen when user to leaves screen

