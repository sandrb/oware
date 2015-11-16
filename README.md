# oware
Oware computational program by Sander Breukink en Siqi Li
https://en.wikipedia.org/wiki/Oware

Project description: 
Dear students,

We have a short course tomorrow morning at 10:15 in TD14
We will work on minmax/alpha beta algorithm
You will have to implement these algorithms and design a program for playing OWARE game.
Please have a look at the Wikipedia page.
https://en.wikipedia.org/wiki/Oware
We are going to modify the game because we need to complexify it.
Instead of having 6 cells/holes we will have 12 holes per player. We also multiply the number of seeds by a factor of 2.
Then, we will use a simple rule for defining when a move is valid.
We will say that a player is starving if and only if he has no more move to play.
A move is valid if after playing it the opponent is not starving and that’s all!

Note that this means that it is not acceptable to take all the seeds of the opponent
The game is finished when the player who has to play has no more any valid move.
When the game is finished the player with the most number of seeds wins the game.
The game is also finish when a player took more than half of the seeds.
You will find attached a synopsis of the code.

See you tomorrow

Best regards

jcr
