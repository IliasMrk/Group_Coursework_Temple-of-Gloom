# Software and Programming III


## Group Coursework: Chris, Ilias, and Paraskevas

#### Session: 2023-24

Methodology used to complete the coursework - working as a team
==========

Version Control Commit Method
----------
* We are using a variation of Gitflow workflow to manage our branches and commits.
* We have a main branch, a dev branch and feature branches belonging to the 3 developers.
* The main branch will be used to store the final version of the code.
* The dev branch will be used to merge the feature branches.
* The feature branches are used to develop the code.
* We are using GitFlow to protect our main branch from unwanted changes or mistakes when committing.

Cooperation Method - Trello Boards
----------
* We are using Trello to manage our tasks.
* We discuss the tasks that need to be done and assign them to the developers.
* Tasks are prioritised using the MoSCoW approach.
* We also create issues on GitHub off the back of the tasks on the Trello board, to keep track of the tasks that need to be done and use them in our commit messages.
* Example : "Subject : RELATES TO #17 - Adding some basic unit tests to new dev codebase"

Cooperation Method - Teams Meetings
----------
* We conduct weekly agile meetings to discuss the progress of the project.
* We discuss the tasks that need to be done and assign them each other.
* We have a weekly agile development cycle and each week we have a sprint review to discuss the progress of the project.
* This is done to ensure that we are on track and that we are meeting the deadlines as the project is 1 month long.
* We provide feedback to each other when it comes to the code solutions employed.

* Cooperation Method - Slack
----------
* We use Slack to communicate with each other throughout the week.
* We use Slack to discuss the challenges we face and to ask for help when needed.
* We also report our progress on Slack and discuss the tasks that need to be done.

Tests Method
----------
* Before any code is pushed to dev, we run 500 headless runs on that code, and ensure it fails exactly zero
  times.
* To ensure that any new approach is an improvement over previous iterations, we take the results from the 500
  headless runs, and collect the results using a python script (held in
  /TEMPORARY SCRIPTS/TEMPORARY_SCRIPT_get_results_from_headless_mode.py). We compare the average bonus / gold
  collected to previous results, and only accept approaches which show improvement.
* We use JUnit to test our code in a separate folder called "test". These tests ensure that individual public
  methods are working as expected.

Solving the mazes
=========
### `explore()`
Our approach involves building up knowledge of the underlying Nodes. When a new Node is visited, its
neighbours (including their id's and distances from the orb) are recorded.

Whenever the player visits a new Node, the code picks the "best" next unvisited Node to visit (using a
heuristic based on the current distance to that Node, and the distance from that Node to the orb).

Once the best Node has been selected, the player makes its way to that Node, using the shortest known path
(determined using an adaptation of Dijkstra).

Once the player arrives at the best Node, the process repeats, until the orb is reached.

### `escape()`
The code begins by identifying the top *n* Nodes with the highest gold. It then attempts to construct a path
from the start point, via each of the *n* Nodes in turn, to the exit. The path is determined using the same
path-finding code as used in the explore phase.

If no such path can be made within the time remaining, *n* is decremented, and the process is repeated, until
a valid path is found.

Once this initial path is found, "diversions" are added in - i.e. short walks away from the initial path, to
pick up additional gold, then returning to and resuming the main path.

The optimal diversions are calculated by finding all the Nodes which are close to the main path, and working
out how quickly they can be reached. These diversions are sorted according to their value divided by
their length. Then, one by one, they are checked to make sure they can be completed within the time
remaining, and, if so, are added into the path.

The player then moves along the updated path, picking up gold, and reaching the exit in time.

Classes Used
----------
* Classes are set up as per the following:
   * `student` package
    * `Explorer`: The class called by main(). `Explorer` is lightweight, and makes use of the Strategy
      Pattern. `explore()` creates an instance of an `ExploreStrategy` and calls it; `escape()` creates an
      instance of an `EscapeStrategy` and calls it.
    * `PathFinder`: A utility class for getting the shortest path between two points. Works for both escape
      and explore phases.
    * `StudentNode`: Abstract class for representing a Node. Its concrete implementations are `ExploreNode`
      and `EscapeNode` (which are used in the explore and escape phases, respectively). This class provides a
      common superclass for nodes in both explore and escape phases, thus allowing PathFinder to use the same
      code in both phases.
  * `student.explore` package
    * `ExploreStrategy`: Minimal interface, requiring just the `explore()` method to be implemented.
    * `BasicFindBestNodeExplore`: Class implementing `ExploreStrategy`. Explores the maze by repeatedly
      finding the "best" next Node to visit, finding the shortest path there, updating its knowledge of the
      maze, and repeating until it reaches the orb.
    * `ExploreNode`: Concrete implementation of `StudentNode`. Represents Nodes, for use in explore phase.
      Because `Node` is not available during the explore phase, this class is used to hold node information,
      and to build up the player's knowledge of the maze.
    * `ExploreGraph`: Collection class for `ExploreNode`. Represents the player's knowledge of the maze at
      any given time.
  * `student.escape` package
    * `EscapeStrategy`: Minimal interface, requiring just the `escape()` method to be implemented.
    * `GetTopNCashWithDiversionsEscape`: Class implementing `EscapeStrategy`. Does two things: (1) constructs
      a path which reaches the exit in time, while travelling via the *n* most valuable nodes; (2) adds in
      diversions from the path, where time allows, to collect extra gold.
    * `EscapeHelper`: Utility class, with various helper methods for the escape phase (such as finding the
      lengths of paths, the value of paths, and finding the Manhattan distance between two Nodes).
    * `DiversionsGenerator`: Utility class, for generating diversions from a path.
    * `EscapeNode`: Concrete implementation of `StudentNode`. Represents Nodes, for use in explore phase.
      Although `Node` is available during the escape phase, `EscapeNode` lets the `PathFinder` methods work
      in both the explore and escape phases.

Best Score
----------
Best score found:

62,096

on Seed 6394073376077183492



Short Description of the running process
----------

Limitations 
----------
* `explore()`:
  * Our heuristic for selecting the "best" next Node to visit is very basic. We have done some
    experimentation to fine-tune the `weight` value for our heuristic, but a better, more sophisticated
    approach is probably possible.
  * Because the explore() phase has access to very limited information, no strategy will ever get the full
    bonus on every run. However, there is additional information which can be inferred from the information
    given, which we don't use. (As a basic example, because we know the maximum number of neighbours for a
    node is 4, if an unvisited node already has four known neighbours, we know there's no additional
    information to be gleaned from that node, and so it's probably not worth visiting.)
  * We did attempt to build (and use) as much inferrable knowledge as possible from the information provided,
    but this approach ran very slowly, and rarely improved the overall bonus received.
* `escape()`:
  * The *n* most valuable `Node`s are visited in order of their Manhattan distance from the start point.
    There is probably a better approach to determining the order, which we would investigate if we had more
    time.
  * Diversions are added to the initial path, and then the path is followed. It could be that, for some
    grids, we'd get a better result by adding in diversions to the *diverted* path, and repeating the process
    until no further diversions can be added.
  * The top-n-nodes approach (as currently implemented) has a drawback - if it is not possible to visit both
    the highest-value node and the exit within the time limit, then the initial path (before diversions are
    added) will simply go straight to the exit. This is true even if visiting the 2nd, 3rd, 4th, 5th, etc.
    most valuable nodes is possible. With more time, we'd implement approaches which address this issue.
