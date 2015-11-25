# oware
Oware computational program by Sander Breukink and Siqi Li
https://en.wikipedia.org/wiki/Oware

Project description: 

  0 |  1 |  2 |  3 |  4 |  5 |  6 |  7 |  8 |  9 | 10 | 11
----|----|----|----|----|----|----|----|----|----|----|----
 23 | 22 | 21 | 20 | 19 | 18 | 17 | 16 | 15 | 14 | 13 | 12 

pits 0 - 11 are mine and pits 12 - 23 are the opponents
Each pit has 4 seeds at the beginning.
Note: Move clockwise!!

We implemented minmax/alpha beta algorithm and designed this program for playing OWARE game.

Valid Move:
We will say that a player is starving if and only if he has no more move to play.
A move is valid if after playing it the opponent is not starving and that's all!
Note that this means that it is not acceptable to take all the seeds of the opponent.

End Conditions:
The game is finished when the player who has to play has no more any valid move. When the game is finished the player with the most number of seeds wins the game.
The game is also finished when a player took more than half of the seeds.

