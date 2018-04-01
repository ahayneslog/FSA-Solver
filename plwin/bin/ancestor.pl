
%%*************************************%%
%% Demo file illustrating some facts   %%
%% and rules in Prolog                 %%
%%*************************************%%

%%*************************************%%
%% FACTS                               %%
%%*************************************%%                        
father(albert,jeffrey).
father(george,victor).
father(albert,george).
father(george,cindy).
father(john,mary).

mother(sue,mary).
mother(mary,cindy).
mother(alice,george).
mother(mary,victor).
mother(alice,jeffrey).
%%*************************************%%
%% RULES                               %%
%%*************************************%% 
parent(X,Y) :- father(X,Y).

parent(X,Y) :- mother(X,Y).

grandparent(X,Z) :- parent(X,Y),parent(Y,Z).

ancestor(X,Z) :- parent(X,Z).

ancestor(X,Z) :- parent(X,Y),ancestor(Y,Z).

sibling(X,Y) :- mother(M,X),mother(M,Y),
            father(F,X),father(F,Y),X \= Y.

cousin(X,Y) :- parent(U,X),parent(V,Y),
               sibling(U,V).


