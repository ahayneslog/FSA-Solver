% Part One for Project 5. 
% Create an FSA Solver for Diagram provided on http://uwf.edu/jcoffey/PLA5_201501.htm

% Alphabet Facts
isX(x).
isY(y).
isZ(z).
isA(a).
% State Checker
isFinal(yes).

% State Transitions
state0([H|T]) :- isX(H) -> state0(T).
state0([H|T]) :- isY(H) -> state1(T).
state1([]) :- isFinal(yes).
state1([H|T]) :- isX(H) -> state2(T).
state2([H|T]) :- isX(H) -> state2(T).
state2([H|T]) :- isY(H) -> state3(T).
state3([]) :- isFinal(yes).
state3([H|T]) :- isX(H) -> state3(T).
state3([H|T]) :- isZ(H) -> state4(T).
state4([H|T]) :- isX(H) -> state4(T).
state4([H|T]) :- isA(H) -> state1(T).

% FSA Rule
fsa(List):- state0(List).

% Example Queries
good :- fsa([x,y,x,y,x,x,z,x,x,x,x,a]).
bad :- fsa([y,x,y,x,x,x,x,x,x,z]).