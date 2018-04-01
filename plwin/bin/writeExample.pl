common_participant(john, programming).
common_participant(sue, crossstitch).
common_participant(john, rollercoasters).
common_participant(sue, rollercoasters).

print_participants :-
    common_participant(Person, PairEvent),
    write(Person), write(' participates in '), write(PairEvent), write('.'), nl,
    fail.
print_participants :-
    true.