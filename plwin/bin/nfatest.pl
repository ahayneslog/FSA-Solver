% Alphabet Facts
isX(x).
isY(y).
isZ(z).
isA(a).

% State Transitions

state0([]) :- write('String rejected.').
state0([H|T]) :- isX(H) -> state1(T).
state0([H|T]) :- isX(H) -> state2(T) ; fail.
state1([]) :- fail.
state1([H|T]) :- isX(H) -> state1(T).
state1([H|T]) :- isX(H) -> state3(T) ; fail. 
state2([]) :- fail.
state2([H|T]) :- isY(H) -> state2(T).
state2([H|T]) :- isY(H) -> state5(T) ; fail.
state3([]) :- fail.
state3([H|T]) :- isZ(H) -> state4(T).
state3([H|T]) :- isZ(H) -> state5(T) ; fail.
state4([]) :- write('String accepted.').
state5([]) :- fail.
state5([H|T]) :- isA(H) -> state6(T).
state5([H|T]) :- isA(H) -> state3(T).
state6([]) :- write('String accepted.').
