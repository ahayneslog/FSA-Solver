isa(a).
isb(b).
isFinal(yes).

state0([H|T]) :- isa(H) -> state0(T).
state0([H|T]) :- isb(H) -> state0(T).
state0([H|T]) :- isa(H) -> state1(T).
state1([]) :- isFinal(yes).
fsa(List) :- state0(List).

