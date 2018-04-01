
%%*************************************%%
%% Demo file illustrating some facts   %%
%% and rules in Prolog                 %%
%%*************************************%%

%%*************************************%%
%% FACTS                               %%
%%*************************************%%                        
indian(curry).
indian(dahl).
indian(tandoori).
indian(kurma).

chinese(chow_mein).
chinese(chop_suey).
chinese(sweet_and_sour).

italian(pizza).
italian(spaghetti).

mild(dahl).
mild(tandoori).
mild(sweet_and_sour).
spicy(kurma).
spicy(spaghetti).

hot(curry).

%%*************************************%%
%% RULES                               %%
%%*************************************%% 

likes(sam,Food) :-
        indian(Food),
        mild(Food).

likes(sam,Food) :-
        italian(Food),
        spicy(Food).

likes(darcie,Food) :-
        chinese(Food),
        mild(Food).

%%*************************************%%
%% query                               %%
%%*************************************%%

q1(Person,Food) :- likes(Person,Food).
q2(X) :- likes(sam,X), likes(darcie,X).
